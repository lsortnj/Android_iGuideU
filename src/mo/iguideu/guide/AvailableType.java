package mo.iguideu.guide;


public class AvailableType {
	public static final String AVAILABLE_TYPE_ANYTIME 			= "anytime";
	public static final String AVAILABLE_TYPE_EVERY_WEEK 		= "everyWeek";
	public static final String AVAILABLE_TYPE_EVERY_MONTH 		= "everyMonth";
	public static final String AVAILABLE_TYPE_SPECIFIC_DATETIME = "specificDatetime";
	public static final String AVAILABLE_TYPE_ASK_ME		 	= "askMe";
	
	private String key 		= "";
	private String showName = "";
	
	public AvailableType(){}
	
	public AvailableType(String key, String showName){
		setKey(key);
		setShowName(showName);
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	
}
