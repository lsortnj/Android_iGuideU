package mo.iguideu.ui.initGuideProfile;

import mo.iguideu.R;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class FragmentInitGuideDataStep1 extends Fragment implements IStepValidateCheck{
	
	private EditText licenseNo = null;
	private RadioGroup radioGroupGotLicense = null;
	private RelativeLayout licenseCodeArea = null;
	
	private boolean isGotLicense = false;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_init_guide_data_step1, container, false);
		licenseNo = (EditText) v.findViewById(R.id.fragment_init_guide_data_edit_license_no);
		radioGroupGotLicense = (RadioGroup) v.findViewById(R.id.fragment_init_guide_data_radio_group_got_license);
		licenseCodeArea =  (RelativeLayout) v.findViewById(R.id.fragment_init_guide_data_license_code_area);
		radioGroupGotLicense.setOnCheckedChangeListener(new GotLicenseCheckedChangeListener());
		
		//Init state
		licenseCodeArea.setVisibility(View.INVISIBLE);
		isGotLicense = false;
		radioGroupGotLicense.check(R.id.fragment_init_guide_data_radio_got_license_no);
		
		return v;
	 }

	 
	public String getLicenseNo(){
		return licenseNo.getText().toString();
	}
	 
	public boolean isGotLicense() {
		return isGotLicense;
	}


	@Override
	public boolean isDataValid() {
		if(isGotLicense && !licenseNo.getText().toString().equals("")){
			return true;
		}else if(isGotLicense && licenseNo.getText().toString().equals("")){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public String getDataInValidString() {
		if(isGotLicense && licenseNo.getText().toString().equals("")){
			return getActivity().getString(R.string.error_license_no_empty);
		}else{
			return "";
		}
	}
	
	class GotLicenseCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId){
			case R.id.fragment_init_guide_data_radio_got_license_yes:
				licenseCodeArea.setVisibility(View.VISIBLE);
				isGotLicense = true;
				break;
			case R.id.fragment_init_guide_data_radio_got_license_no:
				licenseCodeArea.setVisibility(View.INVISIBLE);
				isGotLicense = false;
				break;	
			}
		}
		
	}
}
