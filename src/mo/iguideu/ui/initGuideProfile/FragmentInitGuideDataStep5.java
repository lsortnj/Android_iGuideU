package mo.iguideu.ui.initGuideProfile;

import mo.iguideu.R;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentInitGuideDataStep5 extends Fragment implements IStepValidateCheck{
	
	private EditText editIntro = null;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_init_guide_data_step5, container, false);
		editIntro = (EditText) v.findViewById(R.id.init_guide_edit_intro);
		
		return v;
	 }

	public String getIntroduction(){
		return editIntro.getText().toString();
	}
	 
	@Override
	public boolean isDataValid() {
		if(!editIntro.getText().toString().equals(""))
			return true;
		
		return false;
	}
	
	@Override
	public String getDataInValidString() {
		if(!isDataValid()){
			return getActivity().getString(R.string.tip_init_guide_intro_empty);
		}else{
			return "";
		}
	}
	
}
