package com.sagol.umorili;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.util.concurrent.ExecutionException;

public class SourceListActivity extends SherlockFragmentActivity
        implements SourceListFragment.Callbacks {


    public static int THEME = R.style.Theme_Sherlock_Light_DarkActionBar;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static String selecteID = "random";  //random

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadPref();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_list);
        if (findViewById(R.id.source_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((SourceListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.source_list))
                    .setActivateOnItemClick(true);
            Bundle arguments = new Bundle();

            arguments.putString(SourceDetailFragment.ARG_ITEM_ID, selecteID);
            SourceDetailFragment fragment = new SourceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.source_detail_container, fragment)
                    .commit();
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isLight = SourceListActivity.THEME == R.style.Theme_Sherlock_Light;
/*
        SubMenu sub = menu.addSubMenu(getResources().getString(R.string.theme_label));
        sub.add(0, R.style.Theme_Sherlock, 0, "Default");
        sub.add(0, R.style.Theme_Sherlock_Light, 0, "Light");
        sub.getItem().setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
*/
        menu.add(0, 1, 1, getResources().getString(R.string.random_label)).setIcon(isLight ?
                R.drawable.ic_av_shuffle_light:
                R.drawable.ic_av_shuffle).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(0, 2, 2, getResources().getString(R.string.refresh_text)).setIcon(isLight ?
                R.drawable.ic_navigation_refresh_light :
                R.drawable.ic_navigation_refresh).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add(0, 3, 3, getResources().getString(R.string.options_label)).setIcon(isLight ?
                R.drawable.ic_action_settings_light :
                R.drawable.ic_action_settings).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            return false;
        }
        switch (item.getItemId()) {
            case 1: {
                selecteID = "random";
                LoadData loadData = new LoadData(this);
                if (loadData != null)
                    try {
                        loadData.execute("").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                break;
            }
            case 2: {
                if (mTwoPane) {
                    LoadData loadData = new LoadData(this);
                    if (loadData != null)
                        try {
                            loadData.execute("main").get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                }
                else {
                    SourceListFragment sherlockListFragment = (SourceListFragment)
                            getSupportFragmentManager().findFragmentById(R.id.source_list);

                    if (sherlockListFragment != null) {
                        sherlockListFragment.getSources(true);
                    }
                }
                break;
            }
            case 3: {
                Intent intent = new Intent();
                intent.setClass(this, UmoriliPreferencesActivity.class);
                startActivityForResult(intent, 0);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
//        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadPref();
        recreate();
    }

    private void loadPref(){
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pref = mySharedPreferences.getString("theme_list_preference", "2");

        try {
            switch (Integer.parseInt(pref)) {
                case 0:
                    THEME = R.style.Theme_Sherlock;
                    break;
                case 1:
                    THEME = R.style.Theme_Sherlock_Light;
                    break;
                case 2:
                    THEME = R.style.Theme_Sherlock_Light_DarkActionBar;
                    break;
            }
        } catch (NumberFormatException e) {
            THEME = R.style.Theme_Sherlock_Light_DarkActionBar;
        }

        setTheme(THEME);
//        String st = mySharedPreferences.getString("theme_list_preference", "0");
//        int n = Integer.parseInt(st);
    }

    /**
     * Callback method from {@link SourceListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        selecteID = id;
        LoadData loadData = new LoadData(this);
        if (loadData != null)
            try {
                loadData.execute("").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
    }

    private class LoadData extends AsyncTask<String, Void, String> {

        private ProgressDialog spinner;
        private SherlockFragmentActivity activity;

        public LoadData(SherlockFragmentActivity activity) {
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

        protected String doInBackground(String... activity) {
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(SourceDetailFragment.ARG_ITEM_ID, selecteID);
                SourceDetailFragment fragment = new SourceDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.source_detail_container, fragment)
                        .commit();
            }
            else if (!(activity[0].equals("main"))){
                Intent detailIntent = new Intent(this.activity, SourceDetailActivity.class);
                detailIntent.putExtra(SourceDetailFragment.ARG_ITEM_ID, selecteID);
                startActivity(detailIntent);
            }
            return null;
        }

        protected void onProgressUpdate(Void... progress) {
        }

    }

}