package mo.iguideu.ui.initTravelerProfile;

import net.sf.json.JSONObject;
import mo.iguideu.R;
import mo.iguideu.ui.base.IStepValidateCheck;
import mo.iguideu.user.UserManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FragmentInitTravelerDataStep1 extends Fragment implements IStepValidateCheck{
	
private EditText editRealname = null;
	
	private EditText editPhone1 = null;
	private EditText editPhone2 = null;
	private EditText editPhone3 = null;
	
	private RadioGroup radioGroupGender = null;
	
	private boolean gender = true;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_init_traveler_data_step1, container, false);
		editRealname = (EditText) v.findViewById(R.id.init_traveler_edit_realname);
		
		editPhone1 = (EditText) v.findViewById(R.id.init_treveler_edit_phone1);
		editPhone2 = (EditText) v.findViewById(R.id.init_treveler_edit_phone2);
		editPhone3 = (EditText) v.findViewById(R.id.init_treveler_edit_phone3);
		
		radioGroupGender = (RadioGroup) v.findViewById(R.id.init_traveler_radio_group_gender);
		radioGroupGender.setOnCheckedChangeListener(new GotLicenseCheckedChangeListener());
		
		if(UserManager.getUser().getGender())
			radioGroupGender.check(R.id.init_traveler_radio_male);
		else
			radioGroupGender.check(R.id.init_traveler_radio_female);
		
		editRealname.setText(UserManager.getUser().getName());
		
		refreshPhoneNumber();
		
		return v;
	 }
	 
	 private void refreshPhoneNumber(){
		 if(UserManager.getUser().getPhoneNumberJSON().startsWith("{")){
			 JSONObject json = JSONObject.fromObject(UserManager.getUser().getPhoneNumberJSON());
			 for(Object phone: json.keySet()){
				 if(phone.toString().equals("phone1"))
					 editPhone1.setText(json.getString(phone.toString()));
				 if(phone.toString().equals("phone2"))
					 editPhone2.setText(json.getString(phone.toString()));
				 if(phone.toString().equals("phone3"))
					 editPhone3.setText(json.getString(phone.toString()));
			 }
		 }
		 
		 if(UserManager.getUser().getTravelerPhone().startsWith("{")){
			 JSONObject json = JSONObject.fromObject(UserManager.getUser().getTravelerPhone());
			 for(Object phone: json.keySet()){
				 if(phone.toString().equals("phone1"))
					 editPhone1.setText(json.getString(phone.toString()));
				 if(phone.toString().equals("phone2"))
					 editPhone2.setText(json.getString(phone.toString()));
				 if(phone.toString().equals("phone3"))
					 editPhone3.setText(json.getString(phone.toString()));
			 }
		 }
	 }

	/**
	 * {"phone1":"0956666444","phone2":"0955444333","phone3":"077331234"}
	 * @return
	 */
	public String getPhoneNumberJSONObjectString(){
		JSONObject jsonObj = new JSONObject();
		if(editPhone1!=null && !editPhone1.getText().toString().equals(""))
			jsonObj.put("phone1", editPhone1.getText().toString());
		if(editPhone2!=null && !editPhone2.getText().toString().equals(""))
			jsonObj.put("phone2", editPhone2.getText().toString());
		if(editPhone3!=null && !editPhone3.getText().toString().equals(""))
			jsonObj.put("phone3", editPhone3.getText().toString());
		
		return jsonObj.toString();
	}
	 
	public String getRealName(){
		return editRealname.getText().toString();
	}
	 
	public boolean getGender() {
		return gender;
	}

	private boolean isFillPhoneNumber(){
		if(editPhone1.getText().toString().equals("")&&
			editPhone2.getText().toString().equals("")&&
				editPhone3.getText().toString().equals("")){
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean isDataValid() {
		if(!editRealname.getText().toString().equals("") && isFillPhoneNumber()){
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getDataInValidString() {
		if(editRealname.getText().toString().equals("")){
			return getResources().getString(R.string.tip_init_guide_profile_realname_empty);
		}else if(!isFillPhoneNumber()){
			return getResources().getString(R.string.tip_init_guide_profile_phone_empty);
		}
		return "";
	}
	
	class GotLicenseCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId){
			case R.id.init_guide_step4_radio_male:
				gender = true;
				break;
			case R.id.init_guide_step4_radio_female:
				gender = false;
				break;	
			}
		}
		
	}
}
