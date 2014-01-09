package com.sagol.umorili;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.widget.ShareActionProvider;


public class SourceDetailFragment extends SherlockListFragment {
    public static final String ARG_ITEM_ID = "item_id";

    private UmoriliDataContent.DataItem mItem;
    public static UmoriliDataContent udc;

    private GetDataTask getTask;

    public boolean getData () {
        getTask = new GetDataTask();
        getTask.execute("");
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_source_detail, container, false);
        ListView listView = (ListView) v.findViewById(R.id.sources_detail_listview);
        registerForContextMenu(listView);
        return v;
    }
*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        registerForContextMenu(listView);
        super.onActivityCreated(savedInstanceState);
    }

    private Intent createShareIntent(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(shareIntent);
        return shareIntent;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, getResources().getString(R.string.action_bar_share_with));
        menu.add(0, 1, 1, getResources().getString(R.string.copy_text));
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            return false;
        }
        String text = ((TextView) info.targetView).getText().toString();
        if (item.getItemId() == 0) {
            ShareActionProvider actionProvider = new ShareActionProvider(this.getSherlockActivity());
            actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
            actionProvider.setShareIntent(createShareIntent(text));
        } else if (item.getItemId() == 1) {
            ClipboardManager clipboard = (ClipboardManager)
                    UmoriliApplication.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", text);
            clipboard.setPrimaryClip(clip);
        }
        return true;
    }


    private UmoriliParser umoriliParser = new UmoriliParser();
    private UmoriliDataContent udc_sources;

    private class GetDataTask extends AsyncTask<String, Void, String> {

        private ProgressDialog spinner;

        @Override
        protected void onPreExecute() {
            spinner = new ProgressDialog(getSherlockActivity());
            spinner.setMessage(getResources().getString(R.string.loading_text));
            spinner.show();
        }

        @Override
        protected void onPostExecute(String result) {
                if (udc == null || udc.ITEMS.size() == 0) {
                    Toast.makeText(getSherlockActivity(),
                            getResources().getString(R.string.connection_error_text),
                            Toast.LENGTH_LONG).show();
                } else {
                    setListAdapter(new UmoriliAdapter(
                            getSherlockActivity(),
                            R.id.sources_detail_listview,
                               udc.ITEMS));
                }
            spinner.dismiss();
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            udc_sources = umoriliParser.sources();
            if (udc_sources.ITEMS.size() > 1) {
                mItem = udc_sources.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
                if (mItem != null) {
                    if (mItem.id.equals("random")) {
                        udc = umoriliParser.get(100);
                    } else {
                        udc = umoriliParser.get(mItem.site, mItem.name, 100);
                    }
                }
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
        }

    }
}
