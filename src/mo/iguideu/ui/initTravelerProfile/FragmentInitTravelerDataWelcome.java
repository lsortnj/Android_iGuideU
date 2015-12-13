package mo.iguideu.ui.initTravelerProfile;

import mo.iguideu.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentInitTravelerDataWelcome extends Fragment{
	
	private InitTravelerProfileListener listener = null;
	
	
	public void setInitTravelerProfileLisnter(InitTravelerProfileListener listener){
		this.listener=listener;
	}
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_init_traveler_data_welcome, container, false);
		Button btnContinue = (Button) v.findViewById(R.id.init_traveler_data_btn_continue);
		Button btnCancel   = (Button) v.findViewById(R.id.init_traveler_data_btn_cancel);
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		
		btnContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener!=null)
					listener.onStartCreateTravelerProfile();
			}
		});
		
		return v;
	 }

	 
}
