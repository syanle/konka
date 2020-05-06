package com.cyanogenmod.trebuchet.preference;

import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.util.Log;
import com.cyanogenmod.trebuchet.Launcher;
import com.cyanogenmod.trebuchet.LauncherApplication;
import com.konka.ios7launcher.R;
import com.tencent.stat.common.StatConstants;

public class Preferences extends PreferenceActivity {
    private static final String TAG = "Launcher.Preferences";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Editor editor = getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).edit();
        editor.putBoolean(PreferencesProvider.PREFERENCES_CHANGED, true);
        editor.commit();
        if (LauncherApplication.isScreenLarge()) {
            PreferenceGroup homescreen = (PreferenceGroup) findPreference("ui_homescreen");
            homescreen.removePreference(findPreference("ui_homescreen_grid"));
            homescreen.removePreference(findPreference("ui_homescreen_screen_padding_vertical"));
            homescreen.removePreference(findPreference("ui_homescreen_screen_padding_horizontal"));
            homescreen.removePreference(findPreference("ui_homescreen_indicator"));
            ((PreferenceGroup) findPreference("ui_drawer")).removePreference(findPreference("ui_drawer_indicator"));
        }
        Preference version = findPreference("application_version");
        version.setTitle(getString(R.string.application_name));
        String versionInfo = StatConstants.MTA_COOPERATION_TAG;
        try {
            versionInfo = getString(R.string.versionFormatter, new Object[]{getPackageManager().getPackageInfo(getPackageName(), 0).versionName, Integer.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode)});
        } catch (NameNotFoundException e) {
            Log.e(TAG, "找不到版本信息");
        }
        version.setSummary(new StringBuilder(String.valueOf(versionInfo)).append("\n").append(version.getSummary()).toString());
        findPreference("screen_info").setTitle(Launcher.getWidthPixels() + "x" + Launcher.getHeightPixels() + ", " + Launcher.getDensity());
    }
}
