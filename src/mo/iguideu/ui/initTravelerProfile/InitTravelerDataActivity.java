package mo.iguideu.ui.initTravelerProfile;

import mo.iguideu.IGuideUActivity;
import mo.iguideu.R;
import mo.iguideu.ui.base.DialogPositiveNegative;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class InitTravelerDataActivity extends ActionBarActivity implements InitTravelerProfileListener{

	private static final int	TOTAL_STEPS = 5;
	
	private FragmentManager fragmentManager;
	private FragmentInitTravelerDataWelcome welcome = new FragmentInitTravelerDataWelcome();
	private FragmentInitTravelerDataStep1 step1 = new FragmentInitTravelerDataStep1();
	
	private int currentStep		= 0;
	
	private MenuItem menuNext = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init_traveler_data);
		
		fragmentManager = getSupportFragmentManager();
		
		welcome.setInitTravelerProfileLisnter(this);
		
		fragmentManager
			.beginTransaction()
				.replace(R.id.activity_traveler_guide_container,welcome).commit();
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
	
	public void startInitTravelerData(){
		currentStep++;
		
		fragmentManager
		.beginTransaction()
			.replace(R.id.activity_traveler_guide_container,step1).commit();
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
							.replace(R.id.activity_traveler_guide_container,step1).commit();
					
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
				if(!step1.isDataValid()){
					currentStep--;
					fragmentManager
						.beginTransaction()
							.replace(R.id.activity_init_guide_data_container,welcome).commit();
				}else{
					Toast.makeText(getApplicationContext(), step1.getDataInValidString(), Toast.LENGTH_LONG).show();
				}
				break;
		}
		if(currentStep <= 1){
			currentStep = 1;
		}
		if(currentStep < TOTAL_STEPS)
			menuNext.setVisible(true);
	}

	@Override
	public void onStartCreateTravelerProfile() {
		currentStep = 1;
		fragmentManager
			.beginTransaction()
				.replace(R.id.activity_init_guide_data_container,step1).commit();
	}

	@Override
	public void onFinishCreateTravelerProfile() {
		
	}
	
}
