package legacy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class ConfigLoader {

    String fileContent;
    File file = new File("invoice-creator-config.json");
    Map<String, String> configValues = new HashMap<>();

    public ConfigLoader() {
        configValues.clear();
        load();
    }

    public String getString(String key) {
        return configValues.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(configValues.get(key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(configValues.get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(configValues.get(key));
    }

    public boolean contains(String key) {
        return configValues.containsKey(key);
    }

    public void load() {
        if (!file.exists()) {
            try {
                PrintWriter writer = new PrintWriter("invoice-creator-config.json", "UTF-8");
                writer.println("{\n  \"Name\": \"My Electronics\",\n  \"Address\": \"123 Dundas Street East Mississauga\",\n  \"Postal\": \"L5A 1W7\",\n  \"Email\": \"MyElectronics66@Gmail.com\",\n  \"Phone\": \"905-272-2777\",\n  \"Name1\": \"Easy Home\",\n  \"Address1\": \"123 Dundas Street East Mississauga\",\n  \"Postal1\": \"L5A 1W7\",\n  \"Email1\": \"MyElectronics66@Gmail.com\",\n  \"Phone1\": \"905-272-2777\",\n  \"InvoiceNO\": \"13644\",\n  \"Sales\": \"Zeshan\",\n  \"Tax\": \"0.13\"\n}\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(file.getPath())));

            JSONObject jsonObject = new JSONObject(fileContent);
            JSONArray jsonArray = jsonObject.names();

            for (int i = 0; i < jsonArray.length(); i++) {
                String key = jsonArray.getString(i);

                configValues.put(key, jsonObject.getString(key));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateConfig(String name, String address, String postal, String email, String phone, String name1, String address1, String postal1, String email1, String phone1, String invoice, String sales, String tax) {
        try {
            PrintWriter writer = new PrintWriter("invoice-creator-config.json", "UTF-8");
            writer.println("{\n  \"Name\": \"" + name
                    + "\",\n  \"Address\": \"" + address
                    + "\",\n  \"Postal\": \"" + postal
                    + "\",\n  \"Email\": \"" + email
                    + "\",\n  \"Phone\": \"" + phone
                    + "\",\n  \"Name1\": \"" + name1
                    + "\",\n  \"Address1\": \"" + address1
                    + "\",\n  \"Postal1\": \"" + postal1
                    + "\",\n  \"Email1\": \"" + email1
                    + "\",\n  \"Phone1\": \"" + phone1
                    + "\",\n  \"InvoiceNO\": \"" + invoice
                    + "\",\n  \"Sales\": \"" + sales
                    + "\",\n  \"Tax\": \"" + tax + "\"\n}");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        load();
    }
}
