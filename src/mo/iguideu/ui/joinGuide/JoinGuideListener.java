package mo.iguideu.ui.joinGuide;


public interface JoinGuideListener {
	public void onDateChoosed(String dateString);
	public void onGuestCountConfirm(int guestCount);
	public void onGuideSessionConfirm(SessionJoinStatus guideSession);
	public void onReadyToJoinGuide();
}
