package gov.gsa.fssi.fileprocessor;

import gov.gsa.fssi.files.schemas.Schema;
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


	public static Schema loadSchema(String fileName) {
		Document doc = null;
		Schema newSchema = new Schema(fileName);	
		try {
			File fXmlFile = new File(config.getProperty(Config.SCHEMAS_DIRECTORY) + fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
		} catch (Exception e) {
			logger.error("Received Exception error '{}' while processing file {}", e.getMessage(), fileName);	
			return null;
			//e.printStackTrace();
	    }
		
		if(doc != null){
			//optional, but recommended
			doc.getDocumentElement().normalize();
		 
			//We assume their is only 1 schema in each file.
			Node schemaNode = doc.getFirstChild();
			Element schemaElement = (Element) schemaNode;
		    logger.info("Attempting to load schema '{}' in file '{}'", schemaElement.getElementsByTagName("name").item(0).getTextContent(), fileName);
			
		    newSchema.setName(schemaElement.getElementsByTagName("name").item(0).getTextContent());
			newSchema.setProviderName(schemaElement.getElementsByTagName("provider").item(0).getTextContent());
			newSchema.setVersion(schemaElement.getElementsByTagName("version").item(0).getTextContent());
			newSchema.setFields(initializeFields(doc.getElementsByTagName("field")));
			
			if(newSchema.getStatus().equals(Schema.STATUS_ERROR)){
				logger.error("Could not load Schema '{}' in file '{}' as it is in error status", newSchema.getName(), fileName);
				return null;
			}
			
			logger.info("successfully loaded Schema '{}' from file '{}'", newSchema.getName(), fileName);
			newSchema.setStatus(Schema.STATUS_LOADED);
			return newSchema;
		}
		logger.error("No document found in file '{}'. Unable to load any schema", fileName);
		newSchema.setStatus(Schema.STATUS_ERROR);
		return newSchema;
	}	


		public static ArrayList<SchemaField> initializeFields(NodeList fieldNodes) {
			ArrayList<SchemaField> fields = new ArrayList<SchemaField>();
			for (int temp = 0; temp < fieldNodes.getLength(); temp++) {
				SchemaField field = initializeField(fieldNodes.item(temp));
				fields.add(field);
				logger.info("succesfully added field '{}' to the schema.", field.getName());
			}
			return fields;		
		}

		
		/**
		 * @param node
		 * @return
		 */
	public static SchemaField initializeField(Node node) {
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
									processConstraints(field, constraintList);
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
		private static void processConstraints(SchemaField field, NodeList constraintList) throws DOMException {
			Node currentNode;
			for (int i = 0; i < constraintList.getLength(); i++) {
				currentNode = constraintList.item(i);
				processConstraint(field, currentNode);
			}
		}


		/**
		 * @param field
		 * @param currentNode
		 * @throws DOMException
		 */
		private static void processConstraint(SchemaField field, Node currentNode) throws DOMException {
			FieldConstraint newConstraint = new FieldConstraint();
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				logger.info("Adding Constraint {} - {}", currentNode.getNodeName().trim(), currentNode.getTextContent().trim());		
				newConstraint.setConstraintType(currentNode.getNodeName().trim());
				newConstraint.setValue(currentNode.getTextContent().trim());									
				
				HashMap<String,String> attributeMap = XmlHelper.convertXmlAttributeToHashMap(currentNode.getAttributes());
				Iterator<?> optionsIterator = attributeMap.entrySet().iterator();
				
				while (optionsIterator.hasNext()) {
					Map.Entry<String, String> optionsPair = (Map.Entry)optionsIterator.next();
						newConstraint.addOption(optionsPair.getKey(),optionsPair.getValue()) ;
						logger.info("Adding Attribute {} - {} to Constraint {}", optionsPair.getKey(), optionsPair.getValue(), currentNode.getNodeName());		
				}
				
				field.addConstraint(newConstraint);								
			}
		}

		
		public static void printAllSchemas(ArrayList<Schema> schemas){
			for(Schema schema: schemas){
				schema.printAll();
			}
		}
}
