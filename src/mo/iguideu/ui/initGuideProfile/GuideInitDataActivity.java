package mo.iguideu.ui.initGuideProfile;

import mo.iguideu.IGuideUActivity;
import mo.iguideu.R;
import mo.iguideu.ui.base.DialogPositiveNegative;
import mo.iguideu.user.UserManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GuideInitDataActivity extends ActionBarActivity {

	private static final int	TOTAL_STEPS = 6;
	
	private FragmentManager fragmentManager;
	private FragmentInitGuideDataStep1 step1 = new FragmentInitGuideDataStep1();
	private FragmentInitGuideDataStep2 step2 = new FragmentInitGuideDataStep2();
	private FragmentInitGuideDataStep3 step3 = new FragmentInitGuideDataStep3();
	private FragmentInitGuideDataStep4 step4 = new FragmentInitGuideDataStep4();
	private FragmentInitGuideDataStep5 step5 = new FragmentInitGuideDataStep5();
	private FragmentInitGuideDataFinish stepFinish = new FragmentInitGuideDataFinish();
	
	
	
	private int			   currentStep		= 0;
	private RelativeLayout welcomeView      = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_init_data);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Button btnContinue = (Button)findViewById(R.id.init_guide_data_btn_continue);
		Button btnCancel   = (Button)findViewById(R.id.init_guide_data_btn_cancel);
		
		welcomeView = (RelativeLayout) findViewById(R.id.activity_init_guide_data_welcome_area);
		
		fragmentManager = getSupportFragmentManager();
		
		btnContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fragmentManager
					.beginTransaction()
						.replace(R.id.activity_init_guide_data_container,step1).commit();
				menuNext.setVisible(true);
				welcomeView.setVisibility(View.GONE);
				currentStep = 1;
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(IGuideUActivity.RESULT_CODE_INIT_GUIDE_CANCEL,null);
				finish();
			}
		});
	}
	
	private MenuItem menuNext = null;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.init_guide_data, menu);
		menuNext = menu.findItem(R.id.next_step);
		menuNext.setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	    case android.R.id.home:
	    	if(currentStep == 1)
	    		showExitConfirm();
	    	else
	    		toPreStep();
	        return true;
	    case R.id.next_step:
	    		toNextStep();
	    	break;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	private void showExitConfirm(){
		DialogPositiveNegative dialog = new DialogPositiveNegative(
				this,
				"",
				getResources().getString(R.string.tip_before_exit_init_guide_data),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(IGuideUActivity.RESULT_CODE_INIT_GUIDE_CANCEL,null);
						finish();
					}
			});
			
			dialog.show();
	}
	
	@Override
	public void onBackPressed(){
		if(currentStep == 1){
			showExitConfirm();
		}else
			toPreStep(); 
	}
	
	private void toNextStep(){
		
		switch(currentStep+1){
			case 2:
				if(!step1.isDataValid()){
					Toast.makeText(getApplicationContext(), step1.getDataInValidString(), Toast.LENGTH_LONG).show();
				}else{
					//to step2
					currentStep++;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,step2).commit();
					
					//update license data
					UserManager.getUser().isGotLicense(step1.isGotLicense());
					UserManager.getUser().setLicenseCode(step1.getLicenseNo());
				}
				break;
			case 3:
				if(step2.isDataValid()){
					currentStep++;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,step3).commit();
					
					//update support language
					UserManager.getUser().setSupportLanguage(step2.getSupportLanguages());
				}else{
					Toast.makeText(getApplicationContext(), step2.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
			case 4:
				if(step3.isDataValid()){
					currentStep++;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,step4).commit();
					
					//update transportation type
					UserManager.getUser().setOfferTransportationType(step3.getTranspotationType());
					
					//Finish init guide data
					UserManager.getUser().isInitGuiderProfile(true);
					
					
				}else{
					Toast.makeText(getApplicationContext(), step3.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
			case 5:
				if(step4.isDataValid()){
					currentStep++;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,step5).commit();
					
					//update real name
					UserManager.getUser().setName(step4.getRealName());
					UserManager.getUser().setGender(step4.getGender());
					UserManager.getUser().setPhoneNumberJSON(step4.getPhoneNumberJSONObjectString());
				}else{
					Toast.makeText(getApplicationContext(), step4.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
			case 6:
				if(step5.isDataValid()){
					currentStep++;
					fragmentManager
					.beginTransaction()
					.replace(R.id.activity_init_guide_data_container,stepFinish).commit();
					
					//update introduction
					UserManager.getUser().setIntro(step5.getIntroduction());
				}else{
					Toast.makeText(getApplicationContext(), step5.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
		}
		
		
		if(currentStep >= TOTAL_STEPS){
			currentStep = TOTAL_STEPS;
			menuNext.setVisible(false);
		}
	}
	
	private void toPreStep(){
		
		switch(currentStep-1){
			case 1:
				if(!step2.isDataValid()){
					currentStep--;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,step1).commit();
				}else{
					Toast.makeText(getApplicationContext(), step2.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
			case 2:
				if(step3.isDataValid()){
					currentStep--;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,step2).commit();
				}else{
					Toast.makeText(getApplicationContext(), step3.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
			case 3:
				if(step4.isDataValid()){
					currentStep--;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,step3).commit();
				}else{
					Toast.makeText(getApplicationContext(), step4.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
			case 4:
				if(step5.isDataValid()){
					currentStep--;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,step4).commit();
				}else{
					Toast.makeText(getApplicationContext(), step5.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
			case 5:
				currentStep--;
				fragmentManager
					.beginTransaction()
						.replace(R.id.activity_init_guide_data_container,step5).commit();
				break;
		}
		
		if(currentStep <= 1){
			currentStep = 1;
		}
		if(currentStep < TOTAL_STEPS)
			menuNext.setVisible(true);
	}
	
}
