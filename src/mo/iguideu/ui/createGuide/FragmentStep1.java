package mo.iguideu.ui.createGuide;


import mo.iguideu.R;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Create new Guide step1: 
 * 	Guide name 
 *  Max people
 *  Guide Type: All Day or By Session
 * 
 * @author lsortnj
 * 
 */
public class FragmentStep1 extends Fragment implements IStepValidateCheck,
		OnClickListener {

	private EditText guideName = null;

	private TextView maxPeople = null;

	private ImageButton maxGuestPlus = null;
	private ImageButton maxGuestMinus = null;

	private int currentMaxGuest = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_creat_step1, container,
				false);
		guideName = (EditText) v.findViewById(R.id.create_guide_guide_name);
		maxPeople = (TextView) v.findViewById(R.id.create_guide_step1_tv_max_people);

		maxGuestPlus = (ImageButton) v.findViewById(R.id.create_guide_btn_max_guest_plus);
		maxGuestMinus = (ImageButton) v.findViewById(R.id.create_guide_btn_max_guest_minus);

		maxGuestPlus.setOnClickListener(this);
		maxGuestMinus.setOnClickListener(this);

		maxPeople.setText("" + currentMaxGuest);
		
		return v;
	}

	@Override
	public boolean isDataValid() {

		if (guideName.getText().toString().equals("")
				|| maxPeople.getText().toString().equals("")
				|| Integer.parseInt(maxPeople.getText().toString()) <= 0)
			return false;
	
		return true;
	}

	public String getGuideName() {
		if (guideName != null)
			return guideName.getText().toString();

		return "";
	}

	public int getMaxPeople() {
		try {
			if (maxPeople != null) {
				return Integer.parseInt(maxPeople.getText().toString());
			}
		} catch (Exception e) {
		}

		return GuideEvent.MAX_PEOPLE;
	}

	@Override
	public String getDataInValidString() {
		String invalidString = "";

		if (guideName.getText().toString().equals(""))
			invalidString = getActivity().getString(
					R.string.error_guide_name_empty);
		if (maxPeople.getText().toString().equals(""))
			invalidString = getActivity().getString(
					R.string.tip_create_guide_max_people_empty);

		return invalidString;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.create_guide_btn_max_guest_plus:
			currentMaxGuest++;
			if (currentMaxGuest >= 999)
				currentMaxGuest = 999;
			break;
		case R.id.create_guide_btn_max_guest_minus:
			currentMaxGuest--;
			if (currentMaxGuest <= 1)
				currentMaxGuest = 1;
			break;
		}

		maxPeople.setText("" + currentMaxGuest);
	}
}
