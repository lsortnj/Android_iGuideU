package mo.iguideu.guide;

import mo.iguideu.user.User;

public class Guest extends User {

	private int peopleWillShow = 0;

	public int getPeopleWillShow() {
		return peopleWillShow;
	}

	public void setPeopleWillShow(int peopleWillShow) {
		this.peopleWillShow = peopleWillShow;
	}
}
