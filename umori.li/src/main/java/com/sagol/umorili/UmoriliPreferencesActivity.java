
package com.sagol.umorili;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;


public class UmoriliPreferencesActivity extends SherlockPreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SourceListActivity.THEME);
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new UmoriliPreferencesFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}