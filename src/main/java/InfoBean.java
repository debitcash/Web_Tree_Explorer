import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/* 
 * InfoBean.java: Defines a CDI bean that stores information needed for LogicBean to function, 
 * avoiding unnecessary method triggers in LogicBean by JSF. Contains setters and getters for use in JSF.
 * 
 * Author: GitHub @debitcash
 * 
 */

@Named("infoBean")
@SessionScoped
public class InfoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String infoHomeLink;
    private String infoLinkLimit;
    private String infoRowLimit;
    private String progressMessage = "";
    private float progress;
    
	public String getInfoHomeLink() {
		return infoHomeLink;
	}
	
	public void setInfoHomeLink(String infoHomeLink) {
		this.infoHomeLink = infoHomeLink;
	}
	
	public String getInfoLinkLimit() {
		return infoLinkLimit;
	}
	
	public void setInfoLinkLimit(String infoLinkLimit) {
		 this.infoLinkLimit = infoLinkLimit;
	}
	
	public String getInfoRowLimit() {
		return infoRowLimit;
	}
	
	public void setInfoRowLimit(String infoRowLimit) {
		this.infoRowLimit = infoRowLimit;
	}
	
	public String redirect()
	{
		return "analysis.xhtml?faces-redirect=true";
	}
	
	public float getProgress() {
		return progress;
	}
	
	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	public String getProgressMessage() {
		return progressMessage;
	}
	
	public void setProgressMessage(String progressMessage) {
		this.progressMessage = progressMessage;
	}
	
	public void updateMessage() {
		if (progress > 0) {
			progressMessage = "Progress: " + String.format("%.2f", progress) + "%";
		}
	}

}
