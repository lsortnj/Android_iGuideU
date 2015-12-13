package mo.iguideu.ui.joinGuide;

import java.util.ArrayList;
import java.util.List;

public class JoinStatus {

	private String targetDate  = "";
	private int    remainCount = 0;
	private List<SessionJoinStatus> sessionStatues = new ArrayList<SessionJoinStatus>();
	
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public List<SessionJoinStatus> getSessionStatues() {
		return sessionStatues;
	}
	public void setSessionStatues(List<SessionJoinStatus> sessionStatues) {
		this.sessionStatues = sessionStatues;
	}
	public int getRemainCount() {
		return remainCount;
	}
	public void setRemainCount(int remainCount) {
		this.remainCount = remainCount;
	}
	
}
