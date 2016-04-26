package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import extra.ContVars;
import pojo.Movies;

public class MoviesParser {
    static ContVars ContVars = new ContVars();

    public static List<Movies> parseFeed(String content) {
        try {
            JSONObject arr = new JSONObject(content);
            JSONArray ar = arr.getJSONArray(ContVars.PAGE_CONTENT);
            List<Movies> filmsList = new ArrayList<>();
            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Movies film = new Movies();
                film.setPoster_path(obj.getString(ContVars.POSTER_PATH));
                film.setOverview(obj.getString(ContVars.OVERVIEW));
                film.setOriginal_title(obj.getString(ContVars.ORIGINAL_TITLE));
                film.setRelease_date(obj.getString(ContVars.RELEASE_DATE));
                film.setVote_average(obj.getString(ContVars.VOTE_AVERAGE));
                film.setVote_count(obj.getString(ContVars.VOTE_COUNT));
                film.setId(obj.getInt(ContVars.ID));
                filmsList.add(film);
            }

            return filmsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
