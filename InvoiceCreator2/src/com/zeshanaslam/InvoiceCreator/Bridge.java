package com.zeshanaslam.InvoiceCreator;

import legacy.DataDB;
import legacy.Exporter;
import legacy.objects.InputObject;

import java.io.File;
import java.nio.file.Paths;

public class Bridge {

    public String getPath() {
        return "file:///" + Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "data";
    }

    public void log(String text) {
        System.out.println(text);
    }

    public String getConfigValue(String key) {
        return Main.configLoader.getString(key);
    }

    public InputObject getInputObject(String id) {
        return new DataDB().getInputs("ID", id).get(0);
    }

    public boolean startsWith(String value, String sub) {
        return value.startsWith(sub);
    }

    public void saveEdit(String ID, String date, String store, String modal, String snumber, String desc, String price, String status) {
        new DataDB().updateInputString(ID.trim(), date.trim(), store.trim(), modal.trim(), snumber.trim(), desc.trim(), Double.valueOf(price.trim()), status.trim());
    }

    public void create(String date, String store, String modal, String snumber, String desc, String price, String status) {
        new DataDB().createInput(date.trim(), store.trim(), modal.trim(), snumber.trim(), desc.trim(), Double.valueOf(price.trim()), status.trim());
    }

    public void saveSettings(String name, String address, String postal, String email, String phone, String name1, String address1, String postal1, String email1, String phone1, String invoice, String sales, String tax) {
        Main.configLoader.updateConfig(name.trim(), address.trim(), postal.trim(), email.trim(), phone.trim(), name1.trim(), address1.trim(), postal1.trim(), email1.trim(), phone1.trim(), invoice.trim(), sales.trim(), tax.trim());
    }

    public void reload() {
        Main.browser.executeJavaScript("location.reload();");
    }

    public void export(int type, String from, String to) {
        System.out.println(type);
        new Exporter(type, from, to);
    }
}
