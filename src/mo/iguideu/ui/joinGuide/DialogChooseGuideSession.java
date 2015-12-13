package mo.iguideu.ui.joinGuide;

import mo.iguideu.R;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class DialogChooseGuideSession extends Dialog{

	private JoinGuideListener listener = null;
	private SessionJoinStatus currentGuideSession = null;
	
	public DialogChooseGuideSession(Context context, JoinStatus joinStatus,JoinGuideListener aListener){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.dialog_choose_guide_session);
		
		listener = aListener;
		
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.dialog_choose_session_radio_group_guide_sessions);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				try{
					RadioButton sessionRadio = (RadioButton) findViewById(checkedId);
					currentGuideSession = (SessionJoinStatus) sessionRadio.getTag();
				}catch(Exception e){
					Log.e("DialogChooseGuideSession",e.toString());
				}
			}
		});
		
		if(joinStatus.getSessionStatues()!=null){
			for(SessionJoinStatus session: joinStatus.getSessionStatues()){
				if(session.getRemainCount()==0)
					continue;
				RadioButton sessionRadio = new RadioButton(context);
				sessionRadio.setText(session.getStartTime()+"~"+session.getEndTime()+"\t\t\t"+
						getContext().getResources().getString(R.string.text_remain)+session.getRemainCount());
				sessionRadio.setTag(session);
				radioGroup.addView(sessionRadio);
			}
		}
		
		Button btnDone = (Button) findViewById(R.id.dialog_choose_session_btn_done);
		btnDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentGuideSession==null){
					Toast.makeText(getContext(), 
							getContext().getResources().getString(R.string.tip_must_choose_guide_session), 
								Toast.LENGTH_LONG).show();
				}else{
					if(listener!=null){
						listener.onGuideSessionConfirm(currentGuideSession);
					}
					dismiss();
				}
			}
		});
	}
	
	public SessionJoinStatus getGuideSession() {
		return currentGuideSession;
	}

	public boolean isGuideSessionChoosed(){
		return currentGuideSession==null?false:true;
	}
}
