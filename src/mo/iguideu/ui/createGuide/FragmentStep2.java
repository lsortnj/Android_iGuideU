package mo.iguideu.ui.createGuide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import com.squareup.timessquare.CalendarPickerView;
//import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;

import mo.iguideu.R;
import mo.iguideu.guide.AvailableType;
import mo.iguideu.guide.GuideEvent;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

/**
 * Create new Guide step1: choose available date 
 * @author lsortnj
 *
 */
public class FragmentStep2 extends Fragment implements Spinner.OnItemSelectedListener{
	
	private RelativeLayout layoutDateChoose 		= null;
	private RelativeLayout layoutTimeChoose 		= null;
	private RelativeLayout layoutWeekdayChoose 		= null;
	private RelativeLayout layoutMonthDayChoose 	= null;
	
	private Spinner spinnerAvailableType 	= null;
	private Spinner spinnerWeekday 			= null;
	private Spinner spinnerMonthDay 		= null;
	private EditText	editTimeFrom		= null;
	private EditText	editTimeTo			= null;
	
	
	private EditText editDate = null;
	
	private List<AvailableType> allAvailableType = new ArrayList<AvailableType>();
	private SpinnerAvailabaleTypeAdapter avAdapter;
	
	private Dialog 			 dateChooseDialog = null;
	private TimePickerDialog timePickerDialog = null;
	private String			 timeChooseTitle  = "";
	
	private SimpleDateFormat dateFomat 		= new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat weekdayFormat 	=  new SimpleDateFormat("EEEE");

		
	private GuideEvent guide = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		timeChooseTitle = getResources().getString(R.string.title_time_from);
		dateFomat 		= new SimpleDateFormat("yyyy-MM-dd", getResources().getConfiguration().locale);
		weekdayFormat 	= new SimpleDateFormat("EEEE", getResources().getConfiguration().locale);
		
