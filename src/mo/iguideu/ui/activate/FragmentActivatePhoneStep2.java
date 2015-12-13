package mo.iguideu.ui.activate;

import mo.iguideu.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentActivatePhoneStep2 extends Fragment{
	
	private EditText activateCode = null;
	private Button   btnActivate  = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_activate_phone_step2, container,false);
		activateCode = (EditText) v.findViewById(R.id.activate_phone_step2_edit_activate_code);
		btnActivate  = (Button) v.findViewById(R.id.activate_phone_step2_btn_active);
		return v;
	}
	
	public String getActivateCode(){
		return activateCode.getText().toString();
	}
	
	public Button getActivateButton(){
		return btnActivate;
	}
}
