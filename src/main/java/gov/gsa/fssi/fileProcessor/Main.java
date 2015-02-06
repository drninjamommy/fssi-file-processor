package main.java.gov.gsa.fssi.fileProcessor;

import java.util.ArrayList;

import main.java.gov.gsa.fssi.config.Config;
import main.java.gov.gsa.fssi.files.providers.Provider;
import main.java.gov.gsa.fssi.files.providers.utils.ProvidersBuilder;
import main.java.gov.gsa.fssi.files.schemas.Schema;
import main.java.gov.gsa.fssi.files.schemas.utils.SchemasBuilder;
import main.java.gov.gsa.fssi.files.sourceFiles.SourceFile;
import main.java.gov.gsa.fssi.files.sourceFiles.utils.SourceFileBuilder;
import main.java.gov.gsa.fssi.helpers.FileHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main class for the FSSI File Processor Project.
 * 
 * @author David Larrimore
 * @version 0.1
 */
public class Main {
	static Logger logger = LoggerFactory.getLogger(Main.class);
	static Config config = new Config();	    
	
	public static void main(String[] args) {
	    logger.info("Starting FSSI File Processor");
	    
	    logger.info("Building Providers");
	    ProvidersBuilder providersBuilder = new ProvidersBuilder();
	    ArrayList<Provider> providers = providersBuilder.build();
	    printAllProviders(providers);
	    
	    logger.info("Building Schemas");
	    SchemasBuilder schemasBuilder = new SchemasBuilder();
		ArrayList<Schema> schemas = schemasBuilder.build();
		printAllSchemas(schemas);
		
		for (String fileName : FileHelper.getFilesFromDirectory(config.getProperty(Config.SOURCEFILES_DIRECTORY), ".csv")) {
	    	SourceFileBuilder sourceFileBuilder = new SourceFileBuilder();
	    	SourceFile sourceFile = sourceFileBuilder.build(fileName, schemas, providers);   
	    	if(sourceFile != null && !sourceFile.getStatusLevel().equals(SourceFile.STATUS_ERROR)){
	    		sourceFile.export();
	    	}
		}
		
	    logger.info("Completed FSSI File Processor");	
	}

	/**
	 * Prints all Providers
	 * @param providers an ArrayList of Provider class objects
	 */
	public static void printAllProviders(ArrayList<Provider> providers){
		for(Provider provider: providers){
			provider.print();
		}
	}
	
	/**
	 * Prints all Schemas
	 * @param schemas an ArrayList of Schema class objects
	 */	
	public static void printAllSchemas(ArrayList<Schema> schemas){
		for(Schema schema: schemas){
			schema.printAll();
		}
	}
	
}