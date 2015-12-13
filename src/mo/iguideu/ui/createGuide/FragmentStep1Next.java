package mo.iguideu.ui.createGuide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mo.iguideu.R;
import mo.iguideu.guide.GuideSession;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * Create new Guide step1: 
 * 	Guide name 
 *  Max people
 *  Guide Type: All Day or By Session
 * 
 * @author lsortnj
 * 
 */
public class FragmentStep1Next extends Fragment implements IStepValidateCheck,
		OnClickListener {

	private TextView durationHour = null;
	private TextView durationMin = null;

	private ImageButton durationHourPlus = null;
	private ImageButton durationHourMinus = null;
	private ImageButton durationMinPlus = null;
	private ImageButton durationMinMinus = null;

	private ScrollView scrollViewGuideSessions = null;
	private RelativeLayout layoutDuration = null;
	private LinearLayout layoutGuideSessions = null;
	private RadioGroup radioGroupGuideType = null;
	
	private TimePickerDialog timePickerDialog = null;
	private String			 timeChooseTitle  = "";

	private EditText currentEditStartTime = null;
	private EditText currentEditEndTime   = null;
	private Button   btnAddGuideSession   = null;
	
	private LayoutInflater inflater;
	
	private int currentDurationHour = 0;
	private int currentDurationMin = 10;

	private boolean isAllDayGuide = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater aInflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = aInflater;
		View v = inflater.inflate(R.layout.fragment_creat_step1_next, container,
				false);
		durationHour = (TextView) v.findViewById(R.id.create_guide_txt_duration_hour);
		durationMin = (TextView) v.findViewById(R.id.create_guide_txt_duration_min);

		durationHourPlus = (ImageButton) v.findViewById(R.id.create_guide_btn_duration_hour_plus);
		durationHourMinus = (ImageButton) v.findViewById(R.id.create_guide_btn_duration_hour_minus);
		durationMinPlus = (ImageButton) v.findViewById(R.id.create_guide_btn_duration_min_plus);
		durationMinMinus = (ImageButton) v.findViewById(R.id.create_guide_btn_duration_min_minus);

		radioGroupGuideType = (RadioGroup) v.findViewById(R.id.create_guide_radio_group_guide_type);
		radioGroupGuideType.setOnCheckedChangeListener(new GuideTypeCheckedChangeListener());

		layoutGuideSessions = (LinearLayout) v.findViewById(R.id.create_guide_layout_guide_sessions);
		scrollViewGuideSessions = (ScrollView) v.findViewById(R.id.create_guide_scroll_guide_session);
		layoutDuration = (RelativeLayout) v.findViewById(R.id.create_guide_layout_duration);
		
		btnAddGuideSession = (Button) v.findViewById(R.id.create_guide_btn_add_session);
		btnAddGuideSession.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addNewGuideSession();
			}
		});

		durationHourPlus.setOnClickListener(this);
		durationHourMinus.setOnClickListener(this);
		durationMinPlus.setOnClickListener(this);
		durationMinMinus.setOnClickListener(this);

		durationHour.setText("" + currentDurationHour);
		durationMin.setText("" + currentDurationMin);

		if (isAllDayGuide)
			radioGroupGuideType.check(R.id.create_guide_radio_type_all_day);
		else
			radioGroupGuideType.check(R.id.create_guide_radio_type_session);

		return v;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		updateGuideSessionTimeEditListener();
	}
	
	private void addNewGuideSession(){
		View guideSession = inflater.inflate(R.layout.partial_guide_session, null,false);
		layoutGuideSessions.removeView(btnAddGuideSession);
		layoutGuideSessions.addView(guideSession);
		layoutGuideSessions.addView(btnAddGuideSession);
		TextView tvOrder = (TextView) guideSession.findViewById(R.id.guide_session_txt_order);
		tvOrder.setText(""+(layoutGuideSessions.getChildCount()-1));
		updateGuideSessionTimeEditListener();
	}
	
	private void removeGuideSession(View guideSession){
		((ViewGroup)guideSession.getParent()).removeView(guideSession);
		refreshGuideSessionOrder();
	}
	
	private void refreshGuideSessionOrder(){
		TextView tvOrder = null;
		for (int i = 0; i < layoutGuideSessions.getChildCount(); i++) {
			try {
				if(!(layoutGuideSessions.getChildAt(i) instanceof RelativeLayout))
					continue;
				tvOrder = (TextView) layoutGuideSessions.getChildAt(i).findViewById(R.id.guide_session_txt_order);
				tvOrder.setText(""+(i+1));
			} catch (Exception e) {
			}
		}
	}
	
	private void updateGuideSessionTimeEditListener(){
		RelativeLayout layoutSession 	= null;
		EditText 	startTime 			= null;
		EditText 	endTime 			= null;
		ImageButton btnDelete 			= null;
		
		for (int i = 0; i < layoutGuideSessions.getChildCount(); i++) {
			try {
				if(!(layoutGuideSessions.getChildAt(i) instanceof RelativeLayout))
					continue;
				
				layoutSession = (RelativeLayout) layoutGuideSessions.getChildAt(i);
				startTime 	  = (EditText) layoutSession.findViewById(R.id.guide_session_edit_time_from);
				endTime 	  = (EditText) layoutSession.findViewById(R.id.guide_session_edit_time_to);
				btnDelete	  = (ImageButton) layoutSession.findViewById(R.id.guide_session_btn_delete);
				
				startTime.setInputType(EditorInfo.TYPE_NULL);
				endTime.setInputType(EditorInfo.TYPE_NULL);
				
				btnDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						layoutGuideSessions.removeView(((RelativeLayout)((LinearLayout)v.getParent()).getParent()));
						refreshGuideSessionOrder();
					}
				});
				
				startTime.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						currentEditStartTime = (EditText) v;
						currentEditEndTime = (EditText)((LinearLayout)v.getParent()).findViewById(R.id.guide_session_edit_time_to);
						timeChooseTitle = getResources().getString(R.string.title_time_from);
						showTimePickerDialog();
					}
				});
				endTime.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						currentEditEndTime = (EditText) v;
						currentEditStartTime = (EditText)((LinearLayout)v.getParent()).findViewById(R.id.guide_session_edit_time_from);
						timeChooseTitle = getResources().getString(R.string.title_time_to);
						showTimePickerDialog();
					}
				});
			} catch (Exception e) {
				Log.e("CreateGuideStep1", "Get Guide session:" + e.toString());
			}
		}
	}
	
	private void swapStartAndEndTime(){
		String tmp = currentEditEndTime.getText().toString();
		currentEditEndTime.setText(currentEditStartTime.getText().toString());
		currentEditStartTime.setText(tmp);
	}
	
	private void showTimePickerDialog(){
		timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            	Calendar calendar = Calendar.getInstance();
            	
                if(timeChooseTitle.equals(getResources().getString(R.string.title_time_from))){
                	currentEditStartTime.setText(selectedHour+":"+(String.valueOf(selectedMinute).length()==1?"0":"")+selectedMinute);
                	calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            		calendar.set(Calendar.MINUTE, selectedMinute);
                	if(!currentEditEndTime.getText().equals("")){
                		//Avoid end time bigger than start time
                		try{
                			int toHour   = Integer.parseInt(currentEditEndTime.getText().toString().split(":")[0]);
                			int toMinute = Integer.parseInt(currentEditEndTime.getText().toString().split(":")[1]);
                			//Hour wrong
                			if(selectedHour>toHour)
                				swapStartAndEndTime();
                			//Minute wrong
                			if((selectedHour==toHour) && (selectedMinute>toMinute))
                				swapStartAndEndTime();
                			//Update startDatetime
                			selectedHour  = Integer.parseInt(currentEditStartTime.getText().toString().split(":")[0]);
                			selectedMinute= Integer.parseInt(currentEditStartTime.getText().toString().split(":")[1]);
                    		calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    		calendar.set(Calendar.MINUTE, selectedMinute);
                		}catch(Exception e){
                			Log.e("SetTime", e.toString());
                		}
                	}
                }
                if(timeChooseTitle.equals(getResources().getString(R.string.title_time_to))){
                	currentEditEndTime.setText(selectedHour+":"+(String.valueOf(selectedMinute).length()==1?"0":"")+selectedMinute);
                	calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            		calendar.set(Calendar.MINUTE, selectedMinute);
                	if(!currentEditStartTime.getText().equals("")){
                		//Avoid end time bigger than start time
                		try{
                			int fromHour   = Integer.parseInt(currentEditStartTime.getText().toString().split(":")[0]);
                			int fromMinute = Integer.parseInt(currentEditStartTime.getText().toString().split(":")[1]);
                			if(selectedHour<fromHour)
                				swapStartAndEndTime();
                			if((selectedHour==fromHour) && (selectedMinute<fromMinute))
                				swapStartAndEndTime();
                			
                			//Update ndDatetime
                			selectedHour  = Integer.parseInt(currentEditEndTime.getText().toString().split(":")[0]);
                			selectedMinute= Integer.parseInt(currentEditEndTime.getText().toString().split(":")[1]);
                    		calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    		calendar.set(Calendar.MINUTE, selectedMinute);
                		}catch(Exception e){
                			Log.e("SetTime", e.toString());
                		}
                	}
                }
            }
        }, 12, 00, true);
		timePickerDialog.setTitle(timeChooseTitle);
		timePickerDialog.show();
	}
	

	private boolean isGuideSessionValid() {
		RelativeLayout layoutSession = null;
		EditText startTime = null;
		EditText endTime = null;
		boolean result = true;

		for (int i = 0; i < layoutGuideSessions.getChildCount(); i++) {
			try {
				layoutSession = (RelativeLayout) layoutGuideSessions.getChildAt(i);
				startTime = (EditText) layoutSession.findViewById(R.id.guide_session_edit_time_from);
				endTime = (EditText) layoutSession.findViewById(R.id.guide_session_edit_time_to);
				
				if (startTime.getText().toString().trim().equals("")
						|| endTime.getText().toString().trim().equals("")) {
					result = false;
					break;
				}
			} catch (Exception e) {
				Log.e("CreateGuideStep1", "Get Guide session:" + e.toString());
			}
		}

		return result;
	}

	@Override
	public boolean isDataValid() {

		if (radioGroupGuideType.getCheckedRadioButtonId() == R.id.create_guide_radio_type_session)
			return isGuideSessionValid();

		return true;
	}

	public List<GuideSession> getGuideSessions(){
		RelativeLayout layoutSession = null;
		EditText startTime = null;
		EditText endTime = null;
		List<GuideSession> guideSessions = new ArrayList<GuideSession>();
		
		for (int i = 0; i < layoutGuideSessions.getChildCount(); i++) {
			try {
				layoutSession = (RelativeLayout) layoutGuideSessions.getChildAt(i);
				startTime = (EditText) layoutSession.findViewById(R.id.guide_session_edit_time_from);
				endTime = (EditText) layoutSession.findViewById(R.id.guide_session_edit_time_to);
				
				if(startTime.getText().toString().equals("")||endTime.getText().toString().equals(""))
					continue;
				
				GuideSession guideSession = new GuideSession();
				guideSession.setStartTime(startTime.getText().toString().trim());
				guideSession.setEndTime(endTime.getText().toString().trim());
				guideSessions.add(guideSession);
			} catch (Exception e) {
				Log.e("CreateGuideStep1", "Get Guide session:" + e.toString());
			}
		}
		
		return guideSessions;
	}
	
	public boolean isAllDayGuide(){
		return isAllDayGuide;
	}
	
	public int getDuration() {
		int totalMinuts = 0;

		totalMinuts = (currentDurationHour * 60) + currentDurationMin;

		return totalMinuts;
	}

	@Override
	public String getDataInValidString() {
		String invalidString = "";

		return invalidString;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.create_guide_btn_duration_hour_plus:
			currentDurationHour++;
			if (currentDurationHour >= 24)
				currentDurationHour = 24;
			break;
		case R.id.create_guide_btn_duration_hour_minus:
			currentDurationHour--;
			if (currentDurationHour <= 0)
				currentDurationHour = 0;
			break;
		case R.id.create_guide_btn_duration_min_plus:
			currentDurationMin += 5;
			if (currentDurationMin >= 60) {
				currentDurationMin = 0;
				currentDurationHour++;
			}
			break;
		case R.id.create_guide_btn_duration_min_minus:
			currentDurationMin--;
			if (currentDurationMin <= 10)
				currentDurationMin = 10;
			break;
		}

		durationHour.setText("" + currentDurationHour);
		durationMin.setText("" + currentDurationMin);
	}

	class GuideTypeCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.create_guide_radio_type_all_day:
				isAllDayGuide = true;
				layoutDuration.setVisibility(View.VISIBLE);
				scrollViewGuideSessions.setVisibility(View.GONE);
				break;
			case R.id.create_guide_radio_type_session:
				isAllDayGuide = false;
				layoutDuration.setVisibility(View.GONE);
				scrollViewGuideSessions.setVisibility(View.VISIBLE);
				break;
			}
		}

	}

}
