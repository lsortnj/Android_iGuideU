package mo.iguideu.ui.joinGuide;

import mo.iguideu.guide.GuideSession;

public class SessionJoinStatus extends GuideSession {

	private int remainCount = 0;

	public int getRemainCount() {
		return remainCount;
	}
	public void setRemainCount(int remainCount) {
		this.remainCount = remainCount;
	}
}
