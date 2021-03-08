package veronicadev.ecobot;

import org.json.JSONObject;

import java.io.*;

public class FileManager {
    @SuppressWarnings("unchecked")
    public static JSONObject readJSON(String path){
        JSONObject obj = null;
        InputStream input = FileManager.class.getClassLoader().getResourceAsStream(path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            //Read JSON file
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            obj = new JSONObject(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
