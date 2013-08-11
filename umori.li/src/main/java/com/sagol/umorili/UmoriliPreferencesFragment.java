
package com.sagol.umorili;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class UmoriliPreferencesFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}
