package mo.iguideu.ui.createGuide;

import mo.iguideu.IGuideUActivity;
import mo.iguideu.R;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.ui.base.DialogPositiveNegative;
import mo.iguideu.ui.base.DialogSingleButton;
import mo.iguideu.util.NetworkUtil;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ActivityCreateGuide extends ActionBarActivity implements OnFinishListener{

	private static final int TOTAL_STEPS = 7;
	
	private GuideEvent guide = null;
	
	private int currentStep = 0;
	
	private FragmentStep1 		fragmentStep1 		= new FragmentStep1();
	private FragmentStep1Next 	fragmentStep1Next 	= new FragmentStep1Next();
	private FragmentStep2New 	fragmentStep2new 	= new FragmentStep2New();
	private FragmentStep3 		fragmentStep3 		= new FragmentStep3();
	private FragmentStep4 		fragmentStep4 		= new FragmentStep4();
	private FragmentStep5 		fragmentStep5 		= new FragmentStep5();
	private FragmentStep6 		fragmentStep6 		= new FragmentStep6(this);
	
	private ProgressDialog progressDialog = null;
	 
	private MenuItem menuNext = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_guide);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		guide = new GuideEvent();
		
//		fragmentStep2.setGuideEvent(guide);
		currentStep = 1;
		toFirstStep();
	}
	
	private void toFirstStep(){
        getSupportFragmentManager().beginTransaction()
        	.add(R.id.activity_create_guide_container, fragmentStep1).commit();
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
		    	if(!fragmentStep1.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep1.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep++;
					guide.setGuideName(fragmentStep1.getGuideName());
					guide.setMaxPeople(fragmentStep1.getMaxPeople());
					
					getSupportFragmentManager().beginTransaction()
		        		.replace(R.id.activity_create_guide_container, fragmentStep1Next)
		        		.addToBackStack(null)
						.commit();
					
				}
		    	break;
			case 3:
				if(!fragmentStep1Next.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep1.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep++;
					guide.setDuration(fragmentStep1Next.getDuration());
					guide.setAllDayGuide(fragmentStep1Next.isAllDayGuide());
					guide.setGuideSessions(fragmentStep1Next.getGuideSessions());
					
					getSupportFragmentManager().beginTransaction()
		        		.replace(R.id.activity_create_guide_container, fragmentStep2new)
		        		.addToBackStack(null)
						.commit();
					
					fragmentStep2new.setAvailableDateStrings(guide.getAvailableDateArray());
				}
				break;
			case 4:
				if(!fragmentStep2new.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep2new.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep++;
					
					//TODO Add All Date to Guide
					guide.setAvailableDateArray(fragmentStep2new.getAvailableDateString());
	
					getSupportFragmentManager().beginTransaction()
		        		.replace(R.id.activity_create_guide_container, fragmentStep3)
		        		.addToBackStack(null)
						.commit();
					
				}
				
				break;
				
			case 5:
				if(!fragmentStep3.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep3.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep++;
					guide.setSchedule(fragmentStep3.getSchedule());
					getSupportFragmentManager().beginTransaction()
		        		.replace(R.id.activity_create_guide_container, fragmentStep4)
		        		.addToBackStack(null)
						.commit();
				}
				
				break;
			case 6:
				if(!fragmentStep4.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep4.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep++;
					guide.setPhotos(fragmentStep4.getAllPhotoPath());
					getSupportFragmentManager().beginTransaction()
						.replace(R.id.activity_create_guide_container, fragmentStep5)
						.addToBackStack(null)
						.commit();
				}
				
				break;
			case 7:
				if(!fragmentStep5.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep5.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep++;
					guide.setMeetLocation(fragmentStep5.getMeetLocation());
					getSupportFragmentManager().beginTransaction()
						.replace(R.id.activity_create_guide_container, fragmentStep6)
						.addToBackStack(null)
						.commit();
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
				if(!fragmentStep1Next.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep1Next.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep--;
					guide.setDuration(fragmentStep1Next.getDuration());
					guide.setAllDayGuide(fragmentStep1Next.isAllDayGuide());
					guide.setGuideSessions(fragmentStep1Next.getGuideSessions());
					
					getSupportFragmentManager().beginTransaction()
		        		.replace(R.id.activity_create_guide_container, fragmentStep1)
		        		.addToBackStack(null)
						.commit();
					
				}
				break;
			case 2:
				if(!fragmentStep2new.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep2new.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep--;
					
					guide.setAvailableDateArray(fragmentStep2new.getAvailableDateString());
					
					getSupportFragmentManager().beginTransaction()
		        		.replace(R.id.activity_create_guide_container, fragmentStep1Next)
		        		.addToBackStack(null)
						.commit();
					
				}
				break;
				
			case 3:
				if(!fragmentStep3.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep3.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep--;
					guide.setSchedule(fragmentStep3.getSchedule());
					 
					getSupportFragmentManager().beginTransaction()
		        		.replace(R.id.activity_create_guide_container, fragmentStep2new)
		        		.addToBackStack(null)
						.commit();
					
				}
				break;
				
			case 4:
				if(!fragmentStep4.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep4.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep--;
					guide.setPhotos(fragmentStep4.getAllPhotoPath());
					getSupportFragmentManager().beginTransaction()
		        		.replace(R.id.activity_create_guide_container, fragmentStep3)
		        		.addToBackStack(null)
						.commit();
				}
				break;	
				
			case 5:
				if(!fragmentStep5.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep5.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep--;
					guide.setMeetLocation(fragmentStep5.getMeetLocation());
					getSupportFragmentManager().beginTransaction()
						.replace(R.id.activity_create_guide_container, fragmentStep4)
						.addToBackStack(null)
						.commit();
				}
				break;	
			case 6:
				if(!fragmentStep6.isDataValid()){
					Toast.makeText(getBaseContext(), fragmentStep6.getDataInValidString(), Toast.LENGTH_SHORT).show();
				}else{
					currentStep--;
					if(!fragmentStep6.isFreeGuide())
						guide.setFee(fragmentStep6.getChargeFee());
					getSupportFragmentManager().beginTransaction()
						.replace(R.id.activity_create_guide_container, fragmentStep5)
						.addToBackStack(null)
						.commit();
				}
				break;	
				
		}
		
		if(currentStep <= 1)
			currentStep = 1;
		
		if(currentStep < TOTAL_STEPS)
			menuNext.setVisible(true);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.create_guide, menu);
		menuNext = menu.findItem(R.id.next_step);
		
		return super.onCreateOptionsMenu(menu);
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
	
	private Handler progressHandler = new Handler(){
		public void handleMessage(Message msg){
			if(progressDialog!=null && progressDialog.isShowing())
				progressDialog.dismiss();
			progressDialog = null;
			DialogSingleButton dialog = new DialogSingleButton(ActivityCreateGuide.this);
			switch(msg.what){
				case ServerInfo.CREATE_GUIDE_SUCCESS:
					dialog.setMessage(getResources().getString(R.string.tip_create_guide_success));
					dialog.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							finish();
						}
					});
					break;
				case ServerInfo.CREATE_GUIDE_FAILED:
					dialog.setMessage(getResources().getString(R.string.tip_create_guide_failed));
					break;
				case NetworkUtil.NO_CONNECTION_AVAILABLE:
					dialog.setMessage(getResources().getString(R.string.tip_no_connection));
					break;
			};
			dialog.show();
		}
	};


	@Override
	public void onFinish() {
		if(!fragmentStep6.isDataValid()){
			Toast.makeText(getBaseContext(), fragmentStep6.getDataInValidString(), Toast.LENGTH_SHORT).show();
		}else{
			if(!fragmentStep6.isFreeGuide())
				guide.setFee(fragmentStep6.getChargeFee());
			
			if(NetworkUtil.hasConnectionExist()){
				progressDialog = new ProgressDialog(ActivityCreateGuide.this);
				progressDialog.setTitle(getResources().getString(R.string.title_wait));
				progressDialog.setMessage(getResources().getString(R.string.progress_creating_guide));
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setProgress(0);
				progressDialog.setMax(100);
				progressDialog.show();
				ServerCore.instance().createNewGuide(guide,progressHandler,progressDialog);
			}else{
				progressHandler.sendEmptyMessage(NetworkUtil.NO_CONNECTION_AVAILABLE);
			}
		}
	}
	
	
}
