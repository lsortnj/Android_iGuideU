package mo.iguideu.ui.activate;

import mo.iguideu.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ActivityActivatePhone extends ActionBarActivity {
	private RelativeLayout				container 	= null;
	private RelativeLayout 				step1 		= null;
	private FragmentActivatePhoneStep2 	step2 		= new FragmentActivatePhoneStep2();
	
	private EditText editCountryCode = null;
	private EditText editPhoneNumber = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activate_phone);
		container = (RelativeLayout) findViewById(R.id.activate_phone_layout_container);
		step1     = (RelativeLayout) container.findViewById(R.id.activate_phone_layout_step1);
		
		editCountryCode = (EditText) step1.findViewById(R.id.activate_phone_step1_edit_country_code);
		editPhoneNumber = (EditText) step1.findViewById(R.id.activate_phone_step1_edit_phone);
		
		Button btnSend = (Button) step1.findViewById(R.id.activate_phone_step1_btn_send);
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				submitPhoneNuber();
			}
		});
		
		step2.getActivateButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				validActivateCode();
			}
		});
	}
	
	private void validActivateCode(){
		String activateCode = step2.getActivateCode();
		
	}
	
	private void submitPhoneNuber(){
		String phone = editPhoneNumber.getText().toString();
		if(!phone.equals("") && phone.length() > 8 ){
			
		}else
			Toast.makeText(getApplicationContext(), 
					getResources().getString(R.string.tip_wrong_phone_number_format), 
					Toast.LENGTH_LONG).show();
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
}
