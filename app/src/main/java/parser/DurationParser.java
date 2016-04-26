package parser;

import org.json.JSONException;
import org.json.JSONObject;

import pojo.Duration;

/**
 * Created by ibra on 4/19/2016.
 */
public class DurationParser {
    public static String parseFeed(String content) {
        try

        {
            JSONObject object = new JSONObject(content);
            Duration duration = new Duration();
            duration.setDuration(object.getString("runtime"));
            String time = (duration.getDuration() + " " + "min");
            return time ;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }

    }
}
