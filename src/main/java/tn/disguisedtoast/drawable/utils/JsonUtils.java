package tn.disguisedtoast.drawable.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class JsonUtils {

    public static String getValue(String key, String jsonFilePath){
        JsonParser jsonParser = new JsonParser();
        try{
            JsonObject object = jsonParser.parse(new FileReader(new File(jsonFilePath))).getAsJsonObject();
            return object.get(key).getAsString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
