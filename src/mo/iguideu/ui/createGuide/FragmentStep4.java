package mo.iguideu.ui.createGuide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mo.iguideu.R;
import mo.iguideu.store.AppPreference;
import mo.iguideu.ui.base.ActivityChoosePhoto;
import mo.iguideu.ui.base.DialogPositiveNegative;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Create new Guide step4 select photos
 * 
 * @author lsortnj
 * 
 */
public class FragmentStep4 extends Fragment implements IStepValidateCheck,
		IPhotoSelectListener {

	public static final int REQUEST_CODE_LAUNCH_CAMERA = 0x900;
	
	private ImageButton btnChoosePhoto = null;
	private GridView gridviewPhotos = null;
	private SelectedPhotoGirdviewAdapter adapter = null;
	private List<String> selectedPhotosPath = new ArrayList<String>();
	private DialogPositiveNegative dialogPhotoSource = null;
	private Uri captureImageURI;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_creat_step4, container,
				false);
		btnChoosePhoto = (ImageButton) v
				.findViewById(R.id.create_guide_step4_btn_add_photos);
		gridviewPhotos = (GridView) v
				.findViewById(R.id.create_guide_step4_gridview_photos);

		btnChoosePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogPhotoSource();
				
			}
		});
		adapter = new SelectedPhotoGirdviewAdapter(inflater);
		adapter.setPhotoSelectListener(this);
		gridviewPhotos.setAdapter(adapter);

		updateSelectedPhotoGridview();

		return v;
	}
	
	private void showDialogPhotoSource(){
		if(dialogPhotoSource == null){
			dialogPhotoSource = new DialogPositiveNegative(
					getActivity(), "", getResources().getString(R.string.dilaog_msg_photo_source), 
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							doLaunchCamera();
							dialogPhotoSource.dismiss();
						}
					});
			
			dialogPhotoSource.setPositiveButtonText(getResources().getString(R.string.btn_take_photo));
			dialogPhotoSource.setNegtiveButtonText(getResources().getString(R.string.btn_from_gallery));
			dialogPhotoSource.setNegtiveOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					chooseFromGallery();
					dialogPhotoSource.dismiss();
				}
			});
		}
		
		dialogPhotoSource.show();
	}
	
	private void chooseFromGallery(){
		// To photo choose activity
		Intent intent = new Intent();
		intent.setClass(getActivity(), ActivityChoosePhoto.class);
		startActivityForResult(intent,
				ActivityChoosePhoto.REQUEST_CODE_CHOOSE_PHOTOS);
	}
	
	private void doLaunchCamera(){
		//define the file-name to save photo taken by Camera activity
		String fileName = "guide_photo"+Calendar.getInstance().getTimeInMillis()+".jpg";
		//create parameters for Intent with filename
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
		//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		captureImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		//create new Intent
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageURI);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, REQUEST_CODE_LAUNCH_CAMERA);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ActivityChoosePhoto.REQUEST_CODE_CHOOSE_PHOTOS) {
			switch (resultCode) {
			case ActivityChoosePhoto.RESULT_CODE_CHOOSE_PHOTOS_FINISH:

				String[] paths = data.getExtras().getStringArray("all_path");
				for (String path : paths)
					selectedPhotosPath.add(path);

				updateSelectedPhotoGridview();

				break;
			case ActivityChoosePhoto.RESULT_CODE_CHOOSE_PHOTOS_CANCEL:

				break;
			}
		}else if(requestCode == REQUEST_CODE_LAUNCH_CAMERA){
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = captureImageURI;
	            getActivity().getContentResolver().notifyChange(selectedImage, null);
	            ContentResolver cr = getActivity().getContentResolver();
	            Bitmap bitmap;
	            try {
	            	 String filePath = AppPreference.instance().getTempCameraPhotoSavePath();
	                 bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
	                 bitmap.compress(CompressFormat.JPEG, 80, 
	                		 new FileOutputStream(new File(filePath)));
	                 selectedPhotosPath.add(filePath);
	                 updateSelectedPhotoGridview();
	            } catch (Exception e) {
	                Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
	            }
	        }
		}
	}

	private void updateSelectedPhotoGridview() {
		if (selectedPhotosPath != null) {
			adapter.clearPhotos();
			for (String path : selectedPhotosPath)
				adapter.addSelectedPath(path);
			((BaseAdapter) gridviewPhotos.getAdapter()).notifyDataSetChanged();
		}
	}

	@Override
	public boolean isDataValid() {

		if (gridviewPhotos != null) {
			if (gridviewPhotos.getAdapter().getCount() > 0) {
				return true;
			}
		}

		return false;
	}

	public List<String> getAllPhotoPath() {
		List<String> paths = new ArrayList<String>();

		if (selectedPhotosPath != null) {
			for (String path : selectedPhotosPath) {
				if (path != null)
					paths.add(path);
			}
		}

		return paths;
	}

	@Override
	public String getDataInValidString() {
		return getActivity().getString(
				R.string.tip_create_guide_at_least_one_photo);
	}

	@Override
	public void onPhotoRemoved(String path) {

		if (selectedPhotosPath != null && path != null)
			selectedPhotosPath.remove(path);

		((BaseAdapter) gridviewPhotos.getAdapter()).notifyDataSetChanged();
	}
}