		OnFocusChangeListener onFocusListener = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					if(v.equals(editTimeFrom)){
						showChooseStartTimeDialog();
					}
					if(v.equals(editTimeTo)){
						showChooseEndTimeDialog();
					}
				}
			}
		}; 
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.equals(editTimeFrom)){
					showChooseStartTimeDialog();
				}
				if(v.equals(editTimeTo)){
					showChooseEndTimeDialog();
				}
			}
		};
		
		//Move to here cause getResources()
		editTimeFrom.setOnFocusChangeListener(onFocusListener);
		editTimeFrom.setOnClickListener(onClickListener);
		editTimeTo.setOnFocusChangeListener(onFocusListener);
		editTimeTo.setOnClickListener(onClickListener);
	}
	
	private void showChooseStartTimeDialog(){
		timeChooseTitle = getResources().getString(R.string.title_time_from);
		try{
			if(!editTimeFrom.getText().equals("")){
				timePickerDialog.updateTime(
						Integer.parseInt(editTimeFrom.getText().toString().split(":")[0]), 
						Integer.parseInt(editTimeFrom.getText().toString().split(":")[1]));
			}
		}catch(Exception e){}
		timePickerDialog.setTitle(timeChooseTitle);
		timePickerDialog.show();
	}
	
	private void showChooseEndTimeDialog(){
		timeChooseTitle = getResources().getString(R.string.title_time_to);
		try{
			if(!editTimeTo.getText().equals("")){
				timePickerDialog.updateTime(
						Integer.parseInt(editTimeTo.getText().toString().split(":")[0]), 
						Integer.parseInt(editTimeTo.getText().toString().split(":")[1]));
			}
		}catch(Exception e){}
		timePickerDialog.setTitle(timeChooseTitle);
		timePickerDialog.show();
	}
	
	private void init(){
		initAvailableType();
		initWeekdaySpinner();
		initMonthDaySpinner();
//		initChooseDateDialog();
		initTimePickerDialog();
	}
	
	private void swapStartAndEndTime(){
		String tmp = editTimeTo.getText().toString();
		editTimeTo.setText(editTimeFrom.getText().toString());
		editTimeFrom.setText(tmp);
	}
	
	private void initTimePickerDialog(){
		timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            	Calendar calendar = Calendar.getInstance();
            	
                if(timeChooseTitle.equals(getResources().getString(R.string.title_time_from))){
                	editTimeFrom.setText(selectedHour+":"+(String.valueOf(selectedMinute).length()==1?"0":"")+selectedMinute);
                	calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            		calendar.set(Calendar.MINUTE, selectedMinute);
                	if(!editTimeTo.getText().equals("")){
                		//Avoid end time bigger than start time
                		try{
                			int toHour   = Integer.parseInt(editTimeTo.getText().toString().split(":")[0]);
                			int toMinute = Integer.parseInt(editTimeTo.getText().toString().split(":")[1]);
                			//Hour wrong
                			if(selectedHour>toHour)
                				swapStartAndEndTime();
                			//Minute wrong
                			if((selectedHour==toHour) && (selectedMinute>toMinute))
                				swapStartAndEndTime();
                			//Update startDatetime
                			selectedHour  = Integer.parseInt(editTimeFrom.getText().toString().split(":")[0]);
                			selectedMinute= Integer.parseInt(editTimeFrom.getText().toString().split(":")[1]);
                    		calendar.setTime(guide.getAvailableDatetime().getSpecificDate());
                    		calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    		calendar.set(Calendar.MINUTE, selectedMinute);
                		}catch(Exception e){
                			Log.e("SetTime", e.toString());
                		}
                		guide.getAvailableDatetime().setStartDateTime(calendar.getTime());
                	}
                }
                if(timeChooseTitle.equals(getResources().getString(R.string.title_time_to))){
                	editTimeTo.setText(selectedHour+":"+(String.valueOf(selectedMinute).length()==1?"0":"")+selectedMinute);
                	calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            		calendar.set(Calendar.MINUTE, selectedMinute);
                	if(!editTimeFrom.getText().equals("")){
                		//Avoid end time bigger than start time
                		try{
                			int fromHour   = Integer.parseInt(editTimeFrom.getText().toString().split(":")[0]);
                			int fromMinute = Integer.parseInt(editTimeFrom.getText().toString().split(":")[1]);
                			if(selectedHour<fromHour)
                				swapStartAndEndTime();
                			if((selectedHour==fromHour) && (selectedMinute<fromMinute))
                				swapStartAndEndTime();
                			
                			//Update ndDatetime
                			selectedHour  = Integer.parseInt(editTimeTo.getText().toString().split(":")[0]);
                			selectedMinute= Integer.parseInt(editTimeTo.getText().toString().split(":")[1]);
                    		calendar.setTime(guide.getAvailableDatetime().getSpecificDate());
                    		calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    		calendar.set(Calendar.MINUTE, selectedMinute);
                		}catch(Exception e){
                			Log.e("SetTime", e.toString());
                		}
                		guide.getAvailableDatetime().setEndDateTime(calendar.getTime());
                	}
                }
            }
        }, 12, 00, true);
		timePickerDialog.setTitle(timeChooseTitle);
	}
	
	
	private void initWeekdaySpinner(){
		String weekDayString = "";
		
		Calendar calendar = Calendar.getInstance();
		
		for(int i=1; i<=7; i++){
			calendar.set(Calendar.DAY_OF_WEEK, i);
			weekDayString += weekdayFormat.format(calendar.getTime())+(i==7?"":",");
		}
		
		String[] weekdayArr = weekDayString.split(",");
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity()
				,android.R.layout.simple_spinner_item, weekdayArr);
		
		spinnerWeekday.setAdapter(adapter);
		spinnerWeekday.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(guide!=null)
					guide.getAvailableDatetime().setDayOfWeek(
							restoreDayOfWeekFromString(
									parent.getItemAtPosition(position).toString()));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	private void initMonthDaySpinner(){
		String monthDayString = "";
		
		for(int i=1; i<=30; i++){
			monthDayString += i+(i==30?"":",");
		}
		
		String[] monthDayArr = monthDayString.split(",");
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity()
				,android.R.layout.simple_spinner_item, monthDayArr);
		
		spinnerMonthDay.setAdapter(adapter);
		spinnerMonthDay.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(guide!=null)
					guide.getAvailableDatetime().setDayOfMonth(Integer.parseInt(parent.getItemAtPosition(position).toString()));
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	private int restoreDayOfWeekFromString(String weekday){
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(weekdayFormat.parse(weekday));
			return calendar.get(Calendar.DAY_OF_WEEK);
		} catch (ParseException e) {
			Log.e("ParseWeekday", e.toString());
		}
		
		return Calendar.SUNDAY;
	}
	
