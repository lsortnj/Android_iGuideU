package mo.iguideu.ui.joinGuide;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.wt.calendarcard.CalendarCardPager;
import com.wt.calendarcard.CardGridItem;
import com.wt.calendarcard.OnCellItemClick;

import mo.iguideu.R;
import mo.iguideu.guide.GuideSession;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.util.GuideUtil;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentChooseDate extends Fragment implements OnCellItemClick{

	private CalendarCardPager calendarCard = null;
	private JoinGuideListener joinListener = null;
	private String			  chooseDate   = "";
	
	private DialogChooseGuideSession dialogGuideSession = null;
	private DialogJoinGuestCount     dialogGuestCount   = null;
	
	private List<String> 	 guideDates  = new ArrayList<String>();
	private List<JoinStatus> joinStatues = new ArrayList<JoinStatus>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.calendar_view_pager, container,false);
		
		calendarCard = (CalendarCardPager) v.findViewById(R.id.calendar_card_pager);
		
		return v;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		refresh();
		
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==ServerInfo.GET_GUIDE_AVAILABLE_STATUS_DONE){
				String jsonArr = (String) msg.obj;
				if(jsonArr!=null&&jsonArr.startsWith("["))
					restoreDateSessionJSON(jsonArr);
				
				addAvailableDateToCalendar();
			}
		}
	};
	
	private void addAvailableDateToCalendar(){
		calendarCard.setOnCellItemClick(FragmentChooseDate.this);
		calendarCard.isBorwseOnly(true);
		calendarCard.setAvailableDate(guideDates);
		calendarCard.notifyDataChanged();
	}
	
	private void restoreDateSessionJSON(String jsonString){
		/**
		 * Session Guide:
		 * {"target_date":"2014-08-29","sessions":[{"session":"{\"start\"=>\"12:00\", \"end\"=>\"13:00\"}","remain_count":20}] }
		 * 
		 * All day Guide
		 * {"target_date":"2014-08-27","remain_count":20}
		 */
		boolean isAllDayGuide = GuideUtil.getTargetGuide().isAllDayGuide();
		JSONArray jsonArr = JSONArray.fromObject(jsonString);
		
		joinStatues.clear();
		guideDates.clear();
		
		for(int i=0; i<jsonArr.size(); i++){
			JSONObject jsonObj    = jsonArr.getJSONObject(i);
			JoinStatus joinStatus = new JoinStatus();
			
			joinStatus.setTargetDate(jsonObj.getString("target_date"));
			
			if(isAllDayGuide){
				joinStatus.setRemainCount(jsonObj.getInt("remain_count"));
				if(joinStatus.getRemainCount()==0)
					continue;
			}else{
				JSONArray sessionJson = JSONArray.fromObject(jsonObj.get("sessions"));
				for(int mo=0; mo<sessionJson.size(); mo++){
					JSONObject sessionObj = sessionJson.getJSONObject(mo);
					SessionJoinStatus sessionStatus = new SessionJoinStatus();
					sessionStatus.setStartTime(sessionObj.getJSONObject("session").getString(GuideSession.JSON_KEY_START_TIME));
					sessionStatus.setEndTime(sessionObj.getJSONObject("session").getString(GuideSession.JSON_KEY_END_TIME));
					sessionStatus.setRemainCount(sessionObj.getInt("remain_count"));
					joinStatus.getSessionStatues().add(sessionStatus);
				}
			}
			guideDates.add(jsonObj.getString("target_date"));
			joinStatues.add(joinStatus);
		}
	}

	private void refresh(){
		ServerCore.instance().getGuideAvailableStatus(handler, GuideUtil.getTargetGuide().getId());
	}
	
	public void setJoinListener(JoinGuideListener joinListener) {
		this.joinListener = joinListener;
	}

	private void showJoinGuestCountDialog(int remainCount){
		dialogGuestCount = new DialogJoinGuestCount(getActivity(),remainCount);
		dialogGuestCount.setJoinListener(joinListener);
		dialogGuestCount.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(dialogGuestCount.isConformGuestCount()){
					if(joinListener!=null)
						joinListener.onReadyToJoinGuide();
				}
			}
		});
		dialogGuestCount.show();
	}
	
	private void showGuideSessionsDialog(final JoinStatus joinStatus){
		dialogGuideSession = new DialogChooseGuideSession(
				getActivity(),
				joinStatus,
				joinListener);
		dialogGuideSession.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(dialogGuideSession.isGuideSessionChoosed()){
					SessionJoinStatus session = dialogGuideSession.getGuideSession();
					showJoinGuestCountDialog(session.getRemainCount());
				}
			}
		});
		dialogGuideSession.show();
	}
	
	public String getChooseDate() {
		return chooseDate;
	}
	
	public GuideSession getChooseGuideSession(){
		return dialogGuideSession.getGuideSession();
	}
	
	public int getGuestCount(){
		return dialogGuestCount.getGuestCount();
	}
	
	private JoinStatus getJoinstatus(String date){
		for(JoinStatus joinStatus : joinStatues){
			if(joinStatus.getTargetDate().equals(date))
				return joinStatus;
		}
		
		return null;
	}

	@Override
	public void onCellClick(CardGridItem item, boolean isSelect) {
		if(GuideUtil.getTargetGuide()!=null && isSelect && item!=null){
			JoinStatus joinStatus = getJoinstatus(item.getDateString());
			if(GuideUtil.getTargetGuide().isAllDayGuide()){
				showJoinGuestCountDialog(joinStatus.getRemainCount());
			}else{
				showGuideSessionsDialog(joinStatus);
			}
			chooseDate = item.getDateString();
			if(joinListener!=null)
				joinListener.onDateChoosed(chooseDate);
		}
	}

	@Override
	public void onCellSelect(View v, CardGridItem item) {
		
	}

	@Override
	public void onCellUnselect(View v, CardGridItem item) {
		
	}
}
