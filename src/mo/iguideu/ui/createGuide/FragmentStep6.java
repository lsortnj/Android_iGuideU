package mo.iguideu.ui.createGuide;

import mo.iguideu.R;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * Create new Guide step6: Set charge or free and finish
 * @author lsortnj
 *
 */
public class FragmentStep6 extends Fragment implements IStepValidateCheck{
	
	private EditText 		chargeFee 			= null;
	private RadioGroup 		radioGroupIsCharge 	= null;
	private RelativeLayout 	chargeInfoArea 		= null;
	
	private boolean isFreeGuide = true;
	
	private OnFinishListener listener = null;
	
	public FragmentStep6(){}
	
	public FragmentStep6(OnFinishListener listener){
		this.listener = listener;
	}
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_creat_step6, container, false);
		chargeFee = (EditText) v.findViewById(R.id.create_guide_step6_edit_fee);
		radioGroupIsCharge = (RadioGroup) v.findViewById(R.id.create_guide_step6_radio_group_is_charge);
		chargeInfoArea =  (RelativeLayout) v.findViewById(R.id.create_guide_step6_layout_charge_info);
		radioGroupIsCharge.setOnCheckedChangeListener(new ChargeCheckedChangeListener());
		Button btnFinish	= (Button) v.findViewById(R.id.create_guide_data_btn_finish);
		
		btnFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener!=null)
					listener.onFinish();
			}
		});
		
		//Init state
		chargeInfoArea.setVisibility(View.INVISIBLE);
		isFreeGuide = true;
		radioGroupIsCharge.check(R.id.create_guide_step6_radio_free);
		
		return v;
	 }

	 
	public double getChargeFee(){
		double fee = 0;
		
		try{
			fee = Double.parseDouble(chargeFee.getText().toString());
		}catch(Exception e){}
		
		return fee;
	}
	 
	public boolean isFreeGuide() {
		return isFreeGuide;
	}


	@Override
	public boolean isDataValid() {
		if(isFreeGuide){
			return true;
		}else if(!isFreeGuide && !chargeFee.getText().toString().equals("")){
			return true;
		}else if(!isFreeGuide && chargeFee.getText().toString().equals("")){
			return false;
		}else{
			return false;
		}
	}
	
	@Override
	public String getDataInValidString() {
		return getActivity().getString(R.string.tip_create_guide_charge_fee_empty);
	}
	
	class ChargeCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch(checkedId){
			case R.id.create_guide_step6_radio_charge:
				chargeInfoArea.setVisibility(View.VISIBLE);
				isFreeGuide = false;
				break;
			case R.id.create_guide_step6_radio_free:
				chargeInfoArea.setVisibility(View.INVISIBLE);
				isFreeGuide = true;
				break;	
			}
		}
		
	}
}
