package mo.iguideu.ui.joinGuide;

import mo.iguideu.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class DialogJoinGuestCount extends Dialog implements OnClickListener{

	private ImageButton guestPlus  = null;
	private ImageButton guestMinus = null;
	private TextView 	guestCount = null;
	
	private int currentMaxGuest = 1;
	private int remainCount = 1;
	
	private JoinGuideListener joinListener = null;
	
	private boolean isConfirm = false;
	
	public DialogJoinGuestCount(Context context, int remainCount){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_join_guest);
		setCanceledOnTouchOutside(false);
		this.remainCount = remainCount;
		
		guestPlus 	= (ImageButton) findViewById(R.id.dialog_join_guest_plus);
		guestMinus 	= (ImageButton) findViewById(R.id.dialog_join_guest_minus);
		guestCount  = (TextView) findViewById(R.id.dialog_join_guest_txt_guest_count);
		Button btnOk = (Button) findViewById(R.id.dialog_join_guest_btn_done);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isConfirm = true;
				if(joinListener!=null)
					joinListener.onGuestCountConfirm(currentMaxGuest);
				dismiss();
			}
		});
		
		guestPlus.setOnClickListener(this);
		guestMinus.setOnClickListener(this);
		
		guestCount.setText("" + currentMaxGuest);
	}
	
	public boolean isConformGuestCount(){
		return isConfirm;
	}
	
	public int getGuestCount(){
		return currentMaxGuest;
	}
	
	public void setJoinListener(JoinGuideListener joinListener) {
		this.joinListener = joinListener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_join_guest_plus:
			currentMaxGuest++;
			if (currentMaxGuest >= remainCount)
				currentMaxGuest = remainCount;
			break;
		case R.id.dialog_join_guest_minus:
			currentMaxGuest--;
			if (currentMaxGuest <= 1)
				currentMaxGuest = 1;
			break;
		}

		guestCount.setText("" + currentMaxGuest);
	}
}
