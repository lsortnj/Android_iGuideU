package mo.iguideu.ui.createGuide;

import mo.iguideu.R;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Create new Guide step1: Describe detailed schedule
 * @author lsortnj
 *
 */
public class FragmentStep3 extends Fragment implements IStepValidateCheck{
	
	EditText schedule = null;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_creat_step3, container, false);
		schedule = (EditText) v.findViewById(R.id.create_guide_step3_edit_schedule);
	    return v;
	 }

	@Override
	public boolean isDataValid() {
		
		if(schedule!=null){
			if(!schedule.getText().toString().equals("")){
				return true;
			}
		}
		
		return false;
	}
	
	public String getSchedule(){
		if(schedule!=null)
			return schedule.getText().toString();
		
		return "";
	}

	@Override
	public String getDataInValidString() {
		return getActivity().getString(R.string.tip_create_guide_schedule_empty);
	}
}
