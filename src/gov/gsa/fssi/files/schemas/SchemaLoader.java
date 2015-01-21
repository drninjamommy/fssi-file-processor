package gov.gsa.fssi.files.schemas;

import gov.gsa.fssi.config.Config;
import gov.gsa.fssi.files.LoaderStatus;
import gov.gsa.fssi.files.schemas.schemaFields.SchemaField;
import gov.gsa.fssi.files.schemas.schemaFields.fieldConstraints.FieldConstraint;
import gov.gsa.fssi.helpers.XmlHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SchemaLoader {
	static Logger logger = LoggerFactory.getLogger(SchemaLoader.class);
	static Config config = new Config();	
	private String fileName = null;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public Schema load(String fileName) {
		this.setFileName(fileName);
		return this.load();
	}	

	
	/**
	 *
	 * @return Schema loaded from fileName in schemas_directory
	 */
	public Schema load() {
		Document doc = null;
		Schema schema = new Schema(this.getFileName());	
		if(this.getFileName() != null){
			try {
				File fXmlFile = new File(config.getProperty(Config.SCHEMAS_DIRECTORY) + this.getFileName());
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				doc = dBuilder.parse(fXmlFile);
			} catch (Exception e) {
				logger.error("Received Exception error '{}' while processing file {}", e.getMessage(), this.getFileName());	
				return null;
				//e.printStackTrace();
		    }
			
			if(doc != null){
				//optional, but recommended
				doc.getDocumentElement().normalize();
			 
				//We assume their is only 1 schema in each file.
				Node schemaNode = doc.getFirstChild();
				Element schemaElement = (Element) schemaNode;
			    logger.info("Attempting to load schema '{}' in file '{}'", schemaElement.getElementsByTagName("name").item(0).getTextContent(), this.getFileName());
				
			    schema.setName(schemaElement.getElementsByTagName("name").item(0).getTextContent());
				schema.setProviderName(schemaElement.getElementsByTagName("provider").item(0).getTextContent());
				schema.setVersion(schemaElement.getElementsByTagName("version").item(0).getTextContent());
				schema.setFields(loadFields(doc.getElementsByTagName("field")));
				
				if(schema.getLoaderStatusLevel().equals(LoaderStatus.ERROR)){
					logger.error("Could not load Schema '{}' in file '{}' as it is in error status", schema.getName(), this.getFileName());
					return null;
				}
				
				logger.info("successfully loaded Schema '{}' from file '{}'", schema.getName(), this.getFileName());
				schema.setLoaderStatusLevel(LoaderStatus.LOADED);
				return schema;
			}
			logger.error("No document found in file '{}'. Unable to load any schema", this.getFileName());
			schema.setLoaderStatusLevel(LoaderStatus.ERROR);
			return schema;
			
		}else{
			logger.error("Could not build Schema, no fileName was set");
			return null;
		}
	}		
	
	

	public ArrayList<SchemaField> loadFields(NodeList fieldNodes) {
		ArrayList<SchemaField> fields = new ArrayList<SchemaField>();
		for (int temp = 0; temp < fieldNodes.getLength(); temp++) {
			SchemaField field = loadField(fieldNodes.item(temp));
			fields.add(field);
			logger.info("succesfully added field '{}' to the schema.", field.getName());
		}
		return fields;		
	}

		
	/**
	 * @param node
	 * @return
	 */
	public SchemaField loadField(Node node) {
		SchemaField field = new SchemaField();
			
		if (node.getNodeType() == Node.ELEMENT_NODE) {
				
				NodeList nodeList = node.getChildNodes();
				for (int j = 0; j < nodeList.getLength(); j++){
					Node currentNode = nodeList.item(j);
					if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName() != null){
						//logger.debug("{} - {}", currentNode.getNodeName(), currentNode.getTextContent());	
						if(currentNode.getNodeName().equals("name")){
							field.setName(currentNode.getTextContent());					
						}else if (currentNode.getNodeName().equals("description")){
							field.setDescription(currentNode.getTextContent());	
						}else if (currentNode.getNodeName().equals("title")){
							field.setTitle(currentNode.getTextContent());	
						}else if (currentNode.getNodeName().equals("format")){
							field.setFormat(currentNode.getTextContent());	
						}else if (currentNode.getNodeName().equals("type")){
							field.setType(currentNode.getTextContent());	
						}else if(currentNode.getNodeName().equals("constraints")){
							logger.info("Processing Constraints");
							NodeList constraintList = currentNode.getChildNodes();
							if(constraintList != null){
								try {
									ArrayList<FieldConstraint> constraints = loadConstraints(constraintList);
									if(constraints == null){
										logger.error("No constraints loaded");
									}else{
										field.setConstraints(constraints);	
									}
								} catch (DOMException e) {
									logger.error("Received DOMException '{}'",e.getMessage());
									//e.printStackTrace();
								}	
							}else{
								logger.info("Did not find any constraints");
							}			
							logger.info("Completed processing Constraints");
						}else if (currentNode.getNodeName().equals("alias")){
							field.addAlias(currentNode.getTextContent().trim().toUpperCase());		
						}
					}
				}
				logger.info("Processing field '{}'", field.getName());
			}			
		return field;	
	}


		/**
		 * @param field
		 * @param constraintList
		 * @throws DOMException
		 */
		private ArrayList<FieldConstraint> loadConstraints(NodeList constraintList) throws DOMException {
			Node currentNode;
			ArrayList<FieldConstraint> constraints = new ArrayList<FieldConstraint>();
			for (int i = 0; i < constraintList.getLength(); i++) {
				currentNode = constraintList.item(i);
				FieldConstraint fieldConstraint = loadConstraint(currentNode);
				if(fieldConstraint == null){
				}else{
					constraints.add(fieldConstraint);	
				}
			}
			return constraints;
		}


		/**
		 * @param field
		 * @param currentNode
		 * @throws DOMException
		 */
		private FieldConstraint loadConstraint(Node currentNode) throws DOMException {
			FieldConstraint newConstraint = new FieldConstraint();
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				logger.info("Adding Constraint {} - {}", currentNode.getNodeName().trim(), currentNode.getTextContent().trim());		
				newConstraint.setType(currentNode.getNodeName().trim());
				newConstraint.setValue(currentNode.getTextContent().trim());									
				
				HashMap<String,String> attributeMap = XmlHelper.convertXmlAttributeToHashMap(currentNode.getAttributes());
				Iterator<?> optionsIterator = attributeMap.entrySet().iterator();
				
				while (optionsIterator.hasNext()) {
					Map.Entry<String, String> optionsPair = (Map.Entry)optionsIterator.next();
						newConstraint.addOption(optionsPair.getKey(),optionsPair.getValue()) ;
						logger.info("Adding Attribute {} - {} to Constraint {}", optionsPair.getKey(), optionsPair.getValue(), currentNode.getNodeName());		
				}
				
				return newConstraint;								
			}
			return null;
		}

		
		public static void printAllSchemas(ArrayList<Schema> schemas){
			for(Schema schema: schemas){
				schema.printAll();
			}
		}
}
