package mo.iguideu.ui.rating;

import mo.iguideu.R;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.user.UserManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class DialogRateGuide extends Dialog {

	private RatingBar rateBar   	= null;
	private EditText  editComment   = null;
	private GuideRating guideRate   = new GuideRating();
	
	public DialogRateGuide(Context context, GuideEvent guide){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_rate);
		
		TextView  tvTitle   = (TextView) findViewById(R.id.dialog_rate_guide_txt_title);
		Button    btnOk     = (Button) findViewById(R.id.dialog_rate_guide_btn_ok);
		Button    btnCancel = (Button) findViewById(R.id.dialog_rate_guide_btn_cancel);
		
		rateBar   	= (RatingBar) findViewById(R.id.dialog_rate_guide_rate_bar);
		editComment = (EditText) findViewById(R.id.dialog_rate_guide_edit_comment);
		
		tvTitle.setText(guide.getGuideName());
		
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
		
		guideRate.setGuideId(guide.getId());
		guideRate.setVoterUserId(UserManager.getUser().getId());
	}
	
	private Handler rateHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ServerInfo.DO_RATE_GUIDE_DONE:
				Toast.makeText(getContext(), 
						getContext().getResources().getString(R.string.tip_rating_guide_done), 
							Toast.LENGTH_SHORT).show();
				dismiss();
				break;
			default:
				Toast.makeText(getContext(), 
						getContext().getResources().getString(R.string.tip_rating_guide_failed), 
							Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	class ButtonOnClickListener implements android.view.View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.dialog_rate_guide_btn_ok:
				guideRate.setComment(editComment.getText().toString());
				guideRate.setRating(rateBar.getRating());
				ServerCore.instance().doRateGuide(guideRate, rateHandler);
				break;
			case R.id.dialog_rate_guide_btn_cancel:
				dismiss();
				break;
			}
		}
	}
}
