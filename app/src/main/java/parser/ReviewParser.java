package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import extra.ContVars;
import pojo.Review;

/**
 * Created by ibra on 4/12/2016.
 */
public class ReviewParser {
    static ContVars ContVars = new ContVars();
    public static List<Review> parseFeed(String content) {
        try {
            JSONObject arr = new JSONObject(content);
            JSONArray ar = arr.getJSONArray(ContVars.PAGE_CONTENT);
            List<Review> reviewList= new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Review review = new Review();
                review.setName(obj.getString(ContVars.AUTHOR));
                review.setComment(obj.getString(ContVars.CONTENT));
                reviewList.add(review);
            }

            return reviewList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
