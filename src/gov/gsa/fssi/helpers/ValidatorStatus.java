package gov.gsa.fssi.helpers;

public class ValidatorStatus {
	public static final String ERROR = "error";
	public static final String WARNING = "warning";
	public static final String PASS = "pass";	
	
	private String level;
	private String statusMessage;
	
	
	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	public ValidatorStatus(){
		
	}
	
	
}