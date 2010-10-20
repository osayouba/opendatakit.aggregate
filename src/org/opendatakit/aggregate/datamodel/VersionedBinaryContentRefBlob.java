/**
 * Copyright (C) 2010 University of Washington
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.aggregate.datamodel;

import org.opendatakit.common.persistence.CommonFieldsBase;
import org.opendatakit.common.persistence.DataField;

/**
 * Binary content for a given field in a form is held in a set of tables
 * {@link BinaryContent}, {@link VersionedBinaryContent}, 
 * {@link VersionedBinaryContentRefBlob} and {@link RefBlob} for
 * each instance data field. The VersionedBinaryContentRefBlob table links
 * a particular VersionedBinaryContent with the specific sequence of the
 * blobs needed to reconstruct that binary content.
 * <p>
 * The handling of parts supports massive binary objects that are too big
 * to store as a single blob.  
 * <p>
 * Versioning is unique to binary objects as it is likely that updates to 
 * the media associated with a form will occur, and that the xform 
 * definition itself my change to support revisions to the text, ordering
 * or additional language translations. 
 * <p>
 * The intent is that this is a write-once record with version history.
 * Version is recorded as a UUID (URI) in the {@link BinaryContent} and 
 * {@link VersionedBinaryContent} tables.  VersionedBinaryContent records
 * and BinaryContent records are never destroyed.  VersionedBinaryContent
 * records are never updated, but Binary Content records are.
 * 
 * @author mitchellsundt@gmail.com
 *
 */
public final class VersionedBinaryContentRefBlob extends CommonFieldsBase {

	private static final DataField PART = new DataField("PART",DataField.DataType.INTEGER, false);
	
	public final DataField part;
	
	public VersionedBinaryContentRefBlob(String databaseSchema, String tableName) {
		super(databaseSchema, tableName, BaseType.DYNAMIC_ASSOCIATION);
		fieldList.add(part = new DataField(PART));
	}

	/**
	 * Copy constructor for use by {@link #getEmptyRow(Class)}   
	 * This does not populate any fields related to the values of this row. 
	 *
	 * @param d
	 */
	public VersionedBinaryContentRefBlob(VersionedBinaryContentRefBlob ref) {
		super(ref);
		part = ref.part;
	}

	public Long getPart() {
		return getLongField(part);
	}
	
	public void setPart(Long value) {
		setLongField(part,value);
	}
}
