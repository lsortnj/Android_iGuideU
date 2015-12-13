package mo.iguideu.ui.rating;

import java.util.ArrayList;
import java.util.List;

import mo.iguideu.guide.Guest;
import mo.iguideu.guide.GuideEvent;

public class GuideToRateTraveler extends GuideEvent {

	private String tatgetDate = "";
	private List<Guest> guests = new ArrayList<Guest>();
	private int	   guestCount = 0;
	
	public String getTatgetDate() {
		return tatgetDate;
	}
	public void setTatgetDate(String tatgetDate) {
		this.tatgetDate = tatgetDate;
	}
	public int getGuestCount() {
		return guestCount;
	}
	public void setGuestCount(int guestCount) {
		this.guestCount = guestCount;
	}
	public List<Guest> getGuests() {
		return guests;
	}
	public void setGuests(List<Guest> guests) {
		this.guests = guests;
	}
	public List<Integer> getAllGuestIDs(){
		List<Integer> guestsIDs = new ArrayList<Integer>();
		for(Guest guest : getGuests()){
			guestsIDs.add(guest.getId());
		}
		return guestsIDs;
	}
	
}
