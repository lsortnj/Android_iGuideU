package mo.iguideu.ui.main;

import java.util.List;
import java.util.Locale;
import com.koushikdutta.ion.Ion;

import mo.iguideu.R;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.store.AppPreference;
import mo.iguideu.ui.base.ActivityGuideDetail;
import mo.iguideu.util.GuideUtil;
import mo.iguideu.util.ImageUtil;
import mo.iguideu.util.MoAnimationUtil;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideAdapter extends ArrayAdapter<GuideEvent> {

	public GuideAdapter(Context context, int textViewResourceId) {
	    super(context, textViewResourceId);
	}

	public GuideAdapter(Context context, int resource, List<GuideEvent> items) {
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
		TextView 	highlight= null;

	    if (convertView == null) {
	      	LayoutInflater inflator = LayoutInflater.from(getContext());
	      	convertView = inflator.inflate(R.layout.card_my_guide, null);
	      	
	    	
	    	name 	 = (TextView) convertView.findViewById(R.id.card_my_guide_txt_name);
	    	location = (TextView) convertView.findViewById(R.id.card_my_guide_txt_location);
	    	fee 	 = (TextView) convertView.findViewById(R.id.card_my_guide_txt_fee);
	    	joinCount= (TextView) convertView.findViewById(R.id.card_my_guide_txt_join_count);
	    	highlight= (TextView) convertView.findViewById(R.id.card_my_guide_txt_highlight_text);
	    	img = (ImageView) convertView.findViewById(R.id.card_my_guide_img_cover);
	    	
	    	viewHolder = new ViewHolderItem();
	    	viewHolder.imgCover 	= img;
	    	viewHolder.tvGuideName 	= name;
	    	viewHolder.tvLocation 	= location;
	    	viewHolder.tvFee 		= fee;
	    	viewHolder.tvJoinCount 	= joinCount;
	    	viewHolder.tvHighlight  = highlight;
	    	convertView.setTag(viewHolder);
	    }else{
	    	viewHolder = (ViewHolderItem) convertView.getTag();
	    }
	    
	    convertView.setOnClickListener(new GuideOnClickListener(getItem(position)));
	    
	    viewHolder.tvGuideName.setText(getItem(position).getGuideName());
	    viewHolder.tvGuideName.setTypeface(null, Typeface.BOLD);
	    
	    Locale loc = new Locale("",getItem(position).getMeetLocation().getCountryCode());
	    
	    viewHolder.tvLocation.setText(loc.getDisplayCountry()+","+
	    								getItem(position).getMeetLocation().getCity());
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
	    }catch(Exception e){Log.e("FetchGuidePhoto",e.toString());}
	    
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
	    TextView tvHighlight;
	    ImageView imgCover;
	    
	}
}
