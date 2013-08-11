package com.sagol.umorili;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.concurrent.ExecutionException;


/**
 * A fragment representing a single Source detail screen.
 * This fragment is either contained in a {@link SourceListActivity}
 * in two-pane mode (on tablets) or a {@link SourceDetailActivity}
 * on handsets.
 */
public class SourceDetailFragment extends SherlockFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The umorili content this fragment is presenting.
     */
    private UmoriliDataContent.DataItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SourceDetailFragment() {
    }
    private GetDataTask getTask;

    public boolean getData () {
        getTask = new GetDataTask(this.getActivity());
        try {
            getTask.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        if (getArguments().containsKey(ARG_ITEM_ID)) {
  //          if (ARG_ITEM_ID == "random") {

    //        } else {
                mItem = udc_sources.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
      //      }
     //   }

        getTask = new GetDataTask(this.getActivity());
        if (getTask != null) {
            try {
                getTask.execute("").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    //  private ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_source_detail_webmail, container, false);

        webView = (WebView)rootView.findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
//            webView.getSettings().setTextZoom().setTextSize(WebSettings.TextSize.SMALLEST);

        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        ScrollView scroller = (ScrollView) rootView.findViewById(R.id.scrollView);
        scroller.setSmoothScrollingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (SourceListActivity.THEME == R.style.Theme_Sherlock) {
            webView.setBackgroundColor(0x00000000);
        }
        try {

            String summary = "<html><body>" + getTask.get() + "</body></html>";
            webView.loadDataWithBaseURL(null, summary, "text/html", "utf-8", null);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private UmoriliParser umoriliParser = new UmoriliParser();
    private UmoriliDataContent udc_sources;
    private UmoriliDataContent udc_data;

    private class GetDataTask extends AsyncTask<String, Void, String> {

        private ProgressDialog spinner;

        private FragmentActivity activity;

        public GetDataTask(FragmentActivity activity) {
            this.activity = activity;
        }
        @Override
        protected void onPreExecute() {
            // Вначале мы покажем пользователю ProgressDialog
            // чтобы он понимал что началась загрузка
            // этот метод выполняется в UI потоке
            spinner = new ProgressDialog(activity);
            spinner.setMessage("Идет загрузка...");
            spinner.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // Загрузка закончена. Закроем ProgressDialog.
            // этот метод выполняется в UI потоке
            spinner.dismiss();
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            if (mItem != null) {
                if (mItem.id == "random") {
                    udc_data = umoriliParser.get(100);
                } else {
                    udc_data = umoriliParser.get(mItem.site, mItem.name, 100);
                }
                TypedValue tv = new TypedValue();
                getActivity().getTheme().resolveAttribute(android.R.attr.textColorPrimary, tv, true);
                int textColor = getResources().getColor(tv.resourceId);

                if (udc_data != null) {
                    for (int i = 0; i < udc_data.ITEMS.size(); i++) {
                        result += "<div style=\"color: " +
                                String.format("#%06X", (0xFFFFFF & textColor)) + ";\">" +
                                udc_data.ITEMS.get(i).toString() + "</div><hr><br />";
                    }
                }
            } else {
                udc_sources = umoriliParser.sources();
            }
            return result;
        }

    }
}
