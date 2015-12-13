package mo.iguideu.serverHandler;

/***
 * Define server API <br>
 * Start with C represent Controller<br>
 * Start with A represent Action<br>
 * Start with P represent Parameter
 * 
 * @author lsortnj
 */
public class ServerInfo {
	public static final String 	HOST = "http://192.168.99.13:3000/";
//	public static final String 	HOST = "http://192.168.0.19:3000/";
//	public static final String 	HOST = "http://192.168.3.2:3000/";
//	public static final String 	HOST = "http://www.iguideu.idv.tw/";
	
	public static final int FETCH_COUNT_EACH_TIME = 20;
	public static final int DEFAULT_SEARCH_RANGE  = 50; //KM
	
	//Status code From Server
	public static final int RES_CODE_UPDATE_GUIDE_DATA_SUCCESS 	 				= 10001; 
	public static final int RES_CODE_UPDATE_GUIDE_DATA_FAILED   				= 10002; 
	public static final int RES_CODE_SIGN_UP_SUCCESS		    				= 10003; 
	public static final int RES_CODE_SIGN_UP_FAILED_EXISTED	    				= 10004; 
	public static final int RES_CODE_USER_SIGNED_UP      	    				= 10005; 
	public static final int RES_CODE_USER_SIGN_UP_YET		    				= 10006; 
	public static final int RES_CODE_AFTER_SYNC_USER_DATA	    				= 10007; 
	public static final int RES_CODE_USER_NOT_FOUND             				= 10007; 
	public static final int RES_CODE_GUIDE_NOT_FOUND            				= 10008; 
	public static final int RES_CODE_JOIN_GUIDE_SUCCESS         				= 10009; 
	public static final int RES_CODE_JOIN_GUIDE_FAILED              			= 10010; 
	public static final int RES_CODE_GUIDE_ALREADY_JOINED           			= 10011; 
	public static final int RES_CODE_RATE_GUIDE_SUCCESS             			= 10012; 
	public static final int RES_CODE_RATE_GUIDE_FAILED              			= 10013; 	
	public static final int RES_CODE_ERROR_WHEN_JOIN_GUIDE_SESSION_NOT_FOUND 	= 10014; 
	public static final int RES_CODE_ERROR_WHEN_JOIN_GUIDE_TOO_MANY_GUEST    	= 10015; 
	public static final int RATE_TRAVELER_SUCCESS          						= 10016; 
	public static final int UPDATE_USER_PHOTO_SUCCESS                 			= 10017; 
	public static final int UPDATE_USER_PHOTO_FAILED                  			= 10018;  
	
	//Result code(Only use in Android)
	public static final int CREATE_GUIDE_FAILED 			= 0x500;
	public static final int CREATE_GUIDE_SUCCESS 			= 0x501;
	public static final int GET_GUIDE_NEARBY_DONE 			= 0x502;
	public static final int GET_MY_GUIDES_DONE 				= 0x503;
	public static final int GET_MY_JOIN_GUIDES_DONE 		= 0x504;
	public static final int GET_GUIDER_INFO_DONE    		= 0x505;
	public static final int FETCH_GUIDE_DONE        		= 0x506;
	public static final int FETCH_GUIDE_ERROR       		= 0x507;
	public static final int GET_GUIDE_CAN_RATE_DONE 		= 0x508;
	public static final int GET_TRAVELER_CAN_RATE_DONE 		= 0x509;
	public static final int DO_RATE_GUIDE_DONE				= 0x510;
	public static final int GET_GUIDE_TO_RATE_TRAVELER_DONE	= 0x511;
	public static final int GET_TRAVELER_UPCOMING_GUIDE_DONE= 0x512;
	public static final int GET_GUIDER_UPCOMING_GUIDE_DONE  = 0x513;
	public static final int GET_GUEST_LIST_DONE  			= 0x514;
	public static final int GET_GUIDE_AVAILABLE_STATUS_DONE = 0x515;
	
	
	//Image quality version
	public static final String VERSION_MEDIUM 	= "medium";
	public static final String VERSION_SMALL 	= "small";
	public static final String VERSION_ORIGIN	= "origin";
	 
	//Controller Send SMS
	public static final String	C_ACTIVATOR 				= "activator/";
	public static final String	A_SEND_SMS_ACTIVE_CODE 		= "send_sms_active_code/";
	
	//Controller Guide
	public static final String	C_GUIDE 					= "guide/";
	public static final String	A_GUIDE_NEARBY				= "list_guide_nearby/";
	public static final String	A_GET_MY_GUIDE				= "get_my_guide/";
	public static final String	A_FETCH_PHOTO				= "fetch_photo/";
	public static final String	A_MY_JOIN_GUIDES			= "get_my_join_guides/";
	public static final String	A_JOIN_GUIDE	    		= "join_guide/";
	public static final String	A_FETCH_GUIDER_SIMPLE_INFO	= "fetch_guider_info/";
	public static final String	A_FETCH_GUIDER_ALL_INFO		= "fetch_guider_all_info/";
	public static final String	A_GUIDE_POPULAR 			= "list_guide_popular/";
	public static final String	A_RATE_GUIDE    			= "rate_guide/";
	public static final String	A_RATE_TRAVELER    			= "rate_traveler/";
	public static final String	A_GET_GUIDES_CAN_RATE		= "get_guide_can_rating/";
	public static final String	A_GET_TRAVELER_CAN_RATE		= "get_traveler_can_rating/";
	public static final String	A_CREATE_GUIDE				= "create_new_guide/";
	public static final String	A_GET_TRAVELER_CAN_RATING	= "get_traveler_can_rating/";
	public static final String	A_GET_GUIDE_TO_RATE_TRAVELER= "get_my_finished_and_rate_traveler_yet_guide/";
	public static final String	A_TRAVELER_UPCOMING_GUIDE   = "traveler_upcoming_guide/";
	public static final String	A_GUIDER_UPCOMING_GUIDE     = "guider_upcoming_guide/";
	public static final String	A_FETCH_GUIDE_COVER		    = "get_guide_cover/";
	public static final String	A_ROLL_CALL				    = "roll_call/";
	public static final String	A_GUIDE_AVAILABLE_STATUS	= "get_guide_available_status/";
	
	
	//Controller User
	public static final String	C_USER				= "users/";
	public static final String 	A_REGISTER 			= "register/";
	public static final String 	A_SYNC_DATA 		= "sync_user_data/";
	public static final String 	A_UPDATE_GUIDE_DATA = "update_guide_data/";
	public static final String 	A_IS_SIGN_UP		= "check_sign_up/";
	public static final String 	A_FETCH_USER_PHOTO  = "fetch_user_photo/";
	public static final String 	A_UPDATE_USER_PHOTO = "update_photo/";
	
	//Parameter
	public static final String 	P_USER_JSON			= "user_json";
	public static final String 	P_USER_ID			= "user_id";
	public static final String 	P_USER_PHOTO		= "user_photo";
	public static final String 	P_GUIDE_DATA_JSON	= "guide_data_json";
	public static final String 	P_USERNAME			= "username";
	public static final String 	P_LATITUDE			= "lat";
	public static final String 	P_LONGITUDE			= "lng";
	public static final String 	P_FILE_NAME			= "filename";
	public static final String 	P_ID				= "id";
	public static final String 	P_VERSION			= "version";
	public static final String 	P_TRAVELER_IDS	    = "traveler_ids";
	public static final String 	P_RATING		    = "rating";
	public static final String 	P_COMMENT		    = "comment";
	public static final String 	P_GUIDE_ID		    = "guide_id";
	public static final String 	P_TARGET_DATE		= "target_date";
	
}
