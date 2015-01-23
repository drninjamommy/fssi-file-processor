package gov.gsa.fssi.files.sourceFiles.utils.validators;

import gov.gsa.fssi.config.Config;
import gov.gsa.fssi.files.LoaderStatus;
import gov.gsa.fssi.files.schemas.schemaFields.SchemaField;
import gov.gsa.fssi.files.schemas.schemaFields.fieldConstraints.FieldConstraint;
import gov.gsa.fssi.files.sourceFiles.SourceFile;
import gov.gsa.fssi.files.sourceFiles.records.SourceFileRecord;
import gov.gsa.fssi.files.sourceFiles.records.datas.Data;
import gov.gsa.fssi.files.sourceFiles.utils.validators.strategies.MaxLengthDataValidationStrategy;
import gov.gsa.fssi.files.sourceFiles.utils.validators.strategies.MaximumDataValidationStrategy;
import gov.gsa.fssi.files.sourceFiles.utils.validators.strategies.MinLengthDataValidationStrategy;
import gov.gsa.fssi.files.sourceFiles.utils.validators.strategies.MinimumDataValidationStrategy;
import gov.gsa.fssi.files.sourceFiles.utils.validators.strategies.PatternDataValidationStrategy;
import gov.gsa.fssi.files.sourceFiles.utils.validators.strategies.RequiredDataValidationStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The purpose of this class is to handle the automated processing of all source files.
 *
 * @author davidlarrimore
 *
 */
public class SourceFileValidator{
	static Config config = new Config();	    
	static Logger logger = LoggerFactory.getLogger(SourceFileValidator.class);
	
	
	public void validate(SourceFile sourceFile){
		logger.info("Starting sourceFile validation for file '{}'", sourceFile.getFileName());
		
		if(sourceFile.getSchema() == null){
			logger.error("Cannot validate file '{}', no Schema", sourceFile.getFileName());	
		}else if(sourceFile.getLoaderStatus().equals(LoaderStatus.ERROR)){
			logger.error("file '{}', had a load error, cannot validate", sourceFile.getFileName());
		}else{
			for(SourceFileRecord sourceFileRecord: sourceFile.getRecords()){
				for(SchemaField field:sourceFile.getSchema().getFields()){
					Data data = sourceFileRecord.getDataByHeaderIndex(field.getHeaderIndex());
					if(data != null){
						//logger.debug("HeaderIndex - '{}'", field.getHeaderIndex());
						//TODO: Validate Type
						//context.validateConstraint(field, constraint, data);
						//TODO: Validate Format
						for(FieldConstraint constraint:field.getConstraints()){
							DataValidationContext context = new DataValidationContext();
							context.validateConstraint(field, constraint, data); //Validate Constraint
							if(data.getStatus().equals(FieldConstraint.LEVEL_ERROR) ||data.getStatus().equals(FieldConstraint.LEVEL_WARNING)){
								logger.debug("Row {} - Field '{}' validation {}: '{}' = {}, Value = '{}'", sourceFileRecord.getRowIndex(), field.getName(), constraint.getLevel().toUpperCase(), constraint.getType(), constraint.getValue(), data.getData());	
							}
						}
					}
				}
			}
		}
	}
	
}