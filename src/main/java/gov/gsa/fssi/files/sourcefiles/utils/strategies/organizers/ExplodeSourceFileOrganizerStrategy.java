package main.java.gov.gsa.fssi.files.sourcefiles.utils.strategies.organizers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import main.java.gov.gsa.fssi.files.schemas.schemafields.SchemaField;
import main.java.gov.gsa.fssi.files.sourcefiles.SourceFile;
import main.java.gov.gsa.fssi.files.sourcefiles.records.SourceFileRecord;
import main.java.gov.gsa.fssi.files.sourcefiles.records.datas.Data;
import main.java.gov.gsa.fssi.files.sourcefiles.utils.strategies.SourceFileOrganizerStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class loads a schema from an XML file
 * 
 * @author davidlarrimore
 *
 */
public class ExplodeSourceFileOrganizerStrategy implements
		SourceFileOrganizerStrategy {
	private static final Logger logger = LoggerFactory
			.getLogger(ExplodeSourceFileOrganizerStrategy.class);

	/**
	 * This method maps a sourceFile to its schema and then conforms the
	 * file/data to the schema format We delete any data that is no longer
	 * necessary
	 */
	@Override
	public void organize(SourceFile sourceFile) {
		logger.info("Exploding sourceFile '{}' to Schema '{}'",
				sourceFile.getFileName(), sourceFile.getSchema().getName());
		Map<Integer, String> newHeader = new HashMap<Integer, String>();
		// This is our count to determine location of each header
		Integer headerCounter = 0;

		// The headerTranslationMap object translates the old headerIndex, to
		// the new header index.
		// Key = Old Index, Value = New Index
		Map<Integer, Integer> headerTranslationMap = new HashMap<Integer, Integer>();

		// First, lets add all of the fields from our Schema, they always go
		// first
		for (SchemaField field : sourceFile.getSchema().getFields()) {
			String fieldName = field.getName();
			/*
			 *  based upon prior processes, their is probably a match, so lets check!
			 */
			if (field.getHeaderIndex() >= 0) {
				Iterator<?> currentHeaderIterator = sourceFile.getSourceHeaders().entrySet().iterator();
				if(sourceFile.getSourceHeaders().containsKey(field.getHeaderIndex())){
					headerTranslationMap.put(field.getHeaderIndex(),headerCounter);
					fieldName = sourceFile.getSourceHeaderName(field.getHeaderIndex());
				}
			}

			field.setHeaderIndex(headerCounter);	
			if (logger.isDebugEnabled()) logger.debug("Adding '{} - {}' to newHeader", headerCounter, fieldName);
			newHeader.put(headerCounter, fieldName);
			headerCounter++;
		}

		// Next lets add the stragglers.....
		Iterator<?> currentHeaderIterator = sourceFile.getSourceHeaders().entrySet().iterator();
		while (currentHeaderIterator.hasNext()) {
			Map.Entry<Integer, String> currentHeaderIteratorPairs = (Map.Entry) currentHeaderIterator
					.next();
			boolean foundColumn = false;
			Iterator<?> headerTranslationMapIterator = headerTranslationMap
					.entrySet().iterator();
			// We need to loop through the translation map and see if we have
			// already addressed this column
			while (headerTranslationMapIterator.hasNext()) {
				Map.Entry<Integer, String> headerTranslationMapIteratorPairs = (Map.Entry) headerTranslationMapIterator
						.next();
				if (currentHeaderIteratorPairs.getKey().equals(
						headerTranslationMapIteratorPairs.getKey())) {
					foundColumn = true;
				}
			}
			if (!foundColumn) {
				// Could not find column, lets add it to index!
				logger.info(
						"Source field '{} - {}' was not in Schema, but thats ok!, adding to newHeader index '{}'",
						currentHeaderIteratorPairs.getValue().toString()
								.toUpperCase(),
						currentHeaderIteratorPairs.getKey(), headerCounter);
				headerTranslationMap.put(currentHeaderIteratorPairs.getKey(),
						headerCounter);
				newHeader.put(headerCounter,
						currentHeaderIteratorPairs.getValue());
				headerCounter++;
			}
		}

		Map<Integer, String> fieldMap = new HashMap<Integer, String>();
		for(SchemaField fieldTemp : sourceFile.getSchema().getFields()){
			fieldMap.put(fieldTemp.getHeaderIndex(), fieldTemp.getName());
		}
		// Lets print the new headers
		sourceFile.setSourceHeaders(newHeader);		
		logger.info("Old Header:{}", sourceFile.getSourceHeaders());		
		logger.debug("New Header: {}", sourceFile.getSourceHeaders());
		logger.info("New Field List:{}", fieldMap);

		// now we can process the data by restacking data and filling in the
		// blanks
		for (SourceFileRecord sourceFileRecord : sourceFile.getRecords()) {
			// Restacking
			for (Data data : sourceFileRecord.getDatas()) {
				data.setHeaderIndex(headerTranslationMap.get(data.getHeaderIndex()));
			}

			// Now we fill in the blanks
			Iterator<?> sourceFileHeaderIterator2 = sourceFile.getSourceHeaders().entrySet().iterator();
			while (sourceFileHeaderIterator2.hasNext()) {
				Map.Entry<Integer, String> newHeaderPairs = (Map.Entry) sourceFileHeaderIterator2.next();
				Data data = sourceFileRecord.getDataByHeaderIndex(newHeaderPairs.getKey());
				if (data == null) {sourceFileRecord.addData(new Data(newHeaderPairs.getKey(),""));
				}
			}
		}
	}

}
