package mo.iguideu.ui.rating;

import java.util.ArrayList;
import java.util.List;

import mo.iguideu.R;
import mo.iguideu.guide.Guest;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.ui.base.ViewFactory;
import mo.iguideu.util.MoProgressDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogRateTravelerList extends Dialog {

	private List<Integer> 	    selectedUserId = new ArrayList<Integer>();
	private GuideToRateTraveler guide 				= null;
	private LinearLayout  		travelerContainer 	= null;
	private CheckboxCheckListener checkListener 	= null;
	
	private Button btnRate ;
	
	public DialogRateTravelerList(Context context, GuideToRateTraveler aGuide){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_rate_traveler_list);
		
		guide = aGuide;
		
		travelerContainer = (LinearLayout) findViewById(R.id.dialog_rate_traveler_list_layout_traveler_container);
		btnRate     	  = (Button) findViewById(R.id.dialog_rate_traveler_list_btn_rate);
		
		TextView  guideName   = (TextView) findViewById(R.id.dialog_rate_traveler_list_txt_guide_name);
		TextView  targetDate  = (TextView) findViewById(R.id.dialog_rate_traveler_list_txt_target_date);
		Button    btnCancel   = (Button) findViewById(R.id.dialog_rate_traveler_list_btn_cancel);
		
		ButtonClickListener   btnListener 	= new ButtonClickListener();
		
		checkListener = new CheckboxCheckListener();
		btnRate.setOnClickListener(btnListener);
		btnCancel.setOnClickListener(btnListener);
		
		guideName.setText(guide.getGuideName());
		targetDate.setText(guide.getTatgetDate());
		
		doFetchTravelerInfo();
	}
	
	private void doFetchTravelerInfo(){
		
		travelerContainer.removeAllViews();
		
		for(Guest guest : guide.getGuests()){
			addToScrollViewContainer(guest);
		}
	}
	
	private void addToScrollViewContainer(Guest guest){
		View.OnClickListener clickListener = new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				try{
					CheckBox chkbox = (CheckBox)v.findViewById(R.id.list_item_rating_traveler_checkbox);
					chkbox.setChecked(!chkbox.isChecked());
				}catch(Exception e){Log.e("DialogRateTravelerList", "onclick:"+e.toString());}
			}
		};
		
		View travelerInfoView = ViewFactory.getGuestView(getContext(), guest,"", true, clickListener, checkListener);
		
		travelerContainer.addView(travelerInfoView);
	}
	
	private void refreshRateButton(){
		btnRate.setText(selectedUserId.size()>0?
				getContext().getResources().getString(R.string.btn_rate,selectedUserId.size()):
					getContext().getResources().getString(R.string.btn_rate_all));
	}
	
	private void doRateTraveler(){
		boolean isRateAll = (selectedUserId.size()==0)?true:false;
			
		DialogRateTraveler dialogRate = new DialogRateTraveler(
				getContext(),
				(isRateAll?guide.getAllGuestIDs():selectedUserId),
				guide.getTatgetDate(),
				guide.getId(),
				isRateAll);
		dialogRate.setHandler(rateHandler);
		dialogRate.show();
	}
	
	private void removeTravelerByUserId(List<Integer> userIDs){
		for(int i=0; i<travelerContainer.getChildCount(); i++){
			
			for(Integer uid : userIDs){
				if(travelerContainer.getChildAt(i).getTag() == uid){
					travelerContainer.removeViewAt(i);
				}
			}
		}
		
		if(travelerContainer.getChildCount()==0){
			//Rating All Done
			dismiss();
		}
	}
	
	
	private Handler rateHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ServerInfo.RATE_TRAVELER_SUCCESS:
				Toast.makeText(getContext(), 
						getContext().getResources().getString(R.string.tip_rate_traveler_done), 
							Toast.LENGTH_SHORT).show();
				TravelerRating rating = (TravelerRating) msg.obj;
				removeTravelerByUserId(rating.getTravelerIDs());
				break;
			}
			
			MoProgressDialog.dismissProgress();
		}
	};
	
	class CheckboxCheckListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if(isChecked)
				selectedUserId.add((Integer) buttonView.getTag());
			else
				selectedUserId.remove((Integer) buttonView.getTag());
			
			refreshRateButton();
		}
	}
	
	class ButtonClickListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.dialog_rate_traveler_list_btn_rate:
				doRateTraveler();
				break;
			case R.id.dialog_rate_traveler_list_btn_cancel:
				dismiss();
				break;
			}
		}
		
	}
}
