package mo.iguideu.ui.initGuideProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import mo.iguideu.R;
import mo.iguideu.ui.base.IStepValidateCheck;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentInitGuideDataStep2 extends Fragment implements
		IStepValidateCheck {

	private CheckBox chkLangTw 		= null;
	private CheckBox chkLangEn 		= null;
	private CheckBox chkLangJp 		= null;
	private Spinner  spinnerMoreLang = null;
	private LinearLayout scrollContainer = null;
	private ScrollView scrollViewLangs = null;

	private LayoutInflater layoutInflater = null;
	
	private List<Locale> supportLocale = new ArrayList<Locale>();

//	private boolean isGotLicense = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layoutInflater = inflater;
		View v = inflater.inflate(R.layout.fragment_init_guide_data_step2,
				container, false);
		scrollViewLangs =  (ScrollView) v.findViewById(R.id.fragment_init_guide_data_lang_scrollview);
		scrollContainer = (LinearLayout) v.findViewById(R.id.fragment_init_guide_data_lang_scroll_container);
		chkLangTw = (CheckBox) v
				.findViewById(R.id.lang_chk_tw);
		chkLangEn = (CheckBox) v
				.findViewById(R.id.item_chk_lang_checkbox_en);
		chkLangJp = (CheckBox) v
				.findViewById(R.id.item_chk_lang_checkbox_jp);
		spinnerMoreLang = (Spinner) v
				.findViewById(R.id.fragment_init_guide_data_spinner_more_lang);
		
		refreshSupportLocales();
		initMoreLanguageSpinner();
		
		return v;
	}
	
	private void initMoreLanguageSpinner(){
		
		String languages = getResources().getString(R.string.spinner_choose_more_language)+",";
		
		int idx=0;
		for(Locale locale : supportLocale){
			languages += locale.getDisplayCountry()+"-"+locale.getDisplayLanguage()+
							((idx==supportLocale.size()-1)?"":",");
			idx++;
		}
		
		String[] langArray = languages.split(",");
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity()
				,android.R.layout.simple_spinner_item, langArray);
		
		spinnerMoreLang.setAdapter(adapter);
		spinnerMoreLang.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == 0)
					return;
				
				if(layoutInflater==null)
					return;

				//-1 cause first element is choose more
				Locale selectedLocale = supportLocale.get(position-1);
				
				String selectString = spinnerMoreLang.getSelectedItem().toString();
				String langCode     = selectedLocale.getLanguage()+"_"+selectedLocale.getCountry();
						
				//Avoid select repeat
				for(int i=0; i<scrollContainer.getChildCount(); i++){
					View langChkView = scrollContainer.getChildAt(i);
					if(langChkView.getTag().toString().equals(langCode)){
						//Language already selected, break;
						return;
					}
				}
				
				View itemLanguage = layoutInflater.inflate(R.layout.item_chk_language, scrollContainer, false);
				itemLanguage.setTag(langCode);
				
				CheckBox chkBox = (CheckBox) itemLanguage.findViewById(R.id.lang_chk);
				chkBox.setChecked(true);
				TextView countryName = (TextView) itemLanguage.findViewById(R.id.item_chk_lang_txt_country_name);
				countryName.setText(selectString);
//				ImageView flag = (ImageView) itemLanguage.findViewById(R.id.item_chk_lang_img_flag_tw);
				
				scrollContainer.addView(itemLanguage);
				scrollViewLangs.scrollTo(0, scrollViewLangs.getBottom());
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	public void refreshSupportLocales() {

		String[] countries = Locale.getISOCountries();
		Locale[] locales = Locale.getAvailableLocales();
		
		Map<String, String> languagesMap = new TreeMap<String, String>();

		for (Locale obj : locales) {
			if ((obj.getDisplayCountry() != null)
					&& (!"".equals(obj.getDisplayCountry()))) {
				languagesMap.put(obj.getCountry(), obj.getLanguage());
			}
		}

		for (String countryCode : countries) {
			Locale obj = new Locale("", countryCode);
			if (languagesMap.get(countryCode) != null) {
				obj = new Locale(languagesMap.get(countryCode), countryCode);
				if(!obj.getCountry().equals("TW")&&!obj.getCountry().equals("JP")&&!obj.getCountry().equals("CN"))
					supportLocale.add(obj);
			}
		}
	}

	public List<String> getSupportLanguages() {
		List<String> langs = new ArrayList<String>();

		//User add language check
		for(int i=0; i<scrollContainer.getChildCount(); i++){
			View langChkView = scrollContainer.getChildAt(i);
			
			//Default language check
			if(langChkView.findViewById(R.id.item_chk_lang_checkbox_en)!=null && 
					langChkView.findViewById(R.id.item_chk_lang_checkbox_en).equals(chkLangEn) && 
						chkLangEn.isChecked())
				langs.add("en_US");
			else if(langChkView.findViewById(R.id.lang_chk_tw)!=null && 
						langChkView.findViewById(R.id.lang_chk_tw).equals(chkLangTw) && 
							chkLangTw.isChecked())
				langs.add("zh_TW");
			else if(langChkView.findViewById(R.id.item_chk_lang_checkbox_jp)!=null &&
					 langChkView.findViewById(R.id.item_chk_lang_checkbox_jp).equals(chkLangJp) && 
					 	chkLangJp.isChecked())
				langs.add("ja_JP");
			else{
				CheckBox chkbox = (CheckBox) langChkView.findViewById(R.id.lang_chk);
				if(chkbox!=null && chkbox.isChecked())
					langs.add(langChkView.getTag().toString());
			}
		}
		
		return langs;
	}

	@Override
	public boolean isDataValid() {
		return getSupportLanguages().size()>0?true:false;
	}

	@Override
	public String getDataInValidString() {
		return getResources().getString(R.string.error_init_guide_data_at_least_choose_one_language);
	}

}
