package main.java.gov.gsa.fssi.files.schemas.schemafields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.gov.gsa.fssi.config.Config;
import main.java.gov.gsa.fssi.files.File;
import main.java.gov.gsa.fssi.files.schemas.schemafields.fieldconstraints.FieldConstraint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaField {
	private static final Logger logger = LoggerFactory
			.getLogger(SchemaField.class);

	private int headerIndex = -1;
	private String name = null;
	private String title = null;
	private String type = null;
	private int typeErrorLevel = 3;	//Defaulted to Fatal
	// List of Options which come from the format attributes
	public static final String OPTION_EFFECTIVEDATE = "effectiveDate";
	public static final String OPTION_LEVEL = "level";
	
	
	private String format = null;
	private HashMap<String, String> typeOptions = new HashMap<String, String>();
	private String description = null;
	private List<FieldConstraint> constraints = new ArrayList<FieldConstraint>();
	private List<String> alias = new ArrayList<String>();

	/**
	 * a string (of arbitrary length)
	 */
	public static final String TYPE_STRING = "string";

	/**
	 * a number including floating point numbers.
	 */
	public static final String TYPE_NUMBER = "number";

	/**
	 * an integer.
	 */
	public static final String TYPE_INTEGER = "integer";

	/**
	 * a date. This MUST be in ISO6801 format YYYY-MM-DD or, if not, a format
	 * field must be provided describing the structure.
	 */
	public static final String TYPE_DATE = "date";

	/**
	 * a time without a date
	 */
	public static final String TYPE_TIME = "time";

	/**
	 * a date-time. This MUST be in ISO 8601 format of YYYY-MM-DDThh:mm:ssZ in
	 * UTC time or, if not, a format field must be provided.
	 */
	public static final String TYPE_DATETIME = "datetime";// a time without a

	// date
	/**
	 * a boolean value (1/0, true/false).
	 */
	public static final String TYPE_BOOLEAN = "boolean";

	/**
	 * has one of the following structures: "longitude, latitude
	 */
	public static final String TYPE_GEOPOINT = "geopoint";

	/**
	 * an array in "value,value,value" format
	 */
	public static final String TYPE_ARRAY = "array";
	/**
	 * value of field may be any type
	 */
	public static final String TYPE_ANY = "any";
	
	/**
	 * @param alias
	 */
	public void addAlias(String alias) {
		this.alias.add(alias);
	}

	/**
	 * @param constraint
	 */
	public void addConstraint(FieldConstraint constraint) {
		this.constraints.add(constraint);
	}

	public void addTypeOption(String key, String value) {
		this.typeOptions.put(key, value);
	}	
	
	public HashMap<String, String> getTypeOptions() {
		return typeOptions;
	}

	public String getTypeOptionValue(String key) {
		return typeOptions.get(key);
	}
	
	public int getTypeErrorLevel() {
		return typeErrorLevel;
	}
	
	public void setTypeErrorLevel(int typeErrorLevel) {
		this.typeErrorLevel = typeErrorLevel;
	}

	public void setTypeErrorLevel(String levelName) {
		this.typeErrorLevel = getTypeErrorLevel(levelName);
	}

	public static int getTypeErrorLevel(String levelName) {
		if (levelName.equalsIgnoreCase(File.STATUS_FATAL)) {
			return 3;
		} else if (levelName.equalsIgnoreCase(File.STATUS_ERROR)) {
			return 2;
		} else if (levelName.equalsIgnoreCase(File.STATUS_WARNING)) {
			return 1;
		}
		return 0;

	}	
	
	
	/**
	 * @return
	 */
	private List<String> buildTypeArray() {
		List<String> arrayList = new ArrayList<String>();
		arrayList.add(TYPE_STRING);
		arrayList.add(TYPE_NUMBER);
		arrayList.add(TYPE_INTEGER);
		arrayList.add(TYPE_DATE);
		arrayList.add(TYPE_TIME);
		arrayList.add(TYPE_DATETIME);
		arrayList.add(TYPE_BOOLEAN);
		arrayList.add(TYPE_GEOPOINT);
		arrayList.add(TYPE_ARRAY);
		arrayList.add(TYPE_ANY);
		return arrayList;
	}

	/**
	 * @return the alias
	 */
	public List<String> getAlias() {
		return alias;
	}

	/**
	 * @return the constraints
	 */
	public List<FieldConstraint> getConstraints() {
		return constraints;
	}

	// public static String TYPE_BINARY = "binary"; //a boolean value (1/0,
	// true/false).
	// public static String TYPE_OBJECT = "object"; //(alias xml) an XML-encoded
	// object

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * This constructor class takes a file name and uses it to initialize the
	 * basic elements of a SourceFile
	 * 
	 * @param fileName
	 *            - This should be in name.ext format.
	 */
	public int getHeaderIndex() {
		return headerIndex;
	}


	/**
	 * @return the name
	 */

	// TODO:Add the following Types
	// string � http://www.w3.org/2001/XMLSchema#string
	// integer � http://www.w3.org/2001/XMLSchema#int
	// float � http://www.w3.org/2001/XMLSchema#float
	// double � http://www.w3.org/2001/XMLSchema#double
	// URL � http://www.w3.org/2001/XMLSchema#anyURI
	// boolean � http://www.w3.org/2001/XMLSchema#boolean
	// non-positive integer �
	// http://www.w3.org/2001/XMLSchema#nonPositiveInteger
	// positive integer � http://www.w3.org/2001/XMLSchema#positiveInteger
	// non-negative integer �
	// http://www.w3.org/2001/XMLSchema#nonNegativeInteger
	// negative integer � http://www.w3.org/2001/XMLSchema#negativeInteger
	// date � http://www.w3.org/2001/XMLSchema#date
	// date & time � http://www.w3.org/2001/XMLSchema#dateTime
	// year � http://www.w3.org/2001/XMLSchema#gYear
	// year & month � http://www.w3.org/2001/XMLSchema#gYearMonth
	// time � http://www.w3.org/2001/XMLSchema#time

	
	public SchemaField(){
		
	}

	/**
	 * Copy constructor
	 * @param schemaField
	 */
	public SchemaField(SchemaField schemaField){
		if(schemaField.alias != null){
			for(String newAlias:schemaField.alias) this.alias.add(new String(newAlias));
		}
		
		if(schemaField.constraints != null){
			for(FieldConstraint constraint:schemaField.constraints) this.constraints.add(new FieldConstraint(constraint));			
		}
		
		if(schemaField.description != null) this.description = new String(schemaField.description);
		if(schemaField.format != null) this.format = new String(schemaField.format);
		
		this.typeErrorLevel = schemaField.typeErrorLevel;
		
		if(schemaField.name != null) this.name = new String(schemaField.name);
		if(schemaField.title != null) this.title = new String(schemaField.title);
		if(schemaField.type != null) this.type = new String(schemaField.type);
	}
	
	public String getName() {
		return name;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}


	/**
	 * Validates that a string is a valid SchemaFieldType
	 * 
	 * @param string
	 *            to compare
	 * @return true or false
	 */
	public boolean isValidType(String e) {
		List<String> arrayList = buildTypeArray();
		return (arrayList.contains(e) ? true : false);
	}

	/**
	 * Print Field
	 */
	public void print() {
		logger.debug(
				"     Field Name:'{}' HeaderIndex: '{}' Title:'{}' Type:'{}' Description:'{}' Format:'[{}] {}' Alias:{}}",
				this.getName(), this.getHeaderIndex(), this.getTitle(),
				this.getType(), this.getDescription(), File.getErrorLevelInitial(this.getTypeErrorLevel()), this.getFormat(),
				this.getAlias());
		printConstraints();
	}

	private void printConstraints() {
		for (FieldConstraint constraint : this.getConstraints()) {
			constraint.print();
		}
	}

	public void removeAlias(String alias) {
		int index = this.alias.indexOf(alias);
		this.alias.remove(index);
	}

	/**
	 * @param list
	 *            the alias to set
	 */
	public void setAlias(List<String> list) {
		this.alias = list;
	}

	/**
	 * @param constraintMap
	 *            the constraints to set
	 */
	public void setConstraints(List<FieldConstraint> constraintMap) {
		this.constraints = constraintMap;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	public void setHeaderIndex(int headerIndex) {
		this.headerIndex = headerIndex;
	}


	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	/**
	 * Checks to see if string matches the fields name or any of its aliass
	 * @param fieldName
	 * @return true or false
	 */
	public boolean isField(String fieldName){
		if(fieldName.equalsIgnoreCase(getName())) return true;
		for(String aliass:getAlias()){
			if(fieldName.equalsIgnoreCase(aliass)) return true;
		}
		return false;
	}
}
