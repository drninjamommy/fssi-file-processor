package gov.gsa.fssi.files.sourceFiles.utils.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gsa.fssi.config.Config;
import gov.gsa.fssi.files.schemas.schemaFields.SchemaField;
import gov.gsa.fssi.files.sourceFiles.records.datas.Data;

/**
 * @author DavidKLarrimore
 *
 */
public interface TypeValidationStrategy {
	static Logger logger = LoggerFactory.getLogger(TypeValidationStrategy.class);
	static Config config = new Config();	
	public void validate(SchemaField field, Data data);
	public boolean isValid(SchemaField field, Data data);
}
