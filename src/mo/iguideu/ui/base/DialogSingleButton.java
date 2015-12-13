package mo.iguideu.ui.base;

import mo.iguideu.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogSingleButton extends Dialog
{
	public static final String 	DEFAULT_BUTTON_TEXT	= "OK";
	
	private ImageView		_view_icon		= null;
	private TextView		_view_message	= null;
	private Button			_btn			= null;
	private TextView		_tv_title		= null;
	
	public DialogSingleButton(Context context, String message)
	{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_single_button);
		
		_tv_title	  = (TextView) findViewById(R.id.dialog_single_btn_txt_title);
		_view_icon 	  = (ImageView) findViewById(R.id.dialog_single_btn_img_icon);
		_view_message = (TextView) findViewById(R.id.dialog_single_btn_txt_message);
		_btn          = (Button) findViewById(R.id.dialog_single_btn_btn_done);
		
		_btn.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					dismiss();
				}
			}
		);
		
		_tv_title.setVisibility(View.GONE);
		
		setMessage(message);
	}
	
	public DialogSingleButton(Context context){
		this(context,"");
	}
	
	public void setTitle(String title){
		_tv_title.setText(title);
		_tv_title.setVisibility(View.VISIBLE);
	}
	
	public void setButtonOnClickListener(View.OnClickListener listener)
	{
		_btn.setOnClickListener(listener);
	}
	
	public void setIcon(Bitmap icon)
	{
		_view_icon.setImageBitmap(icon);
	}
	
	public void setMessage(String message)
	{
		_view_message.setText(message);
	}
	public void isHideIcon(boolean isHideIcon){
		if(isHideIcon)
			_view_icon.setVisibility(View.GONE);
		else
			_view_icon.setVisibility(View.VISIBLE);
	}
}
