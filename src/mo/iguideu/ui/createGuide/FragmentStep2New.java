package mo.iguideu.ui.createGuide;

import java.util.ArrayList;
import java.util.List;

import com.wt.calendarcard.CalendarCardPager;
import com.wt.calendarcard.CardGridItem;
import com.wt.calendarcard.OnCellItemClick;

import mo.iguideu.R;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentStep2New extends Fragment implements OnCellItemClick,
		IStepValidateCheck {

	private CalendarCardPager calendarCard = null;
	private List<String> selectDateStrings = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_create_step2_new,
				container, false);
		calendarCard = (CalendarCardPager) v
				.findViewById(R.id.create_guide_step2_new_calendar_card);
		calendarCard.setOnCellItemClick(this);
		calendarCard.setAvailableDate(selectDateStrings);
		calendarCard.notifyDataChanged();
		return v;
	}

	public void setAvailableDateStrings(List<String> selectDateStrings) {
		this.selectDateStrings = selectDateStrings;
	}

	public List<String> getAvailableDateString() {
		return selectDateStrings;
	}

	@Override
	public void onCellSelect(View v, CardGridItem item) {
		selectDateStrings.add(item.getDateString());
	}

	@Override
	public void onCellUnselect(View v, CardGridItem item) {
		if (selectDateStrings.contains(item.getDateString()))
			selectDateStrings.remove(item.getDateString());
	}

	@Override
	public void onCellClick(CardGridItem item, boolean isSelected) {

	}

	@Override
	public boolean isDataValid() {
		return (selectDateStrings.size() == 0) ? false : true;
	}

	@Override
	public String getDataInValidString() {
		return getResources()
				.getString(R.string.tip_create_guide_date_unselect);
	}

}
