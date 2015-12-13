package mo.iguideu.ui.initGuideProfile;

import java.util.ArrayList;
import java.util.List;

import mo.iguideu.R;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class FragmentInitGuideDataStep3 extends Fragment implements
		IStepValidateCheck {

	private CheckBox chkCar 		= null;
	private CheckBox chkMoto 		= null;
	private CheckBox chkBike 		= null;
	private CheckBox chkBus 		= null;
	private CheckBox chkTrain 		= null;
	private CheckBox chkTaxi 		= null;
	private CheckBox chkPlane 		= null;
	private CheckBox chkBoat 		= null;
	private CheckBox chkElse 		= null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_init_guide_data_step3,
				container, false);
		chkCar = (CheckBox) v
				.findViewById(R.id.trans_chk_car);
		chkMoto = (CheckBox) v
				.findViewById(R.id.trans_chk_moto);
		chkBike = (CheckBox) v
				.findViewById(R.id.trans_chk_bike);
		chkBus = (CheckBox) v
				.findViewById(R.id.trans_chk_bus);
		chkTrain = (CheckBox) v
				.findViewById(R.id.trans_chk_train);
		chkTaxi = (CheckBox) v
				.findViewById(R.id.trans_chk_taxi);
		chkPlane = (CheckBox) v
				.findViewById(R.id.trans_chk_plane);
		chkBoat = (CheckBox) v
				.findViewById(R.id.trans_chk_boat);
		chkElse = (CheckBox) v
				.findViewById(R.id.trans_chk_else);
		
		return v;
	}
	
	public List<String> getTranspotationType(){
		List<String> transTypes = new ArrayList<String>();
		
		if(chkCar.isChecked())
			transTypes.add(chkCar.getTag().toString());
		if(chkMoto.isChecked())
			transTypes.add(chkMoto.getTag().toString());
		if(chkBike.isChecked())
			transTypes.add(chkBike.getTag().toString());
		if(chkBus.isChecked())
			transTypes.add(chkBus.getTag().toString());
		if(chkTrain.isChecked())
			transTypes.add(chkTrain.getTag().toString());
		if(chkTaxi.isChecked())
			transTypes.add(chkTaxi.getTag().toString());
		if(chkPlane.isChecked())
			transTypes.add(chkPlane.getTag().toString());
		if(chkBoat.isChecked())
			transTypes.add(chkBoat.getTag().toString());
		if(chkElse.isChecked())
			transTypes.add(chkElse.getTag().toString());
		
		return transTypes;
	}

	@Override
	public boolean isDataValid() {
		return getTranspotationType().size()==0?false:true;
	}

	@Override
	public String getDataInValidString() {
		return getResources().getString(R.string.error_init_guide_data_at_least_choose_one_trans_type);
	}

}
