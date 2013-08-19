package com.sagol.umorili;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;

import java.util.concurrent.ExecutionException;

public class SourceDetailFragment extends SherlockListFragment {
    public static final String ARG_ITEM_ID = "item_id";

    private UmoriliDataContent.DataItem mItem;
    public static UmoriliDataContent udc;

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

        mItem = udc_sources.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

        getTask = new GetDataTask(this.getActivity());
        try {
            getTask.execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (udc == null || udc.ITEMS.size() == 0) {
            Toast.makeText(this.getSherlockActivity(),
                    getResources().getString(R.string.connection_error_text),
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            setListAdapter(new UmoriliAdapter(
                    getActivity(),
                    R.id.sources_detail_listview,
                    udc.ITEMS));
        }

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    private UmoriliParser umoriliParser = new UmoriliParser();
    private UmoriliDataContent udc_sources;

    private class GetDataTask extends AsyncTask<String, Void, String> {

        private ProgressDialog spinner;

        private FragmentActivity activity;

        public GetDataTask(FragmentActivity activity) {
            this.activity = activity;
        }
        @Override
        protected void onPreExecute() {
            spinner = new ProgressDialog(activity);
            spinner.setMessage("Идет загрузка...");
            spinner.show();
        }

        @Override
        protected void onPostExecute(String result) {
            spinner.dismiss();
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            if (mItem != null) {
                if (mItem.id.equals("random")) {
                    udc = umoriliParser.get(100);
                } else {
                    udc = umoriliParser.get(mItem.site, mItem.name, 100);
                }
            } else {
                udc_sources = umoriliParser.sources();
            }
            return result;
        }

    }
}
