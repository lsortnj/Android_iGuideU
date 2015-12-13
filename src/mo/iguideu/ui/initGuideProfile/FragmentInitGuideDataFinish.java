package mo.iguideu.ui.initGuideProfile;

import mo.iguideu.IGuideUActivity;
import mo.iguideu.R;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import mo.iguideu.ui.base.DialogSingleButton;
import mo.iguideu.util.NetworkUtil;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentInitGuideDataFinish extends Fragment{
	
	private ProgressDialog progressDialog = null;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	 }

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_init_guide_data_finish, container, false);
		Button btnFinish = (Button) v.findViewById(R.id.init_guide_data_btn_finish);
		
		btnFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NetworkUtil.hasConnectionExist()){
					progressDialog = ProgressDialog.show(getActivity(), 
							getResources().getString(R.string.title_wait), 
							getResources().getString(R.string.tip_updating_guider_profile), true, false);
					
					//Update server side data
					ServerCore.instance().updateGuiderData(updateGuideDataServerHandler);
				}else{
					updateGuideDataServerHandler.sendEmptyMessage(NetworkUtil.NO_CONNECTION_AVAILABLE);
				}
			}
		});
		
		return v;
	 }
	 
	//Handle result code from server
		private Handler updateGuideDataServerHandler = new Handler(){
			public void handleMessage(Message msg){
				
				if(progressDialog!=null && progressDialog.isShowing())
					progressDialog.dismiss();
				progressDialog = null;
				
				DialogSingleButton resultDialg = new DialogSingleButton(getActivity());
				
				final int responseCode = msg.what;
				
				switch(responseCode){
					case ServerInfo.RES_CODE_UPDATE_GUIDE_DATA_SUCCESS:
						resultDialg.setMessage(getResources().getString(R.string.tip_update_guide_data_seccess));
						break;
					case ServerInfo.RES_CODE_UPDATE_GUIDE_DATA_FAILED:
						resultDialg.setMessage(getResources().getString(R.string.tip_update_guide_data_failed));
						break;
					case NetworkUtil.NO_CONNECTION_AVAILABLE:
						resultDialg.setMessage(getResources().getString(R.string.tip_no_connection));
					default:
						resultDialg.setMessage(getResources().getString(R.string.tip_update_guide_data_failed));
				}
				resultDialg.setCanceledOnTouchOutside(false);
				resultDialg.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						if(responseCode==ServerInfo.RES_CODE_UPDATE_GUIDE_DATA_SUCCESS){
							getActivity().setResult(IGuideUActivity.RESULT_CODE_INIT_GUIDE_UPDATE_SUCCESS,null);
							getActivity().finish();
						}
					}
				});
				
				resultDialg.show();
			}
		};
}
