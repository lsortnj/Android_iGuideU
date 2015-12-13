package mo.iguideu.ui.rating;

import java.util.ArrayList;
import java.util.List;

public class TravelerRating {

	private List<Integer> travelerIDs = new ArrayList<Integer>();
	
	private Integer guideId 	= 0;
	private double  rating 		= 0;
	private String  comment 	= "";
	private String  targetDate  = "";
	public List<Integer> getTravelerIDs() {
		return travelerIDs;
	}
	public void setTravelerIDs(List<Integer> travelerIDs) {
		this.travelerIDs = travelerIDs;
	}
	public Integer getGuideId() {
		return guideId;
	}
	public void setGuideId(Integer guideId) {
		this.guideId = guideId;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	
	
}
