package com.sagol.umorili;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;


import java.util.concurrent.ExecutionException;

public class SourceListFragment extends SherlockListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sUmoriliCallbacks;

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks sUmoriliCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public SourceListFragment() {
    }

    private GetSources getSources = null;


    public boolean getSources (boolean async) {
        if (async)
        {
            getSources = new GetSources(this.getSherlockActivity());
            try {
                getSources.execute("").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else {
            udc = umoriliParser.sources();
        }
        if (udc.ITEMS.size() == 0) {
            Toast.makeText(this.getSherlockActivity(),
                    getResources().getString(R.string.connection_error_text),
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            setListAdapter(new ArrayAdapter<UmoriliDataContent.DataItem>(
                    getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    android.R.id.text1,
                    udc.ITEMS.subList(0, udc.ITEMS.size() - 1)));
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSources(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sUmoriliCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        mCallbacks.onItemSelected(udc.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    private UmoriliParser umoriliParser = new UmoriliParser();
    public static UmoriliDataContent udc;

    private class GetSources extends AsyncTask<String, Void, String> {

        private ProgressDialog spinner;
        private SherlockFragmentActivity activity;

        public GetSources(SherlockFragmentActivity activity) {
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

        protected String doInBackground(String... urls) {
            udc = umoriliParser.sources();
            return null;
        }

        protected void onProgressUpdate(Void... progress) {
        }

    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
