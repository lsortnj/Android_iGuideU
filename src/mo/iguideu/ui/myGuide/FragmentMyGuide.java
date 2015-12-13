package mo.iguideu.ui.myGuide;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;

import mo.iguideu.R;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentMyGuide extends Fragment {

	private PullAndLoadListView listview;
	private ProgressDialog 		progressDialog = null;
	
	private List<MyGuide> guides = new ArrayList<MyGuide>();
	
	private int 	currentFetchOffset 	= 0;
	private boolean isLast 				= false;
	
	private boolean isFirstLoad = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v   = inflater.inflate(R.layout.fragment_my_guides, container, false);
		listview = (PullAndLoadListView) v.findViewById(R.id.my_guide_listview);
		listview.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
            	resetAll();
            	doRefresh();
            }
        });
		listview.setOnLoadMoreListener(new OnLoadMoreListener() {
            public void onLoadMore() {
            	fetchMoreGuide();
            }
        });
		
		resetAll();
		
		isFirstLoad = true;
		
		return v;
	}
	
	public void resetAll(){
		isFirstLoad = true;
		isLast = false;
		currentFetchOffset = 0;
		guides.clear();
	}
	
	private void fetchMoreGuide(){
		if(!isLast){
			currentFetchOffset++;
			getMyGuides();
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.my_guide, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.btn_add) {
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 @Override
	 public void onResume(){
		 super.onResume();
		 
		 if(isFirstLoad){
			 resetAll();
			 doRefresh();
		 }
	 }
	 
	 public void doRefresh(){
		 progressDialog = ProgressDialog.show(getActivity(), 
					getResources().getString(R.string.title_wait), 
					getResources().getString(R.string.progress_refresh_guide), true, false);
		 progressDialog.show();
		 getMyGuides();
	 }
	 
	 private Handler handler = new Handler(){
		 public void handleMessage(Message msg){
			 if(progressDialog!=null && progressDialog.isShowing())
					progressDialog.dismiss();
			 listview.onRefreshComplete();
			 listview.onLoadMoreComplete();
			 switch(msg.what){
			 	case ServerInfo.GET_MY_GUIDES_DONE:
			 		String nearbyJson = (String) msg.obj;
			 		if(nearbyJson!=null && nearbyJson.startsWith("[")){
			 			restoreGuideFromJSONString(nearbyJson);
			 			isFirstLoad = false;
			 		}
			 		break;
			 }
		 }
	 };
	 
	 private void getMyGuides(){
		 ServerCore.instance().getMyGuides(handler,currentFetchOffset);
	 }
	 
	 private void appendToListView(){
		 MyGuideAdapter adapter = new MyGuideAdapter(getActivity(),R.layout.card_my_guide,guides);
		 listview.setAdapter(adapter);
		 if(currentFetchOffset!=0){
				int scrollTo = (currentFetchOffset-1)*ServerInfo.FETCH_COUNT_EACH_TIME+ServerInfo.FETCH_COUNT_EACH_TIME;
				listview.setSelectionFromTop(scrollTo, 0);
		 }
	 }
	 
	 private void restoreGuideFromJSONString(String jsonString){
		 if(jsonString!=null && !jsonString.equals("") && jsonString.startsWith("[")){
			 JSONArray guideJsonArr = JSONArray.fromObject(jsonString);
			 
			 for(int i=0; i<guideJsonArr.size(); i++){
				 JSONObject guideJsonObj = guideJsonArr.getJSONObject(i);
				 MyGuide guide = new MyGuide();
				 guide.restoreFromJSONObject(guideJsonObj);
				 guides.add(guide);
			 }
			 
			 if(guideJsonArr.size()<ServerInfo.FETCH_COUNT_EACH_TIME){
					isLast = true;
					listview.isAllLoaded(true);
			 }
			 
			 appendToListView();
		 }
	 }
	
	@Override
	public void onStart(){
		super.onStart();
	
	}
	
	public ListView getListView() {
		return listview;
	}
	 
}
