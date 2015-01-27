package gov.gsa.fssi.files.sourceFiles.utils.contexts;

import gov.gsa.fssi.files.sourceFiles.SourceFile;
import gov.gsa.fssi.files.sourceFiles.utils.strategies.SourceFileExporterStrategy;

public class SourceFileExporterContext {
	   private SourceFileExporterStrategy strategy;   

	   //this can be set at runtime by the application preferences
	   public void setSourceFileExporterStrategy(SourceFileExporterStrategy strategy){
	       this.strategy = strategy;  
	   }

	   //this can be set at runtime by the application preferences
	   public SourceFileExporterStrategy getSourceFileExporterStrategy(){
	       return this.strategy;
	   }
	   
	  //use the strategy
		/**
		 * @param field
		 * @param constraint
		 * @param data
		 */
		public void export(SourceFile sourceFile) {
			strategy.export(sourceFile);
	   }
}