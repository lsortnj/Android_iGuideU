package mo.iguideu.ui.joinGuide;

import mo.iguideu.R;
import mo.iguideu.guide.GuideSession;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.ui.base.DialogPositiveNegative;
import mo.iguideu.user.User;
import mo.iguideu.user.UserManager;
import mo.iguideu.util.CalendarUtil;
import mo.iguideu.util.GuideUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ActivityJoinGuide extends ActionBarActivity implements JoinGuideListener{

	private FragmentChooseDate fragmentChooseDate = new FragmentChooseDate();
	private GuideSession guideSession = null;
	private int guestCount = 1;	
	private String joinDateString = "";
	private DialogPositiveNegative dialogFianlConfirm  = null;
	private DialogPositiveNegative dialogAddToCalendar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_guide);
		setTitle(getResources().getString(R.string.title_join_guide)+"-"+GuideUtil.getTargetGuide().getGuideName());
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getSupportFragmentManager().beginTransaction()
			.add(R.id.activity_join_guide_layout_container, fragmentChooseDate).commit();
	}
	
	@Override
	public void onStart(){
		super.onStart();
		fragmentChooseDate.setJoinListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	    case android.R.id.home:
	    	finish();
	        return true;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	public GuideSession getGuideSession() {
		return guideSession;
	}

	public void setGuideSession(GuideSession guideSession) {
		this.guideSession = guideSession;
	}

	public int getGuestCount() {
		return guestCount;
	}

	public void setGuestCount(int guestCount) {
		this.guestCount = guestCount;
	}

	private Handler joinHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ServerInfo.RES_CODE_JOIN_GUIDE_SUCCESS:
				showAddToCalendarDialog();
				break;
			case ServerInfo.RES_CODE_USER_NOT_FOUND:
				Toast.makeText(getApplicationContext(), "RES_CODE_USER_NOT_FOUND", Toast.LENGTH_LONG).show();;
				break;
			case ServerInfo.RES_CODE_GUIDE_NOT_FOUND:
				Toast.makeText(getApplicationContext(), "RES_CODE_GUIDE_NOT_FOUND", Toast.LENGTH_LONG).show();;
				break;
			case ServerInfo.RES_CODE_GUIDE_ALREADY_JOINED:
				Toast.makeText(getApplicationContext(), "RES_CODE_GUIDE_ALREADY_JOINED", Toast.LENGTH_LONG).show();;
				break;
			case ServerInfo.RES_CODE_ERROR_WHEN_JOIN_GUIDE_TOO_MANY_GUEST:
				Toast.makeText(getApplicationContext(), "RES_CODE_ERROR_WHEN_JOIN_GUIDE_TOO_MANY_GUEST", Toast.LENGTH_LONG).show();;
				break;
			case ServerInfo.RES_CODE_ERROR_WHEN_JOIN_GUIDE_SESSION_NOT_FOUND:
				Toast.makeText(getApplicationContext(), "RES_CODE_ERROR_WHEN_JOIN_GUIDE_SESSION_NOT_FOUND", Toast.LENGTH_LONG).show();;
				break;
			default:
				Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();;
			}
		}
	};
	
	private Handler handlerGuiderInfo = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ServerInfo.GET_GUIDER_INFO_DONE:
				User guider = new User();
				guider.restoreFromJSON((String)msg.obj);
				CalendarUtil.addJoinGuideEventToCalendar(
						GuideUtil.getTargetGuide(), 
						guider, 
						joinDateString, 
						guideSession);
				finish();
				break;
			}
		}
	};
	
	private void addGuideJoinEventToCalendar(){
		ServerCore.instance().getGuiderAllInfo(GuideUtil.getTargetGuide().getGuiderId(), handlerGuiderInfo);
	}
	
	private void showAddToCalendarDialog(){
		dialogAddToCalendar = new DialogPositiveNegative(
				this,"",getResources().getString(R.string.message_add_reminder_to_calendar),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						addGuideJoinEventToCalendar();
						dialogAddToCalendar.dismiss();
					}
			});
		dialogAddToCalendar.setNegtiveOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {finish();}
		});
		dialogAddToCalendar.isHideIcon(true);
		dialogAddToCalendar.show();
	}
	
	private void showFinalConfirmDialog(){
		String msg = "";
		
		if(GuideUtil.getTargetGuide().isAllDayGuide())
			msg = getResources().getString(R.string.message_join_all_day_guide_confirm,
					GuideUtil.getTargetGuide().getGuideName(),
					joinDateString,
					guestCount);
		else
			msg = getResources().getString(R.string.message_join_session_guide_confirm,
					GuideUtil.getTargetGuide().getGuideName(),
					joinDateString,
					guideSession.getStartTime()+"~"+guideSession.getEndTime(),
					guestCount);
		
		dialogFianlConfirm = new DialogPositiveNegative(
				this,"",msg,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						ServerCore.instance().joinGuide(
								UserManager.getUser().getId(), 
								GuideUtil.getTargetGuide().getId(), 
								guestCount,
								joinDateString,
								guideSession,
								joinHandler);
						dialogFianlConfirm.dismiss();
					}
			});
		dialogFianlConfirm.isHideIcon(true);
		dialogFianlConfirm.show();
	}
	
	@Override
	public void onGuestCountConfirm(int guestCount) {
		setGuestCount(guestCount);
	}

	@Override
	public void onGuideSessionConfirm(SessionJoinStatus guideSession) {
		setGuideSession(guideSession);
	}

	@Override
	public void onReadyToJoinGuide() {
		showFinalConfirmDialog();
	}

	@Override
	public void onDateChoosed(String dateString) {
		joinDateString = dateString;
	}
}
