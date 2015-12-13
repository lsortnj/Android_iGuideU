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

public class DialogPositiveNegative extends Dialog
{
	private TextView		_tvTitle		= null;
	private TextView		_view_message	= null;
	private ImageView		_view_icon		= null;
	private Button			_positive_btn	= null;
	private Button			_negtive_btn	= null;
	
	
	
	public DialogPositiveNegative(Context context,
			String title,
			String message,
			View.OnClickListener positiveListener)
	{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_positive_negtive);
		
		//Initial view components
		_tvTitle		= (TextView) findViewById(R.id.dialog_p_n_txt_title);
		_view_icon 		= (ImageView) findViewById(R.id.dialog_p_n_img_icon);
		_view_message	= (TextView) findViewById(R.id.dialog_p_n_txt_message);
		_positive_btn	= (Button) findViewById(R.id.dialog_p_n_btn_positive);
		_negtive_btn	= (Button) findViewById(R.id.dialog_p_n_btn_negtive);
		
		if(title.equals(""))
			_tvTitle.setVisibility(View.GONE);
		else{
			_tvTitle.setText(title);
			_tvTitle.setVisibility(View.VISIBLE);
		}
		
		_view_message.setText(message);
		
		if(positiveListener!=null)
			_positive_btn.setOnClickListener(positiveListener);
		
		_negtive_btn.setOnClickListener
		(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dismiss();
				}
			}
		);
	}
	
	public void setTitle(String title){
		_tvTitle.setText(title);
		_tvTitle.setVisibility(View.VISIBLE);
	}
	
	public void setPositiveOnClickListener(View.OnClickListener listener)
	{
		_positive_btn.setOnClickListener(listener);
	}
	
	public void setPositiveButtonText(String positiveText){
		_positive_btn.setText(positiveText);
	}
	
	public void setNegtiveButtonText(String negtiveText){
		_negtive_btn.setText(negtiveText);
	}
	
	public void setNegtiveOnClickListener(View.OnClickListener listener)
	{
		_negtive_btn.setOnClickListener(listener);
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
