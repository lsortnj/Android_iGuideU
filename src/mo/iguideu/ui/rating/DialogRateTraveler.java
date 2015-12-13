package mo.iguideu.ui.rating;

import java.util.ArrayList;
import java.util.List;

import mo.iguideu.R;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.util.MoProgressDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class DialogRateTraveler extends Dialog {

	private RatingBar rateBar   	= null;
	private EditText  editComment   = null;
	private Handler	  handler		= null;
	private List<Integer> guestIDs = new ArrayList<Integer>();
	
	private int 	guideID 	= 0;
	private String 	targetDate 	= "";
	
	public DialogRateTraveler(
			Context context, 
			List<Integer> guestIDs,
			String targetDate, 
			int guideID, 
			boolean isRateAll){
		
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_rate);
		
		this.guestIDs 		= guestIDs;
		this.guideID  		= guideID;
		this.targetDate 	= targetDate;
		
		TextView  tvTitle   = (TextView) findViewById(R.id.dialog_rate_guide_txt_title);
		Button    btnOk     = (Button) findViewById(R.id.dialog_rate_guide_btn_ok);
		Button    btnCancel = (Button) findViewById(R.id.dialog_rate_guide_btn_cancel);
		
		rateBar   	= (RatingBar) findViewById(R.id.dialog_rate_guide_rate_bar);
		editComment = (EditText) findViewById(R.id.dialog_rate_guide_edit_comment);
		
		tvTitle.setText(isRateAll?
				getContext().getResources().getString(R.string.title_rate_all_traveler):
				 getContext().getResources().getString(R.string.title_rate_traveler,guestIDs.size()));
		
		ButtonOnClickListener listener = new ButtonOnClickListener();
		btnOk.setOnClickListener(listener);
		btnCancel.setOnClickListener(listener);
		
		rateBar.setRating(1);
		rateBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				if(rating<1)
					rating = 1;
			}
		});
	}
	
	public void setHandler(Handler handler){
		this.handler=handler;
	}
	
	private void doRate(){
		TravelerRating rating = new TravelerRating();
		rating.setTravelerIDs(guestIDs);
		rating.setComment(editComment.getText().toString());
		rating.setGuideId(guideID);
		rating.setTargetDate(targetDate);
		rating.setRating(rateBar.getRating());
		
		MoProgressDialog.showProgress(getContext(),"", getContext().getResources().getString(R.string.tip_rating_please_wait), false);
		ServerCore.instance().doRateTravelers(rating, handler);
		dismiss();
	}
	
	class ButtonOnClickListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.dialog_rate_guide_btn_ok:
				doRate();
				break;
			case R.id.dialog_rate_guide_btn_cancel:
				dismiss();
				break;
			}
		}
	}
}
