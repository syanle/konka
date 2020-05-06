package com.konka.kkimplements.common;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class IniEditor {
    HashMap<String, String> map = new HashMap<>();

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0137 A[SYNTHETIC, Splitter:B:41:0x0137] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0156 A[SYNTHETIC, Splitter:B:53:0x0156] */
    public boolean loadFile(String fileName) {
        int indexValueEnd;
        String strValueTmp;
        BufferedReader reader = null;
        try {
            FileReader fileReader = new FileReader(new File(fileName));
            BufferedReader reader2 = new BufferedReader(fileReader);
            String strKey = null;
            while (true) {
                try {
                    String tempString = reader2.readLine();
                    if (tempString == null) {
                        reader2.close();
                        if (reader2 != null) {
                            try {
                                reader2.close();
                                BufferedReader bufferedReader = reader2;
                            } catch (IOException e) {
                                BufferedReader bufferedReader2 = reader2;
                            }
                        }
                    } else {
                        String str = tempString.trim();
                        if (!(str.isEmpty() || str.charAt(0) == '#' || str.charAt(0) == ';')) {
                            if (str.charAt(0) == '[') {
                                strKey = str.substring(1, str.indexOf(93)).trim();
                            } else if (str.contains("=")) {
                                int indexNameEnd = str.indexOf(61);
                                Log.d("DEBUG", "Str key : " + indexNameEnd);
                                String strName = str.substring(0, indexNameEnd).trim();
                                int indexValueStart = indexNameEnd + 1;
                                int indexValueEnd1 = str.indexOf(59);
                                int indexValueEnd2 = str.indexOf(35);
                                if (indexValueEnd1 == -1 || indexValueEnd2 == -1) {
                                    indexValueEnd = Math.max(indexValueEnd1, indexValueEnd2);
                                } else {
                                    indexValueEnd = Math.min(indexValueEnd1, indexValueEnd2);
                                }
                                if (indexValueEnd == -1) {
                                    strValueTmp = str.substring(indexValueStart);
                                } else {
                                    strValueTmp = str.substring(indexValueStart, indexValueEnd);
                                }
                                String strValue = strValueTmp.trim();
                                if (strKey != null) {
                                    String strKeyTmp = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(strKey)).append(":").toString())).append(strName).toString();
                                    this.map.put(strKeyTmp, strValue);
                                    System.out.printf("%s = %s\n", new Object[]{strKeyTmp, strValue});
                                } else {
                                    if (reader2 != null) {
                                        try {
                                            reader2.close();
                                        } catch (IOException e2) {
                                        }
                                    }
                                    BufferedReader bufferedReader3 = reader2;
                                    return false;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    reader = reader2;
                    try {
                        e.printStackTrace();
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e4) {
                            }
                        }
                        return true;
                    } catch (Throwable th) {
                        th = th;
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                    if (reader != null) {
                    }
                    throw th;
                }
            }
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            if (reader != null) {
            }
            return true;
        }
        return true;
    }

    public void unloadFile() {
        this.map.clear();
    }

    public String getValue(String key, String def) {
        String strValue = (String) this.map.get(key);
        if (strValue == null) {
            return def;
        }
        return strValue;
    }
}
