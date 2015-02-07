package main.java.gov.gsa.fssi.files.sourceFiles.records.datas;

import java.util.ArrayList;

import main.java.gov.gsa.fssi.files.File;
import main.java.gov.gsa.fssi.files.sourceFiles.records.SourceFileRecord;
import main.java.gov.gsa.fssi.files.sourceFiles.records.datas.results.ValidationResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Data {
	static Logger logger = LoggerFactory.getLogger(SourceFileRecord.class);
	private String data = ""; //TODO: turn this into a generic Object
	private Integer headerIndex = 0;
	private int maxErrorLevel = 0;
	private boolean status = true;
	private ArrayList<ValidationResult> validationResults = new ArrayList<ValidationResult>();
	
	
	/**
	 * @return the validationResults
	 */	
	public ArrayList<ValidationResult> getValidationResults() {
		return validationResults;
	}
	/**
	 * @param index
	 * @return the validationResult based upon provided index
	 */
	public ValidationResult getValidationResult(int index) {
		return validationResults.get(index);
	}	
	/**
	 * @param validationResults the validationResults to set
	 */
	public void setValidationResults(ArrayList<ValidationResult> validationResults) {
		for (ValidationResult validationResult : validationResults) {
			this.setMaxErrorLevel(validationResult.getErrorLevel());
			this.setStatus(validationResult.getErrorLevel());			
		}
		this.validationResults = validationResults;
	}
	/**
	 * @param validationResults the validationResults to add
	 */
	public void addValidationResult(ValidationResult validationResult) {
		this.setMaxErrorLevel(validationResult.getErrorLevel());
		this.setStatus(validationResult.getErrorLevel());
		this.validationResults.add(validationResult);
	}	
	/**
	 * @param status
	 * @param errorLevel
	 */
	public void addValidationResult(boolean status, int errorLevel, String rule) {
		this.addValidationResult(new ValidationResult(status, errorLevel, rule));
	}		
	/**
	 * @return the validatorStatus
	 */
	public boolean getStatus() {
		return status;
	}
	/**
	 * This sets the overall Pass/Fail status of the Data object. Once it is fail (false), it cannot change back
	 * @param validatorStatus the validatorStatus to set
	 */
	public void setStatus(int errorLevel) {
		if(this.getStatus() == true && errorLevel > 1) this.status = false;
	}

	public Data() {
		super();
	}
	
	public Data(Integer headerIndex) {
		super();
		this.headerIndex = headerIndex;
	}
	
	public Data(Integer headerIndex, String data) {
		super();
		this.headerIndex = headerIndex;
		this.data = data;
	}
	/**
	 * @return
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return
	 */
	public Integer getHeaderIndex() {
		return headerIndex;
	}
	/**
	 * @param headerIndex
	 */
	public void setHeaderIndex(Integer headerIndex) {
		this.headerIndex = headerIndex;
	}
	/**
	 * @return
	 */
	public int getMaxErrorLevel() {
		return maxErrorLevel;
	}
	/**
	 * @return
	 */
	public String getErrorLevelName(int statusLevel) {
		String name = null;
		if(statusLevel <= 3){
			switch (statusLevel){
			case 0:
				name = File.STATUS_PASS;
				break;
			case 1:
				name = File.STATUS_WARNING;
				break;
			case 2:
				name = File.STATUS_ERROR;
				break;
			case 3:
				name = File.STATUS_FATAL;
				break;
			default:
				break;
			}	
		}
		return name;
	}		
	/**
	 * @param status
	 */
	public void setMaxErrorLevel(int statusLevel) {
		if(statusLevel > this.maxErrorLevel && statusLevel <= 3) this.maxErrorLevel = statusLevel;
	}
	public void print(){
			logger.debug(" Data: {} Max Status: {}, ", this.getData(), this.getMaxErrorLevel());
	}
	
}
