package gov.gsa.fssi.files.providers.utils.contexts;

import java.util.ArrayList;

import gov.gsa.fssi.files.providers.Provider;
import gov.gsa.fssi.files.providers.utils.strategies.ProviderLoaderStrategy;

/**
 * Context for loading Providers
 * 
 * @author DavidKLarrimore
 * @see gov.gsa.fssi.files.providers.strategies.ProviderLoaderStrategy
 */
public class ProviderLoaderContext {
	private ProviderLoaderStrategy strategy;   
	/**
	 * @param strategy String strategy to set
	 */	   
	public void setProviderLoaderStrategy(ProviderLoaderStrategy strategy){
	       this.strategy = strategy;  
	}
	/**
	 * @return current strategy
	 */
	   public ProviderLoaderStrategy getProviderLoaderStrategy(){
	       return this.strategy;
	   }
		/**
		 *execute strategy
		 * @param fileName String fileName
		 * @param providers ArrayList<Provider> of Provider
		 */
		public void load(String fileName, ArrayList<Provider> providers) {
			strategy.load(fileName, providers); //Validate Constraint
	   }
}
