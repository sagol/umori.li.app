package com.sagol.umorili;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * An activity representing a single Source detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SourceListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link SourceDetailFragment}.
 */
public class SourceDetailActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SourceListActivity.THEME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_detail);

 //       getActionBar().setDisplayHomeAsUpEnabled(true);
        //скрываем статусбар:
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

            try {
                UmoriliDataContent.DataItem mItem = SourceListFragment.udc.ITEM_MAP.get(SourceListActivity.selecteID);
                setTitle(mItem.desc);
            } catch (Exception e){ // надо переделать на UncaughtExceptionHandler
                setTitle("");
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isLight = SourceListActivity.THEME == R.style.Theme_Sherlock_Light;
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
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
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
