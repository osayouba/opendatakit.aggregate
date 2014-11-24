/*
 * Copyright (C) 2012-2013 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.aggregate.odktables.impl.api;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.opendatakit.aggregate.odktables.DataManager;
import org.opendatakit.aggregate.odktables.DataManager.WebsafeRows;
import org.opendatakit.aggregate.odktables.api.DataService;
import org.opendatakit.aggregate.odktables.api.OdkTables;
import org.opendatakit.aggregate.odktables.api.RealizedTableService;
import org.opendatakit.aggregate.odktables.api.TableService;
import org.opendatakit.aggregate.odktables.exception.BadColumnNameException;
import org.opendatakit.aggregate.odktables.exception.ETagMismatchException;
import org.opendatakit.aggregate.odktables.exception.InconsistentStateException;
import org.opendatakit.aggregate.odktables.exception.PermissionDeniedException;
import org.opendatakit.aggregate.odktables.rest.entity.Row;
import org.opendatakit.aggregate.odktables.rest.entity.RowList;
import org.opendatakit.aggregate.odktables.rest.entity.RowOutcome;
import org.opendatakit.aggregate.odktables.rest.entity.RowOutcomeList;
import org.opendatakit.aggregate.odktables.rest.entity.RowResource;
import org.opendatakit.aggregate.odktables.rest.entity.RowResourceList;
import org.opendatakit.aggregate.odktables.security.TablesUserPermissions;
import org.opendatakit.common.persistence.QueryResumePoint;
import org.opendatakit.common.persistence.exception.ODKDatastoreException;
import org.opendatakit.common.persistence.exception.ODKEntityNotFoundException;
import org.opendatakit.common.persistence.exception.ODKTaskLockException;
import org.opendatakit.common.utils.WebUtils;
import org.opendatakit.common.web.CallingContext;

public class DataServiceImpl implements DataService {
  private final String schemaETag;
  private final DataManager dm;
  private final UriInfo info;

  public DataServiceImpl(String appId, String tableId, String schemaETag, UriInfo info, TablesUserPermissions userPermissions, CallingContext cc)
      throws ODKEntityNotFoundException, ODKDatastoreException {
    this.schemaETag = schemaETag;
    this.dm = new DataManager(appId, tableId, userPermissions, cc);
    this.info = info;
  }

  @Override
  public Response getRows(@QueryParam(CURSOR_PARAMETER) String cursor, @QueryParam(FETCH_LIMIT) String fetchLimit) throws ODKDatastoreException, PermissionDeniedException, InconsistentStateException, ODKTaskLockException, BadColumnNameException {
    int limit = (fetchLimit == null || fetchLimit.length() == 0) ? 2000 : Integer.parseInt(fetchLimit);
    WebsafeRows websafeResult = dm.getRows(QueryResumePoint.fromWebsafeCursor(WebUtils.safeDecode(cursor)), limit);
    RowResourceList rowResourceList = new RowResourceList(getResources(websafeResult.rows),
        WebUtils.safeEncode(websafeResult.websafeRefetchCursor),
        WebUtils.safeEncode(websafeResult.websafeBackwardCursor),
        WebUtils.safeEncode(websafeResult.websafeResumeCursor),
        websafeResult.hasMore, websafeResult.hasPrior);
    return Response.ok(rowResourceList).build();
  }
  
  @Override
  public Response /*RowOutcomeList*/ alterRows(RowList rows)
      throws ODKTaskLockException, ODKDatastoreException, ETagMismatchException,
      PermissionDeniedException, BadColumnNameException, InconsistentStateException {

    ArrayList<RowOutcome> changedRows = dm.insertOrUpdateRows(rows);
    RowOutcomeList outcomes = getOutcomes(changedRows);
    return Response.ok(outcomes).build();
  }

  @Override
  public Response getRow(@PathParam("rowId") String rowId) throws ODKDatastoreException, PermissionDeniedException, InconsistentStateException, ODKTaskLockException, BadColumnNameException {
    Row row = dm.getRow(rowId);
    RowResource resource = getResource(row);
    return Response.ok(resource).build();
  }

  @Override
  public Response createOrUpdateRow(@PathParam("rowId") String rowId, Row row) throws ODKTaskLockException,
      ODKDatastoreException, ETagMismatchException, PermissionDeniedException,
      BadColumnNameException, InconsistentStateException {
    row.setRowId(rowId);

    // changed to behave like the bulk update action.
    // Returns a RowOutcome (was RowResource).
    
    RowList rowList = new RowList();
    ArrayList<Row> rows = new ArrayList<Row>();
    rows.add(row);
    rowList.setRows(rows);
    ArrayList<RowOutcome> changedRows = dm.insertOrUpdateRows(rowList);
    RowOutcomeList outcomes = getOutcomes(changedRows);
    RowOutcome outcome = outcomes.getRows().get(0);
    return Response.ok(outcome).build();
  }

  @Override
  public Response deleteRow(@PathParam("rowId") String rowId, @QueryParam(QUERY_ROW_ETAG) String rowETag) throws ODKDatastoreException, ODKTaskLockException,
      PermissionDeniedException, InconsistentStateException, BadColumnNameException, ETagMismatchException {
    String dataETagOnTableOfModification = dm.deleteRow(rowId, rowETag);
    return Response.ok(dataETagOnTableOfModification).build();
  }

  private RowResource getResource(Row row) {
    String appId = dm.getAppId();
    String tableId = dm.getTableId();
    String rowId = row.getRowId();

    UriBuilder ub = info.getBaseUriBuilder();
    ub.path(OdkTables.class, "getTablesService");
    URI self = ub.clone().path(TableService.class, "getRealizedTable").path(RealizedTableService.class, "getData").path(DataService.class, "getRow")
        .build(appId, tableId, schemaETag, rowId);
    URI table = ub.clone().build(appId, tableId);
    RowResource resource = new RowResource(row);
    try {
      resource.setSelfUri(self.toURL().toExternalForm());
      resource.setTableUri(table.toURL().toExternalForm());
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("unable to convert URL ");
    }
    return resource;
  }

  private ArrayList<RowResource> getResources(List<Row> rows) {
    ArrayList<RowResource> resources = new ArrayList<RowResource>();
    for (Row row : rows) {
      resources.add(getResource(row));
    }
    return resources;
  }

  private RowOutcomeList getOutcomes(ArrayList<RowOutcome> rows) {
    String appId = dm.getAppId();
    String tableId = dm.getTableId();
    // for bandwidth efficiency, do not provide selfUri in response array

    UriBuilder ub = info.getBaseUriBuilder();
    ub.path(OdkTables.class, "getTablesService");
    URI table = ub.clone().build(appId, tableId);
    RowOutcomeList outcomeList = new RowOutcomeList(rows);
    try {
      outcomeList.setTableUri(table.toURL().toExternalForm());
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("unable to convert URL ");
    }
    return outcomeList;
  }
}