package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConfigLoader {

	String fileContent;
	File file = new File("invoice-creator-config.json");
	Map<String, String> configValues = new HashMap<>();

	public ConfigLoader() {
		if (!file.exists()) {
			try {
				PrintWriter writer = new PrintWriter("invoice-creator-config.json", "UTF-8");
				writer.println("{\n  \"Name\": \"My Electronics\",\n  \"Address\": \"123 Dundas Street East Mississauga\",\n  \"Postal\": \"L5A 1W7\",\n  \"Email\": \"MyElectronics66@Gmail.com\",\n  \"Phone\": \"905-272-2777\",\n  \"Name1\": \"My Electronics\",\n  \"Address1\": \"123 Dundas Street East Mississauga\",\n  \"Postal1\": \"L5A 1W7\",\n  \"Email1\": \"MyElectronics66@Gmail.com\",\n  \"Phone1\": \"905-272-2777\",\n  \"Tax\": \"1.13\"\n}");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fileContent = FileUtils.readFileToString(file, "utf-8");

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
	
	public void updateConfig(String name, String address, String postal, String email, String phone, String name1, String address1, String postal1, String email1, String phone1, String tax) {
		try {
			PrintWriter writer = new PrintWriter("invoice-creator-config.json", "UTF-8");
			writer.println("{\n  \"Name\": \"" + name + "\",\n  \"Address\": \"" + address + "\",\n  \"Postal\": \"" + postal + "\",\n  \"Email\": \"" + email + "\",\n  \"Phone\": \"" + phone + "\",\n  \"Name1\": \"" + name1 + "\",\n  \"Address1\": \"" + address1 + "\",\n  \"Postal1\": \"" + postal1 + "\",\n  \"Email1\": \"" + email1 + "\",\n  \"Phone1\": \"" + phone1 + "\",\n  \"Tax\": \"" + tax + "\"\n}");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
