package mo.iguideu.ui.createGuide;

import java.util.ArrayList;
import java.util.List;

import com.koushikdutta.ion.Ion;

import mo.iguideu.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SelectedPhotoGirdviewAdapter extends BaseAdapter {

	private List<String> selectedPath = new ArrayList<String>();
	private LayoutInflater infalter;
	private IPhotoSelectListener listener = null;
	
	
	public SelectedPhotoGirdviewAdapter(LayoutInflater inflater){
		this.infalter = inflater;
		
	}
	
	public void setPhotoSelectListener(IPhotoSelectListener listener){
		this.listener = listener;
	}
	
	@Override
	public int getCount() {
		return selectedPath.size();
	}

	@Override
	public Object getItem(int position) {
		return selectedPath.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolderItem viewHolder;
		
		ImageView 	imgPhoto 	 = null;
		ImageView 	imgDelete 	 = null;
		
		if (convertView == null) {
			convertView = infalter.inflate(R.layout.item_upload_photo, null);
			imgPhoto 	= (ImageView) convertView.findViewById(R.id.item_upload_photo_img);
			imgDelete 	= (ImageView) convertView.findViewById(R.id.item_upload_photo_img_delete);
			
			viewHolder = new ViewHolderItem();
	    	viewHolder.imgPhoto  = imgPhoto;
	    	viewHolder.imgDelete = imgDelete;
	    	convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolderItem) convertView.getTag();
		}
		
		viewHolder.imgDelete.setTag(selectedPath.get(position));
		
		Ion.with(viewHolder.imgPhoto)
        .resize(128, 128)
        .centerCrop()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.error)
        .load(selectedPath.get(position));
		
		viewHolder.imgDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String path = "";
				try{
					path = (String) v.getTag();
					selectedPath.remove(path);
					Log.e("SelectPhotoAdapter", "Remove photo!"+path);
				}catch(Exception e){}
				if(listener!=null)
					listener.onPhotoRemoved(path);
			}
		});
		
		return convertView;
	}

	
	public void addSelectedPath(String path){
		selectedPath.add(path);
	}
	
	public void addSelectPaths(String[] paths){
		for(String path : paths)
			selectedPath.add(path);
	}
	
	public void clearPhotos(){
		selectedPath.clear();
	}
	
	static class ViewHolderItem {
	    ImageView imgPhoto;
	    ImageView imgDelete;
	}
}
