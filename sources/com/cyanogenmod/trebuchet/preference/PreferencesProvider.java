package com.cyanogenmod.trebuchet.preference;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import com.cyanogenmod.trebuchet.AppsCustomizePagedView.TransitionEffect;
import com.cyanogenmod.trebuchet.LauncherApplication;
import com.cyanogenmod.trebuchet.Workspace;
import com.konka.ios7launcher.R;

public final class PreferencesProvider {
    public static final String PREFERENCES_CHANGED = "preferences_changed";
    public static final String PREFERENCES_KEY = "com.konka.ios7launcher_preferences";

    public static class Application {
    }

    public static class Interface {

        public static class Dock {
        }

        public static class Drawer {

            public static class Indicator {
                public static boolean getShowScrollingIndicator(Context context) {
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_drawer_indicator_enable", true);
                }

                public static boolean getFadeScrollingIndicator(Context context) {
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_drawer_indicator_fade", true);
                }
            }

            public static class Scrolling {
                public static TransitionEffect getTransitionEffect(Context context, String def) {
                    return TransitionEffect.valueOf(context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getString("ui_drawer_scrolling_transition_effect", def));
                }

                public static boolean getFadeInAdjacentScreens(Context context) {
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_drawer_scrolling_fade_adjacent_screens", false);
                }
            }

            public static boolean getJoinWidgetsApps(Context context) {
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_drawer_widgets_join_apps", false);
            }
        }

        public static class General {
            public static boolean getAutoRotate(Context context, boolean def) {
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_general_orientation", def);
            }
        }

        public static class Homescreen {

            public static class Indicator {
                public static boolean getShowScrollingIndicator(Context context) {
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_indicator_enable", true);
                }

                public static boolean getFadeScrollingIndicator(Context context) {
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_indicator_fade", false);
                }

                public static boolean getShowDockDivider(Context context) {
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_indicator_background", true);
                }

                public static boolean getUseHomescreenIconRes(Context context) {
                    boolean def;
                    try {
                        def = context.getResources().getBoolean(R.bool.config_use_homescreen_icon_res);
                    } catch (NotFoundException e) {
                        def = false;
                    }
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_indicator_use_icon_res", def);
                }
            }

            public static class Scrolling {
                public static boolean getScrollWallpaper(Context context) {
                    boolean def;
                    try {
                        def = context.getResources().getBoolean(R.bool.config_scroll_wallpaper);
                    } catch (NotFoundException e) {
                        def = false;
                    }
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_scrolling_scroll_wallpaper", def);
                }

                public static Workspace.TransitionEffect getTransitionEffect(Context context, String def) {
                    return Workspace.TransitionEffect.valueOf(context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getString("ui_homescreen_scrolling_transition_effect", def));
                }

                public static boolean getFadeInAdjacentScreens(Context context, boolean def) {
                    return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_scrolling_fade_adjacent_screens", def);
                }
            }

            public static boolean setNumberHomescreens(Context context, int number) {
                if (number <= 0) {
                    return false;
                }
                Editor editor = context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).edit();
                editor.putInt("ui_homescreen_screens", number);
                return editor.commit();
            }

            public static int getNumberHomescreens(Context context) {
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getInt("ui_homescreen_screens", context.getResources().getInteger(R.integer.config_workspace_number_homescreen));
            }

            public static int getDefaultHomescreen(Context context) {
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getInt("ui_homescreen_default_screen", context.getResources().getInteger(R.integer.default_workspace_page));
            }

            public static int getCellCountX(Context context, int def) {
                try {
                    return Integer.parseInt(context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getString("ui_homescreen_grid", "0|" + def).split("\\|")[1]);
                } catch (NumberFormatException e) {
                    return def;
                }
            }

            public static int getCellCountY(Context context, int def) {
                try {
                    return Integer.parseInt(context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getString("ui_homescreen_grid", new StringBuilder(String.valueOf(def)).append("|0").toString()).split("\\|")[0]);
                } catch (NumberFormatException e) {
                    return def;
                }
            }

            public static int getScreenPaddingVertical(Context context) {
                return (int) (((float) context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getInt("ui_homescreen_screen_padding_vertical", 0)) * 3.0f * LauncherApplication.getScreenDensity());
            }

            public static int getScreenPaddingHorizontal(Context context) {
                return (int) (((float) context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getInt("ui_homescreen_screen_padding_horizontal", 0)) * 3.0f * LauncherApplication.getScreenDensity());
            }

            public static boolean getShowSearchBar(Context context) {
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_general_search", false);
            }

            public static boolean getResizeAnyWidget(Context context) {
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_general_resize_any_widget", false);
            }

            public static boolean getHideIconLabels(Context context) {
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_general_hide_icon_labels", false);
            }

            public static boolean getHomescreenAllowAdd(Context context) {
                boolean def;
                try {
                    def = context.getResources().getBoolean(R.bool.config_allow_homescreen_add);
                } catch (NotFoundException e) {
                    def = true;
                }
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_general_allow_add", def);
            }

            public static boolean getSearchScreenEnable(Context context) {
                boolean def;
                try {
                    def = context.getResources().getBoolean(R.bool.config_searchScreenEnable);
                } catch (NotFoundException e) {
                    def = true;
                }
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_general_allow_searchscreen", def);
            }

            public static boolean getEnforceLockItemPolicy(Context context) {
                boolean def;
                try {
                    def = context.getResources().getBoolean(R.bool.config_enforceLockItemPolicy);
                } catch (NotFoundException e) {
                    def = true;
                }
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_general_enforce_lock_item_policy", def);
            }

            public static boolean getScaleOnItemSelect(Context context) {
                boolean def;
                try {
                    def = context.getResources().getBoolean(R.bool.config_scaleOnItemSelect);
                } catch (NotFoundException e) {
                    def = false;
                }
                return context.getSharedPreferences(PreferencesProvider.PREFERENCES_KEY, 0).getBoolean("ui_homescreen_general_scale_on_item_select", def);
            }
        }

        public static class Icons {
        }
    }
}
