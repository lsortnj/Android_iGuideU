package mo.iguideu.ui.createGuide;

import java.util.List;

import mo.iguideu.R;
import mo.iguideu.guide.AvailableType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerAvailabaleTypeAdapter extends BaseAdapter {

	private List<AvailableType> allAvailableType = null;
	private Context context = null;
	
	public SpinnerAvailabaleTypeAdapter(Context aContext,List<AvailableType> allType){
		context = aContext;
		allAvailableType = allType;
	}
	
	@Override
	public int getCount() {
		return allAvailableType.size();
	}

	@Override
	public Object getItem(int position) {
		return allAvailableType.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView!=null)
			return convertView;
		
		LayoutInflater _LayoutInflater=LayoutInflater.from(context);  
        convertView=_LayoutInflater.inflate(R.layout.available_type_spinner_item, null);  
		
        TextView showName = (TextView) convertView.findViewById(R.id.create_spinner_item_av_type_show_name);
        showName.setText(allAvailableType.get(position).getShowName());
        
		return convertView;
	}

}