//	private void initChooseDateDialog(){
//		dateChooseDialog = new Dialog(getActivity());
//		dateChooseDialog.setContentView(R.layout.dialog_date_picker);
//		
//		Calendar nextYear = Calendar.getInstance();
//		nextYear.add(Calendar.YEAR, 1);
//		Date today = new Date();
//
//		CalendarPickerView calendarView = (CalendarPickerView) dateChooseDialog.findViewById(R.id.calendar_view);
//		calendarView.init(today, nextYear.getTime()).withSelectedDate(today);
//		calendarView.setOnDateSelectedListener(new OnDateSelectedListener() {
//			
//			@Override
//			public void onDateUnselected(Date date) {
//			}
//			
//			@Override
//			public void onDateSelected(Date date) {
//				editDate.setText(dateFomat.format(date));
//				if(guide!=null)
//					guide.getAvailableDatetime().setSpecificDate(date);
//				dateChooseDialog.dismiss();
//			}
//		});
//	}
	
	private void initAvailableType(){
		allAvailableType.clear();
		
		allAvailableType.add(new AvailableType(
				AvailableType.AVAILABLE_TYPE_ANYTIME,
				getActivity().getString(R.string.av_type_any)));
		allAvailableType.add(new AvailableType(
				AvailableType.AVAILABLE_TYPE_EVERY_WEEK,
				getActivity().getString(R.string.av_type_every_week)));
		allAvailableType.add(new AvailableType(
				AvailableType.AVAILABLE_TYPE_EVERY_MONTH,
				getActivity().getString(R.string.av_type_every_month)));
		allAvailableType.add(new AvailableType(
				AvailableType.AVAILABLE_TYPE_SPECIFIC_DATETIME,
				getActivity().getString(R.string.av_type_specific)));
		allAvailableType.add(new AvailableType(
				AvailableType.AVAILABLE_TYPE_ASK_ME,
				getActivity().getString(R.string.av_type_ask_me)));
		
		avAdapter = new SpinnerAvailabaleTypeAdapter(getActivity(),allAvailableType);
		spinnerAvailableType.setAdapter(avAdapter);
		spinnerAvailableType.setOnItemSelectedListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_creat_step2, container, false);
		
		layoutDateChoose 		= (RelativeLayout) v.findViewById(R.id.create_layout_choose_date);
		layoutTimeChoose 		= (RelativeLayout) v.findViewById(R.id.create_layout_choose_time);
		layoutWeekdayChoose 	= (RelativeLayout) v.findViewById(R.id.create_layout_choose_weekday);
		layoutMonthDayChoose 	= (RelativeLayout) v.findViewById(R.id.create_layout_choose_month_day);
		
		layoutDateChoose.setVisibility(View.GONE);
		layoutTimeChoose.setVisibility(View.GONE);
		layoutWeekdayChoose.setVisibility(View.GONE);
		layoutMonthDayChoose.setVisibility(View.GONE);
		
		spinnerAvailableType = (Spinner) v.findViewById(R.id.create_spinner_available_type);
		spinnerWeekday		 = (Spinner) v.findViewById(R.id.create_spinner_weekday);
		spinnerMonthDay		 = (Spinner) v.findViewById(R.id.create_spinner_month_day);
		
		editTimeFrom		 = (EditText) layoutTimeChoose.findViewById(R.id.create_edit_time_from);
		editTimeTo			 = (EditText) layoutTimeChoose.findViewById(R.id.create_edit_time_to);
		
		editDate 			 = (EditText) layoutDateChoose.findViewById(R.id.create_edit_date);
		
		editTimeFrom.setInputType(EditorInfo.TYPE_NULL);
		editTimeTo.setInputType(EditorInfo.TYPE_NULL);
		
		
		
		editDate.setInputType(EditorInfo.TYPE_NULL);
		editDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					dateChooseDialog.show();
			}
		});
		editDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dateChooseDialog.show();
			}
		});
		
		init();	
		
		return v;
	}

	
	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		AvailableType avType = (AvailableType) avAdapter.getItem(position);
		
		if(avType!=null && guide!=null){
			guide.setAvailableType(avType.getKey());
			
			if(avType.getKey().equals(AvailableType.AVAILABLE_TYPE_ANYTIME)){
				layoutDateChoose.setVisibility(View.GONE);
				layoutTimeChoose.setVisibility(View.GONE);
				layoutWeekdayChoose.setVisibility(View.GONE);
				layoutMonthDayChoose.setVisibility(View.GONE);
			}
			if(avType.getKey().equals(AvailableType.AVAILABLE_TYPE_EVERY_WEEK)){
				layoutDateChoose.setVisibility(View.GONE);
				layoutTimeChoose.setVisibility(View.GONE);
				layoutWeekdayChoose.setVisibility(View.VISIBLE);
				layoutMonthDayChoose.setVisibility(View.GONE);
			}
			if(avType.getKey().equals(AvailableType.AVAILABLE_TYPE_EVERY_MONTH)){
				layoutDateChoose.setVisibility(View.GONE);
				layoutTimeChoose.setVisibility(View.GONE);
				layoutWeekdayChoose.setVisibility(View.GONE);
				layoutMonthDayChoose.setVisibility(View.VISIBLE);
			}
			if(avType.getKey().equals(AvailableType.AVAILABLE_TYPE_SPECIFIC_DATETIME)){
				layoutDateChoose.setVisibility(View.VISIBLE);
				layoutTimeChoose.setVisibility(View.VISIBLE);
				layoutWeekdayChoose.setVisibility(View.GONE);
				layoutMonthDayChoose.setVisibility(View.GONE);
			}
			if(avType.getKey().equals(AvailableType.AVAILABLE_TYPE_ASK_ME)){
				layoutDateChoose.setVisibility(View.GONE);
				layoutTimeChoose.setVisibility(View.GONE);
				layoutWeekdayChoose.setVisibility(View.GONE);
				layoutMonthDayChoose.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	public GuideEvent getGuideEvent() {
		return guide;
	}

	public void setGuideEvent(GuideEvent guide) {
		this.guide = guide;
	}
	
	
}
