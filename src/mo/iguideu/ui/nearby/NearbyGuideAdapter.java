package mo.iguideu.ui.nearby;
import java.util.List;

import com.koushikdutta.ion.Ion;

import mo.iguideu.R;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.ui.base.ActivityGuideDetail;
import mo.iguideu.util.GuideUtil;
import mo.iguideu.util.MoAnimationUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyGuideAdapter extends ArrayAdapter<GuideEvent> {

	public NearbyGuideAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}

	public NearbyGuideAdapter(Context context, int resource, List<GuideEvent> items) {
	    super(context, resource, items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderItem viewHolder;
		
		ImageView 	img 	 = null;
		TextView 	name 	 = null;
		TextView 	location = null;
		TextView 	fee		 = null;
		TextView 	joinCount= null;

	    if (convertView == null) {
	      	LayoutInflater inflator = LayoutInflater.from(getContext());
	      	convertView = inflator.inflate(R.layout.card_my_guide, null);
	      	
	    	
	    	name 	 = (TextView) convertView.findViewById(R.id.card_my_guide_txt_name);
	    	location = (TextView) convertView.findViewById(R.id.card_my_guide_txt_location);
	    	fee 	 = (TextView) convertView.findViewById(R.id.card_my_guide_txt_fee);
	    	joinCount= (TextView) convertView.findViewById(R.id.card_my_guide_txt_join_count);
	    	img = (ImageView) convertView.findViewById(R.id.card_my_guide_img_cover);
	    	
	    	viewHolder = new ViewHolderItem();
	    	viewHolder.imgCover 	= img;
	    	viewHolder.tvGuideName 	= name;
	    	viewHolder.tvLocation 	= location;
	    	viewHolder.tvFee 		= fee;
	    	viewHolder.tvJoinCount 	= joinCount;
	    	convertView.setTag(viewHolder);
	    }else{
	    	viewHolder = (ViewHolderItem) convertView.getTag();
	    }
	    
	    convertView.setOnClickListener(new GuideOnClickListener(getItem(position)));
	    
	    viewHolder.tvGuideName.setText(getItem(position).getGuideName());
	    viewHolder.tvGuideName.setTypeface(null, Typeface.BOLD);
	    
	    viewHolder.tvLocation.setText(
	    		String.format("%.1f", getItem(position).getDistance())+" "+
	    			getContext().getResources().getString(R.string.unit_km));
	    viewHolder.tvFee.setText(
	    		getItem(position).getFee()==0?
	    				getContext().getResources().getString(R.string.free):
	    					""+getItem(position).getFee());
	    viewHolder.tvJoinCount.setText(""+getItem(position).getJoinCount());
	    
	    viewHolder.imgCover.setImageBitmap(null);
	    
	    try{
	    	Ion.with(viewHolder.imgCover)
	        .placeholder(R.drawable.placeholder)
	        .error(R.drawable.error)
	        .animateIn(MoAnimationUtil.getFadeInAnimation(500))
	        .load(getItem(position).getPhotos().get(0));
	    }catch(Exception e){}
	    
		return convertView;
	}
	
	class GuideOnClickListener implements OnClickListener{

		private GuideEvent guide;
		
		public GuideOnClickListener(GuideEvent guide){
			this.guide = guide;
		}
		
		@Override
		public void onClick(View v) {
			
			GuideUtil.setTargetGuide(guide);
			
			Intent intent = new Intent();
			intent.setClass(getContext(), ActivityGuideDetail.class);
			getContext().startActivity(intent);
		}
		
	}
	
	static class ViewHolderItem {
	    TextView tvGuideName;
	    TextView tvLocation;
	    TextView tvFee;
	    TextView tvJoinCount;
	    ImageView imgCover;
	    
	}
}
