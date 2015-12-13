package mo.iguideu.user;

import mo.iguideu.store.AppPreference;

public class UserManager {
	private static User user = new User();

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		UserManager.user = user;
		AppPreference.instance().setLastUsername(user.getUsername());
	}
	
	public static void syncUserData(String jsonDataFromServer){
		
		if(jsonDataFromServer!=null && !jsonDataFromServer.equals("")){
			user.restoreFromJSON(jsonDataFromServer);
		}
	}
}
