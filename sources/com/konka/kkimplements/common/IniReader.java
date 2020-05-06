package com.konka.kkimplements.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class IniReader {
    private Properties properties;
    private String secion;
    private int sectionCounts;
    private Map<String, Properties> sections = new HashMap();

    public IniReader(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        read(reader);
        reader.close();
    }

    private void read(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                parseLine(line);
            } else {
                return;
            }
        }
    }

    private void parseLine(String line) {
        String line2 = stripComment(line);
        if (line2.matches("\\[.*\\]")) {
            this.secion = line2.replaceFirst("\\[(.*)\\]", "$1");
            this.properties = new Properties();
            this.sections.put(this.secion, this.properties);
            this.sectionCounts++;
        } else if (line2.matches(".*=.*") && this.properties != null) {
            int i = line2.indexOf(61);
            String name = line2.substring(0, i);
            String value = line2.substring(i + 1);
            this.properties.setProperty(name.trim(), value.trim());
        }
    }

    /* access modifiers changed from: protected */
    public String stripComment(String line) {
        int i = line.indexOf(35);
        if (i >= 0) {
            line = line.substring(0, i);
        }
        int i2 = line.indexOf(59);
        if (i2 >= 0) {
            line = line.substring(0, i2);
        }
        return line.trim();
    }

    public String getValue(String section, String key) {
        Properties p = (Properties) this.sections.get(section);
        if (p != null) {
            return p.getProperty(key);
        }
        System.out.println("[wf] get the section is null");
        return null;
    }

    public int getSectionCounts() {
        return this.sectionCounts;
    }

    public int getInt(String section, String key, int def) {
        Properties p = (Properties) this.sections.get(section);
        if (p == null) {
            return def;
        }
        String value = p.getProperty(key);
        if (value == null) {
            return def;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Result is not valid number-string, reture default.");
            return def;
        }
    }
}
