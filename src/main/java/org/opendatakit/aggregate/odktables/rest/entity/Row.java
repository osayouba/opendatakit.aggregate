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

package org.opendatakit.aggregate.odktables.rest.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Row {

  @Element(name = "id", required = false)
  private String rowId;

  @Element(name = "etag", required = false)
  private String rowEtag;
  
  @Element(name = "dataEtagAtModification", required=false)
  private String dataEtagAtModification;

  @Element(required = false)
  private boolean deleted;

  @Element(required = false)
  private String createUser;

  @Element(required = false)
  private String lastUpdateUser;

  @Element(required = false)
  private Scope filterScope;

  /**
   * OdkTables metadata column.
   */
  @Element(required = false)
  private String uriUser;

  /**
   * OdkTables metadata column.
   */
  @Element(required = false)
  private String formId;

  /**
   * OdkTables metadata column.
   */
  @Element(required = false)
  private String instanceName;

  /**
   * OdkTables metadata column.
   */
  @Element(required = false)
  private String locale;

  /**
   * OdkTables metadata column.
   */
  @Element(required = false)
  private String timestamp;

  @ElementMap(entry = "entry", key = "column", attribute = true, inline = true)
  private Map<String, String> values;

  /**
   * Construct a row for insertion.
   *
   * @param rowId
   * @param values
   */
  public static Row forInsert(String rowId, Map<String, String> values) {
    Row row = new Row();
    row.rowId = rowId;
    row.values = values;
    row.filterScope = Scope.EMPTY_SCOPE;
    return row;
  }

  /**
   * Construct a row for updating.
   *
   * @param rowId
   * @param rowEtag
   * @param values
   */
  public static Row forUpdate(String rowId, String rowEtag, Map<String, String> values) {
    Row row = new Row();
    row.rowId = rowId;
    row.rowEtag = rowEtag;
    row.values = values;
    return row;
  }

  public Row() {
    this.rowId = null;
    this.rowEtag = null;
    this.dataEtagAtModification = null;
    this.deleted = false;
    this.createUser = null;
    this.lastUpdateUser = null;
    this.filterScope = null;
    this.values = new HashMap<String, String>();
    this.uriUser = null;
    this.formId = null;
    this.instanceName = null;
    this.locale = null;
    this.timestamp = null;
  }

  public String getRowId() {
    return this.rowId;
  }

  public String getRowEtag() {
    return this.rowEtag;
  }
  
  public String getDataEtagAtModification() {
    return this.dataEtagAtModification;
  }

  public boolean isDeleted() {
    return this.deleted;
  }

  public String getCreateUser() {
    return createUser;
  }

  public String getLastUpdateUser() {
    return lastUpdateUser;
  }

  public Scope getFilterScope() {
    return filterScope;
  }

  public Map<String, String> getValues() {
    return this.values;
  }

  public void setRowId(final String rowId) {
    this.rowId = rowId;
  }

  public void setRowEtag(final String rowEtag) {
    this.rowEtag = rowEtag;
  }
  
  public void setDataEtagAtModification(final String dataEtagAtModification) {
    this.dataEtagAtModification = dataEtagAtModification;
  }

  public void setDeleted(final boolean deleted) {
    this.deleted = deleted;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public void setLastUpdateUser(String lastUpdateUser) {
    this.lastUpdateUser = lastUpdateUser;
  }

  public void setFilterScope(Scope filterScope) {
    this.filterScope = filterScope;
  }

  public void setValues(final Map<String, String> values) {
    this.values = values;
  }

  public String getUriUser() {
    return this.uriUser;
  }

  public String getFormId() {
    return this.formId;
  }

  public String getInstanceName() {
    return this.instanceName;
  }

  public String getLocale() {
    return this.locale;
  }

  public String getTimestamp() {
    return this.timestamp;
  }

  public void setUriUser(String uriUser) {
    this.uriUser = uriUser;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }

  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  /**
   * Expects a string as generated by {@link WebUtils#iso8601Date(Date)}.
   *
   * @param timestamp
   */
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Sets the row's stringified date field.
   *
   * @param timestamp
   */
  public void setTimestamp(Date timestamp) {
    if ( timestamp == null ) {
      this.timestamp = null;
    } else {
      DateTime dt = new DateTime(timestamp);
      DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
      this.timestamp = fmt.print(dt);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((rowId == null) ? 0 : rowId.hashCode());
    result = prime * result + ((rowEtag == null) ? 0 : rowEtag.hashCode());
    result = prime * result + ((dataEtagAtModification == null) ? 
        0 : dataEtagAtModification.hashCode());
    result = prime * result + ((deleted) ? 0 : 1);
    result = prime * result + ((createUser == null) ? 0 : createUser.hashCode());
    result = prime * result + ((lastUpdateUser == null) ? 0 : lastUpdateUser.hashCode());
    result = prime * result + ((filterScope == null) ? 0 : filterScope.hashCode());
    result = prime * result + ((uriUser == null) ? 0 : uriUser.hashCode());
    result = prime * result + ((formId == null) ? 0 : formId.hashCode());
    result = prime * result + ((instanceName == null) ? 0 : instanceName.hashCode());
    result = prime * result + ((locale == null) ? 0 : locale.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
    result = prime * result + ((values == null) ? 0 : values.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof Row)) {
      return false;
    }
    Row other = (Row) obj;
    return (rowId == null ? other.rowId == null : rowId.equals(other.rowId))
        && (rowEtag == null ? other.rowEtag == null : rowEtag.equals(other.rowEtag))
        && (dataEtagAtModification == null ? other.dataEtagAtModification == null :
            dataEtagAtModification.equals(dataEtagAtModification))
        && (deleted == other.deleted)
        && (createUser == null ? other.createUser == null : createUser.equals(other.createUser))
        && (lastUpdateUser == null ? other.lastUpdateUser == null : lastUpdateUser
            .equals(other.lastUpdateUser))
        && (filterScope == null ? other.filterScope == null : filterScope.equals(other.filterScope))
        && (uriUser == null ? other.uriUser == null : uriUser.equals(other.uriUser))
        && (formId == null ? other.formId == null : formId.equals(other.formId))
        && (instanceName == null ? other.instanceName == null : instanceName
            .equals(other.instanceName))
        && (locale == null ? other.locale == null : locale.equals(other.locale))
        && (timestamp == null ? other.timestamp == null : timestamp.equals(other.timestamp))
        && (values == null ? other.values == null : values.equals(other.values));
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Row [rowId=");
    builder.append(rowId);
    builder.append(", rowEtag=");
    builder.append(rowEtag);
    builder.append(", dataEtagAtModification=");
    builder.append(dataEtagAtModification);
    builder.append(", deleted=");
    builder.append(deleted);
    builder.append(", createUser=");
    builder.append(createUser);
    builder.append(", lastUpdateUser=");
    builder.append(lastUpdateUser);
    builder.append(", filterScope=");
    builder.append(filterScope);
    builder.append(", values=");
    builder.append(values);
    builder.append("]");
    return builder.toString();
  }
}