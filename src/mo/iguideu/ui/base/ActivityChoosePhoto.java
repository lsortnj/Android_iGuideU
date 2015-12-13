package mo.iguideu.ui.base;


import java.io.File;
import java.util.ArrayList;
import mo.iguideu.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import com.koushikdutta.ion.Ion;

public class ActivityChoosePhoto extends Activity {

	public static final int REQUEST_CODE_CHOOSE_PHOTOS 		 = 0x400;
	public static final int RESULT_CODE_CHOOSE_PHOTOS_CANCEL = 0x401;
	public static final int RESULT_CODE_CHOOSE_PHOTOS_FINISH = 0x402;
	
	private GridView gridGallery;

	private ImageView imgNoMedia;
	private Button btnGalleryOk;

	private MyAdapter mAdapter;
	private ArrayList<String> selectImgUris = new ArrayList<String>();

    // Adapter to populate and imageview from an url contained in the array adapter
    private class MyAdapter extends ArrayAdapter<String> {
    	
    	
        public MyAdapter(Context context) {
            super(context, 0);
        }
        
        public void changeSelection(View v, int position) {

        	String  selectImgUri = getItem(position);
        	boolean isAlreadyAdd = false; 
        	
        	for(String uriString : selectImgUris){
        		if(uriString.equals(selectImgUri)){
        			isAlreadyAdd = true;
        			break;
        		}
        	}
        	
        	if(isAlreadyAdd)
        		selectImgUris.remove(selectImgUri);
        	else
        		selectImgUris.add(selectImgUri);
    		
    		((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(!isAlreadyAdd);
    	}
        
        @SuppressLint("InflateParams")
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // see if we need to load more to get 40, otherwise populate the adapter
            if (position > getCount() - 4)
                loadMore();

            final ViewHolder holder;
            if (convertView == null) {

    			convertView = getLayoutInflater().inflate(R.layout.gallery_item, null);
    			holder = new ViewHolder();
    			holder.imgQueue = (ImageView) convertView
    					.findViewById(R.id.imgQueue);

    			holder.imgQueueMultiSelected = (ImageView) convertView
    					.findViewById(R.id.imgQueueMultiSelected);

    			holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);

    			convertView.setTag(holder);

    		} else {
    			holder = (ViewHolder) convertView.getTag();
    		}

            Ion.with(holder.imgQueue)
                    .resize(256, 256)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .load(getItem(position));

            return convertView;
        }
    }
    
    public class ViewHolder {
		ImageView imgQueue;
		ImageView imgQueueMultiSelected;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo_choose);

		Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
		
		int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi * 2;
        
        gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		gridGallery.setNumColumns(cols);
		gridGallery.setOnItemClickListener(mItemMulClickListener);
		
		mAdapter = new MyAdapter(this);
		gridGallery.setAdapter(mAdapter);

		findViewById(R.id.llBottomContainer).setVisibility(View.VISIBLE);
		
		imgNoMedia = (ImageView) findViewById(R.id.imgNoMedia);

        btnGalleryOk = (Button) findViewById(R.id.btnGalleryOk);
		btnGalleryOk.setOnClickListener(mOkClickListener);
        
        loadMore();
        
        checkImageStatus();
	}


	private void checkImageStatus() {
		if (mAdapter.isEmpty()) {
			imgNoMedia.setVisibility(View.VISIBLE);
		} else {
			imgNoMedia.setVisibility(View.GONE);
		}
	}

	View.OnClickListener mOkClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			String[] allPath = new String[selectImgUris.size()];
			
			int idx=0;
			for(String uriString : selectImgUris){
				allPath[idx] = uriString;
				idx++;
        	}
			
			Intent data = new Intent().putExtra("all_path", allPath);
			setResult(RESULT_CODE_CHOOSE_PHOTOS_FINISH, data);
			finish();

		}
	};
	AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			mAdapter.changeSelection(v, position);
		}
	};

	

	Cursor mediaCursor;
    public void loadMore() {
        if (mediaCursor == null) {
            mediaCursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), null, null, null, null);
        }

        int loaded = 0;
        while (mediaCursor.moveToNext() && loaded < 10) {
            // get the media type. ion can show images for both regular images AND video.
            int mediaType = mediaCursor.getInt(mediaCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE));
            if (mediaType != MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                && mediaType != MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                continue;
            }

            loaded++;

            String uri = mediaCursor.getString(mediaCursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            File file = new File(uri);
            // turn this into a file uri if necessary/possible
            if (file.exists())
                mAdapter.add(file.toURI().toString());
            else
                mAdapter.add(uri);
        }
    }

}
