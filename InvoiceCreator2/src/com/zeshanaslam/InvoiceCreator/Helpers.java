package com.zeshanaslam.InvoiceCreator;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import legacy.DataDB;
import legacy.objects.InputObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Future;

public class Helpers {

    private Browser browser;
    private DataDB dataDB;

    public Helpers(Browser browser) {
        this.browser = browser;
        dataDB = new DataDB();
    }

    public String getColumns() {
        return "[\n" +
                "  {\"name\":\"ID\",\"title\":\"ID\",\"breakpoints\":\"xs sm\",\"type\":\"number\",\"style\":{\"width\":80,\"maxWidth\":80}},\n" +
                "  {\"name\":\"Date\",\"title\":\"Date\",\"type\":\"date\",\"breakpoints\":\"xs sm md\",\"formatString\":\"DD MMM YYYY\"},\n" +
                "  {\"name\":\"Store\",\"title\":\"Store\"},\n" +
                "  {\"name\":\"Model\",\"title\":\"Model\"},\n" +
                "  {\"name\":\"Serial\",\"title\":\"Serial\"},\n" +
                "  {\"name\":\"Desc\",\"title\":\"Description\"},\n" +
                "  {\"name\":\"Price\",\"title\":\"Price\"},\n" +
                "  {\"name\":\"Status\",\"title\":\"Status\"},\n" +
                "  {\"name\":\"Manage\",\"title\":\"Manage\"}\n" +
                "]";
    }
    public String loadTable() {
        JSONArray jsonArray = new JSONArray();

        for (InputObject inputObject: dataDB.getInputs("All", "")) {
            JSONObject item = new JSONObject();

            item.put("ID", inputObject.ID);
            item.put("Date", inputObject.date);
            item.put("Store", inputObject.store);
            item.put("Model", inputObject.model);
            item.put("Serial", inputObject.serial);
            item.put("Desc", inputObject.desc);
            item.put("Price", inputObject.price);
            item.put("Status", inputObject.status);
            item.put("Manage", "<button onclick=\"edit('" + inputObject.ID + "');\" class=\"btn btn-primary btn-xs\"><span class=\"glyphicon glyphicon-pencil\"></span></button>"/* +
                    "<button onclick=\"delete('" + inputObject.ID + "');\" class=\"btn btn-danger btn-xs\"><span class=\"glyphicon glyphicon-trash\"></span></button>"*/);

            jsonArray.put(item);
        }

        return jsonArray.toString();
    }


}
