package mo.iguideu;

import mo.iguideu.ui.createGuide.ActivityCreateGuide;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ScrollView scrollView;
    private View mDrawerListView;
    private View mFragmentContainerView;
    
    //Side menu
  	private View menuPopular;
  	private View menuNearby;
  	private View menuNiceGuider;
  	private View menuUserProfile;
  	private View menuMyGuide;
  	private View menuTravelerUpcoming;
  	private View menuTravelerWaitingRating;
  	private View menuGuiderUpcoming;
  	private View menuGuiderWaitingRating;
  	private View menuSetting;
  	private View menuLogout;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mDrawerListView = inflater.inflate(R.layout.fragment_side_menu, container, false);
        
        scrollView			= (ScrollView) mDrawerListView.findViewById(R.id.side_menu_scrollview);
        menuUserProfile  	= mDrawerListView.findViewById(R.id.side_menu_layout_user_info);
        menuPopular      	= mDrawerListView.findViewById(R.id.side_menu_layout_popular);
        menuNearby       	= mDrawerListView.findViewById(R.id.side_menu_layout_nearby_guide);
        menuNiceGuider   	= mDrawerListView.findViewById(R.id.side_menu_layout_nice_guider);
        menuMyGuide 	 	= mDrawerListView.findViewById(R.id.side_menu_layout_my_guide);
		menuSetting 	 	= mDrawerListView.findViewById(R.id.side_menu_layout_setting);
		menuLogout 	     	= mDrawerListView.findViewById(R.id.side_menu_btn_logout);
		
		menuTravelerUpcoming		= mDrawerListView.findViewById(R.id.side_menu_layout_traveler_upcoming);
		menuTravelerWaitingRating	= mDrawerListView.findViewById(R.id.side_menu_layout_traveler_waiting_rating);
		
		menuGuiderUpcoming			= mDrawerListView.findViewById(R.id.side_menu_layout_guider_upcoming);
		menuGuiderWaitingRating		= mDrawerListView.findViewById(R.id.side_menu_layout_guider_waiting_rate);
		
		
		SideMenuClickListener listener = new SideMenuClickListener();
		menuUserProfile.setOnClickListener(listener);
		menuMyGuide.setOnClickListener(listener);
		menuSetting.setOnClickListener(listener);
		menuPopular.setOnClickListener(listener);
		menuNearby.setOnClickListener(listener);
		menuNiceGuider.setOnClickListener(listener);
		menuTravelerUpcoming.setOnClickListener(listener);
		menuTravelerWaitingRating.setOnClickListener(listener);
		menuGuiderUpcoming.setOnClickListener(listener);
		menuGuiderWaitingRating.setOnClickListener(listener);
		menuLogout.setOnClickListener(listener);
		
		//Hide Rating Reminder Default
		menuTravelerWaitingRating.setVisibility(View.GONE);
		menuGuiderWaitingRating.setVisibility(View.GONE);
		
        return mDrawerListView;
    }
    
    public void scrollToTop(){
    	scrollView.fullScroll(ScrollView.FOCUS_UP);
    }
    
    private AlphaAnimation getFlashAnimation(){
    	AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1200);
        anim.setRepeatCount(Animation.INFINITE);
        return anim;
    }
    
    public void setTravelerWaitingRatingCount(int count){
    	if(count>0){
    		menuTravelerWaitingRating.setVisibility(View.VISIBLE);
    		TextView waitingRatingCount = (TextView) menuTravelerWaitingRating.findViewById(R.id.side_menu_txt_traveler_rating_count);
        	waitingRatingCount.setText(""+count);
        	
        	waitingRatingCount.startAnimation(getFlashAnimation());
    	}else
    		menuTravelerWaitingRating.setVisibility(View.GONE);
    }
    
    public void setGuiderWaitingRatingCount(int count){
    	if(count>0){
    		menuGuiderWaitingRating.setVisibility(View.VISIBLE);
    		TextView waitingRatingCount = (TextView) menuGuiderWaitingRating.findViewById(R.id.side_menu_txt_guider_waiting_rate_count);
    		waitingRatingCount.setText(""+count);
    		waitingRatingCount.startAnimation(getFlashAnimation());
    	}else
    		menuGuiderWaitingRating.setVisibility(View.GONE);
    }
    
    
    
    class SideMenuClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			
			if (mDrawerLayout != null) {
	            mDrawerLayout.closeDrawer(mFragmentContainerView);
	        }
	        if (mCallbacks != null) {
	            mCallbacks.onNavigationDrawerItemSelected(v.getId());
	        }
		}
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                showGlobalContextActionBar();
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
//            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);
            
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.btn_add) {
        	//To Create Guide Screen
        	Intent intent = new Intent();
        	intent.setClass(getActivity(), ActivityCreateGuide.class);
        	getActivity().startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
