package com.sagol.umorili;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

public class SourceDetailActivity extends SherlockFragmentActivity {

    public static int THEME = SourceListActivity.THEME;
    public static String selecteID = SourceListActivity.selecteID;
    public static int font_size = SourceListActivity.font_size;
    public static boolean full_screen = SourceListActivity.full_screen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            THEME       = savedInstanceState.getInt("theme");
            selecteID   = savedInstanceState.getString("select_id");
            font_size   = savedInstanceState.getInt("font_size");
            full_screen = savedInstanceState.getBoolean("full_screen");
        } else {
            THEME = SourceListActivity.THEME;
            selecteID = SourceListActivity.selecteID;
            font_size = SourceListActivity.font_size;
            full_screen = SourceListActivity.full_screen;
        }

        setTheme(THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_detail);

        if (full_screen) getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
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
        outState.putInt("font_size", font_size);
        outState.putBoolean("full_screen", full_screen);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        THEME       = savedInstanceState.getInt("theme");
        selecteID   = savedInstanceState.getString("select_id");
        font_size   = savedInstanceState.getInt("font_size");
        full_screen = savedInstanceState.getBoolean("full_screen");
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
                        getSupportFragmentManager().findFragmentById(R.id.source_detail_container);

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
