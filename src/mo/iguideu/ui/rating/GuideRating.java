package mo.iguideu.ui.rating;

public class GuideRating {

	private int guideId = -1;
	private int voterUserId  = -1;
	//Rating 0.0 ~ 5.0
	private double rating = 0.0;
	
	private String comment = "";

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getGuideId() {
		return guideId;
	}

	public void setGuideId(int guideId) {
		this.guideId = guideId;
	}

	public int getVoterUserId() {
		return voterUserId;
	}

	public void setVoterUserId(int voterUserId) {
		this.voterUserId = voterUserId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
