package main.java.gov.gsa.fssi.helpers.mockdata;

import java.util.List;

import main.java.gov.gsa.fssi.files.schemas.Schema;
import main.java.gov.gsa.fssi.files.schemas.schemafields.SchemaField;

public class MockSchema {

	public static Schema make() {
		Schema schema = new Schema();
		return schema;
	}

	public static Schema make(String name) {
		Schema schema = new Schema();
		schema.setName(name);
		return schema;
	}

	public static Schema make(String name, List<SchemaField> schemaFields) {
		Schema schema = new Schema();
		schema.setName(name);
		for (SchemaField schemaField : schemaFields) {
			schema.addField(schemaField);
		}
		return schema;
	}

	public static Schema make(String name, SchemaField schemaField) {
		Schema schema = new Schema();
		schema.setName(name);
		schema.addField(schemaField);
		return schema;
	}
}
