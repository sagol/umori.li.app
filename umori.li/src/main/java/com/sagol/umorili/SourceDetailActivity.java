package com.sagol.umorili;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class SourceDetailActivity extends SherlockFragmentActivity {

    public static int THEME = SourceListActivity.THEME;
    public static String selecteID = SourceListActivity.selecteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_detail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(SourceDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(SourceDetailFragment.ARG_ITEM_ID));
            SourceDetailFragment fragment = new SourceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.source_detail_container, fragment)
                    .commit();

        } else {
            THEME     = savedInstanceState.getInt("theme");
            selecteID = savedInstanceState.getString("select_id");
        }
        try {
            UmoriliDataContent.DataItem mItem = SourceListFragment.udc.ITEM_MAP.get(selecteID);
            setTitle(mItem.desc);
        } catch (Exception e){ // надо переделать на UncaughtExceptionHandler
            setTitle("");
        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", THEME);
        outState.putString("select_id", selecteID);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        THEME     = savedInstanceState.getInt("theme");
        selecteID = savedInstanceState.getString("select_id");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isLight = THEME == R.style.Theme_Sherlock_Light;
        menu.add(getResources().getString(R.string.refresh_text)).setIcon(isLight ?
                R.drawable.ic_navigation_refresh_light :
                R.drawable.ic_navigation_refresh).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, SourceListActivity.class));
                return true;
            case 0:
                SourceDetailFragment sourceDetailFragment = (SourceDetailFragment)
                        getSupportFragmentManager().findFragmentById(R.id.webView);

                if (sourceDetailFragment != null) {
                    sourceDetailFragment.getData();
                } else {
                    Bundle arguments = new Bundle();
                    arguments.putString(SourceDetailFragment.ARG_ITEM_ID,
                            getIntent().getStringExtra(SourceDetailFragment.ARG_ITEM_ID));
                    SourceDetailFragment fragment = new SourceDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.source_detail_container, fragment)
                            .commit();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
