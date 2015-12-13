package mo.iguideu.ui.upcoming;

import mo.iguideu.guide.GuideEvent;

public class GuiderUpcomingGuide extends GuideEvent {

	private String  targetDate   = "";
	private String  sessionStart = "";
	private String  sessionEnd   = "";
	
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public String getSessionStart() {
		return sessionStart;
	}
	public void setSessionStart(String sessionStart) {
		this.sessionStart = sessionStart;
	}
	public String getSessionEnd() {
		return sessionEnd;
	}
	public void setSessionEnd(String sessionEnd) {
		this.sessionEnd = sessionEnd;
	}
}
