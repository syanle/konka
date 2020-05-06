package com.cyanogenmod.trebuchet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import com.konka.android.tv.KKCommonManager;
import com.konka.android.tv.KKCommonManager.EN_KK_LAUNCHER_CONFIG_FILE_TYPE;
import com.konka.launcherblacklist.BlackListFilter;
import com.tencent.stat.common.StatConstants;
import com.umeng.common.a;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class AllAppsList {
    public static final int DEFAULT_APPLICATIONS_NUMBER = 42;
    private static final String TAG = AllAppsList.class.getSimpleName();
    private Comparator<String> StringComparator = new Comparator<String>() {
        public int compare(String lhs, String rhs) {
            Log.d("AllAppsList", "compare--->" + lhs + "====" + rhs);
            return rhs.compareTo(lhs);
        }
    };
    private final String XML_package = a.d;
    public ArrayList<ApplicationInfo> added = new ArrayList<>(42);
    public ArrayList<ApplicationInfo> data = new ArrayList<>(42);
    private List<String> mAppBlackList = new ArrayList();
    private BlackListFilter mBlackListFilter;
    private IconCache mIconCache;
    private Comparator<ComponentName> mPackageOnlyComparator = new Comparator<ComponentName>() {
        public int compare(ComponentName lhs, ComponentName rhs) {
            if (StatConstants.MTA_COOPERATION_TAG.equals(lhs.getClassName())) {
                return rhs.getPackageName().compareTo(lhs.getPackageName());
            }
            return rhs.compareTo(lhs);
        }
    };
    public ArrayList<ApplicationInfo> modified = new ArrayList<>();
    public ArrayList<ApplicationInfo> removed = new ArrayList<>();

    public class DhDefaultHandler extends DefaultHandler {
        private List<String> apps = null;
        private String preTag = null;

        public DhDefaultHandler() {
        }

        public List<String> getApps() {
            return this.apps;
        }

        public void startDocument() throws SAXException {
            this.apps = new ArrayList();
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            a.d.equals(qName);
            this.preTag = qName;
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            a.d.equals(qName);
            this.preTag = null;
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            if (this.preTag != null) {
                String content = new String(ch, start, length);
                if (a.d.equals(this.preTag)) {
                    this.apps.add(content);
                }
            }
        }
    }

    public AllAppsList(IconCache iconCache, Context context) {
        this.mIconCache = iconCache;
        this.mBlackListFilter = BlackListFilter.getInstance(context);
        String fileName = null;
        try {
            fileName = KKCommonManager.getInstance(context).getLauncherConfigurationFileName(EN_KK_LAUNCHER_CONFIG_FILE_TYPE.APP_BLACK_LIST, context.getPackageName());
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
        }
        if (fileName == null || !new File(fileName).exists()) {
            Log.d(TAG, "the appblacklist is not found!!!");
        } else {
            this.mAppBlackList = getAppBlackList(fileName);
        }
    }

    public void add(ApplicationInfo info) {
        Log.v("appslistadd", info.componentName.getPackageName());
        boolean findActivity = findActivity(this.data, info.componentName);
        Log.d("appslistadd", "findActivity = " + findActivity);
        if (!findActivity) {
            if (findBlackedPackage(info.componentName)) {
                Log.d("AllAppsList", "the activity is blacked=====" + info.componentName);
                return;
            }
            Log.d("AllAppsList", "the activity is not blacked=====" + info.componentName);
            if (!findBlackedPackageTencent(info.componentName.getPackageName())) {
                this.data.add(info);
                this.added.add(info);
            }
        }
    }

    public void clear() {
        this.data.clear();
        this.added.clear();
        this.removed.clear();
        this.modified.clear();
    }

    public int size() {
        return this.data.size();
    }

    public ApplicationInfo get(int index) {
        return (ApplicationInfo) this.data.get(index);
    }

    public void addPackage(Context context, String packageName) {
        List<ResolveInfo> matches = findActivitiesForPackage(context, packageName);
        if (matches.size() > 0) {
            for (ResolveInfo info : matches) {
                add(new ApplicationInfo(context.getPackageManager(), info, this.mIconCache, null));
            }
        }
    }

    public void removePackage(String packageName) {
        List<ApplicationInfo> data2 = this.data;
        for (int i = data2.size() - 1; i >= 0; i--) {
            ApplicationInfo info = (ApplicationInfo) data2.get(i);
            if (packageName.equals(info.intent.getComponent().getPackageName())) {
                this.removed.add(info);
                data2.remove(i);
            }
        }
        this.mIconCache.flush();
    }

    public void updatePackage(Context context, String packageName) {
        List<ResolveInfo> matches = findActivitiesForPackage(context, packageName);
        if (matches.size() > 0) {
            for (int i = this.data.size() - 1; i >= 0; i--) {
                ApplicationInfo applicationInfo = (ApplicationInfo) this.data.get(i);
                ComponentName component = applicationInfo.intent.getComponent();
                if (packageName.equals(component.getPackageName()) && !findActivity(matches, component)) {
                    this.removed.add(applicationInfo);
                    this.mIconCache.remove(component);
                    this.data.remove(i);
                }
            }
            for (ResolveInfo info : matches) {
                ApplicationInfo applicationInfo2 = findApplicationInfoLocked(info.activityInfo.applicationInfo.packageName, info.activityInfo.name);
                if (applicationInfo2 == null) {
                    add(new ApplicationInfo(context.getPackageManager(), info, this.mIconCache, null));
                } else {
                    this.mIconCache.remove(applicationInfo2.componentName);
                    this.mIconCache.getTitleAndIcon(applicationInfo2, info, null);
                    this.modified.add(applicationInfo2);
                }
            }
            return;
        }
        for (int i2 = this.data.size() - 1; i2 >= 0; i2--) {
            ApplicationInfo applicationInfo3 = (ApplicationInfo) this.data.get(i2);
            ComponentName component2 = applicationInfo3.intent.getComponent();
            if (packageName.equals(component2.getPackageName())) {
                this.removed.add(applicationInfo3);
                this.mIconCache.remove(component2);
                this.data.remove(i2);
            }
        }
    }

    public static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        mainIntent.setPackage(packageName);
        List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<>();
    }

    public static boolean findActivity(List<ResolveInfo> apps, ComponentName component) {
        String className = component.getClassName();
        for (ResolveInfo info : apps) {
            if (info.activityInfo.name.equals(className)) {
                return true;
            }
        }
        return false;
    }

    private static boolean findActivity(ArrayList<ApplicationInfo> apps, ComponentName component) {
        Iterator it = apps.iterator();
        while (it.hasNext()) {
            ApplicationInfo info = (ApplicationInfo) it.next();
            if (info.componentName.getPackageName().equals(component.getPackageName()) && info.componentName.getClassName().equals(component.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public boolean findBlackedPackage(ComponentName component) {
        Log.v("mName", component.getClassName());
        ArrayList<ComponentName> blackListSaved = this.mBlackListFilter.getAppsBlackList();
        Log.v("mName", new StringBuilder(String.valueOf(blackListSaved.size())).toString());
        return (!this.mAppBlackList.isEmpty() && Collections.binarySearch(this.mAppBlackList, component.getClassName()) >= 0) || Collections.binarySearch(blackListSaved, component, this.mPackageOnlyComparator) >= 0;
    }

    private boolean findBlackedPackageTencent(String name) {
        List<String> tencentBalckList = new ArrayList<>();
        Log.v("tencentTV", name);
        try {
            tencentBalckList = getXmlcontent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String mName : tencentBalckList) {
            Log.v("tencentblack", mName);
            if (mName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private ApplicationInfo findApplicationInfoLocked(String packageName, String className) {
        Iterator it = this.data.iterator();
        while (it.hasNext()) {
            ApplicationInfo info = (ApplicationInfo) it.next();
            ComponentName component = info.intent.getComponent();
            if (packageName.equals(component.getPackageName()) && className.equals(component.getClassName())) {
                return info;
            }
        }
        return null;
    }

    private List<String> getXmlcontent() throws Exception {
        new ArrayList();
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        InputStream is = getClass().getResourceAsStream("/assets/appblackxml.xml");
        DhDefaultHandler dh = new DhDefaultHandler();
        parser.parse(is, dh);
        is.close();
        return dh.getApps();
    }

    private List<String> getAppBlackList(String fileName) {
        Log.d("AllAppsList", "the file name=====" + fileName);
        List<String> appNameList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader buf = new BufferedReader(fr);
            try {
                String line = buf.readLine();
                if (line != null) {
                    Log.v("line", line);
                }
                String s = getRightNameString(line);
                if (s != null) {
                    Log.v("s", s);
                }
                int i = 0;
                while (line != null) {
                    if (s != null) {
                        appNameList.add(i, s);
                        i++;
                    }
                    line = buf.readLine();
                    s = getRightNameString(line);
                    if (s != null) {
                        Log.v("MS", s);
                    }
                }
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            try {
                buf.close();
                fr.close();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
            Collections.sort(appNameList);
            Log.d("AllAppsList", "the black list=====" + appNameList);
            FileReader fileReader = fr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("AllAppsList", "FileNotFoundException====" + fileName);
        }
        return appNameList;
    }

    private String getRightNameString(String strLine) {
        if (strLine == null) {
            return null;
        }
        int iEnd = strLine.indexOf(35);
        if (iEnd > 0) {
            return strLine.substring(0, iEnd);
        }
        if (iEnd < 0) {
            return strLine;
        }
        return null;
    }
}
