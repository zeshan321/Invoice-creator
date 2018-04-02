package legacy.objects;

import org.json.JSONObject;

public class InputObject {

    public String ID;
    public String store;
    public String date;
    public String model;
    public String serial;
    public String desc;
    public String price;
    public String status;

    public InputObject(String ID, String store, String date, String modal, String serial, String desc, String price, String status) {
        this.ID = ID;
        this.store = store;
        this.date = date;
        this.model = modal;
        this.serial = serial;
        this.desc = desc;
        this.price = price;
        this.status = status;
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }
}