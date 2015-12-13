package mo.iguideu.ui.upcoming;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import mo.iguideu.R;
import mo.iguideu.guide.Guest;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.ui.base.ViewFactory;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogGuestList extends Dialog {

	private List<Guest>			guests				= new ArrayList<Guest>();
	private GuiderUpcomingGuide guide 				= null;
	private LinearLayout  		travelerContainer 	= null;
	private JSONObject 			sessionJsonObj 		= null;
	
	public DialogGuestList(Context context, GuiderUpcomingGuide aGuide){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_guest_list);
		
		guide   = aGuide;
		
		if(!guide.isAllDayGuide()){
			sessionJsonObj = new JSONObject();
			sessionJsonObj.put("start", guide.getSessionStart());
			sessionJsonObj.put("end", guide.getSessionEnd());
		}
		
		travelerContainer = (LinearLayout) findViewById(R.id.dialog_guest_layout_guest_container);
		
		TextView  guideName   = (TextView) findViewById(R.id.dialog_guest_list_txt_guide_name);
		TextView  targetDate  = (TextView) findViewById(R.id.dialog_guest_list_txt_target_date);
		Button    btnOK       = (Button) findViewById(R.id.dialog_guest_list_btn_ok);
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {dismiss();}
		});
		
		guideName.setText(guide.getGuideName());
		String sessionString = guide.isAllDayGuide()?"":guide.getSessionStart()+"~"+guide.getSessionEnd();
		targetDate.setText(guide.getTargetDate()+" "+sessionString);
		
		refresh();
	}
	
	private void restoreGuestInfo(String jsonArrString){
		if(jsonArrString != null && jsonArrString.startsWith("[")){
			JSONArray  jsonArr = JSONArray.fromObject(jsonArrString);
			JSONObject jsonObj = null;
			for(int i=0; i<jsonArr.size(); i++){
				jsonObj = jsonArr.getJSONObject(i);
				Guest guest = new Guest();
				guest.setId(jsonObj.getInt("user_id"));
				guest.setName(jsonObj.getJSONObject("user").getString("name"));
				guest.setPeopleWillShow(jsonObj.getInt("guest_count"));
				
				guests.add(guest);
			}
			addGuestView();
		}
	}
	
	private void addGuestView(){
		for(Guest guest : guests){
			View travelerInfoView = ViewFactory.getGuestView(
					getContext(), 
					guest,
					guest.getPeopleWillShow()+" "+getContext().getResources().getString(R.string.text_people_will_show),
					false, null, null);
			travelerContainer.addView(travelerInfoView);
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == ServerInfo.GET_GUEST_LIST_DONE)
				restoreGuestInfo((String) msg.obj);
		}
	};
	
	private void refresh(){
		travelerContainer.removeAllViews();
		guests.clear();
		
		String session = (sessionJsonObj==null)?"":sessionJsonObj.toString();
		ServerCore.instance().getRollCallGuestList(handler,guide.getId(),guide.getTargetDate(),session);
	}
}
