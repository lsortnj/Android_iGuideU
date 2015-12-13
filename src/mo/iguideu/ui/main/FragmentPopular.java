package mo.iguideu.ui.main;

import java.util.ArrayList;
import java.util.List;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import mo.iguideu.R;
import mo.iguideu.guide.GuideEvent;
import mo.iguideu.serverHandler.ServerCore;
import mo.iguideu.serverHandler.ServerInfo;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class FragmentPopular extends Fragment {

	private List<GuideEvent> guides = new ArrayList<GuideEvent>();

	private ProgressDialog progressDialog = null;

	private PullAndLoadListView listview = null;
	private GuideAdapter adapter;

	private int 	currentFetchOffset 	= 0;
	private boolean isLast 				= false;
	
	private boolean isFirstLoad = true;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		listview = (PullAndLoadListView) v
				.findViewById(R.id.load_more_listview);
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
			getGuidePopular();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.discover, menu);
		MenuItem menuSearch = menu.findItem(R.id.menu_discover_search);
		
	    MenuItemCompat.setOnActionExpandListener(menuSearch, new OnActionExpandListener() {
	        @Override
	        public boolean onMenuItemActionCollapse(MenuItem item) {
	            // Do something when collapsed
	            return true;  // Return true to collapse action view
	        }

	        @Override
	        public boolean onMenuItemActionExpand(MenuItem item) {
	            // Do something when expanded
	            return true;  // Return true to expand action view
	        }
	    });
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();

		 if(isFirstLoad){
			 resetAll();
			 doRefresh();
		 }
	}

	public void doRefresh() {
		progressDialog = ProgressDialog.show(getActivity(), getResources()
				.getString(R.string.title_wait),
				getResources().getString(R.string.progress_refresh_guide),
				true, false);
		progressDialog.show();
		getGuidePopular();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			listview.onRefreshComplete();
			listview.onLoadMoreComplete();
			switch (msg.what) { 
			case ServerInfo.FETCH_GUIDE_DONE:
				String nearbyJson = (String) msg.obj;
				if(nearbyJson!=null && nearbyJson.startsWith("[")){
					restoreGuideFromJSONString(nearbyJson);
					isFirstLoad = false;
				}
				break;
			}
		} 
	};

	private void getGuidePopular() {
		ServerCore.instance().getGuidePopular(handler, currentFetchOffset);
	}

	private void appendToListView() {
		adapter = new GuideAdapter(getActivity(),R.layout.card_my_guide, guides);
		listview.setAdapter(adapter);
		if(currentFetchOffset!=0){
			int scrollTo = (currentFetchOffset-1)*ServerInfo.FETCH_COUNT_EACH_TIME+ServerInfo.FETCH_COUNT_EACH_TIME-1;
			listview.setSelectionFromTop(scrollTo, 0);
		}
		
	}

	private void restoreGuideFromJSONString(String jsonString) {
		if (jsonString != null && !jsonString.equals("")
				&& jsonString.startsWith("[")) {
			JSONArray guideJsonArr = JSONArray.fromObject(jsonString);
			
			for (int i = 0; i < guideJsonArr.size(); i++) {
				JSONObject guideJsonObj = guideJsonArr.getJSONObject(i);
				GuideEvent guide = new GuideEvent();
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
}
