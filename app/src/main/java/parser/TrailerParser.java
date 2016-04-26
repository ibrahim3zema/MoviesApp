package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import extra.ContVars;
import pojo.Trailer;

/**
 * Created by ibra on 4/12/2016.
 */
public class TrailerParser {
    static ContVars ContVars = new ContVars();
    public static List<Trailer> parseFeed(String content) {
        try {
            JSONObject arr = new JSONObject(content);
            JSONArray ar = arr.getJSONArray(ContVars.PAGE_CONTENT);
            List<Trailer> trailerList= new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Trailer trailer = new Trailer();
                trailer.setName(obj.getString(ContVars.NAME));
                trailer.setKey(obj.getString(ContVars.KEY));
                trailerList.add(trailer);
            }

            return trailerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
