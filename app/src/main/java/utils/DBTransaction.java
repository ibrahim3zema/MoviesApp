package utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import pojo.Movies;

/**
 * Created by ibra on 4/20/2016.
 */
public class DBTransaction {
    DBHelper helper ;
    public DBTransaction(Context context) {
        helper = new DBHelper(context);
    }
    public long set_Fav(Movies movies) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(DBCreation.movies.COLUMN_POSTER_PATH, movies.getPoster_path());
        value.put(DBCreation.movies.COLUMN_OVERVIEW, movies.getOverview());
        value.put(DBCreation.movies.COLUMN_MOVIE_ID, movies.getId());
        value.put(DBCreation.movies.COLUMN_ORIGINAL_TITLE, movies.getOriginal_title());
        value.put(DBCreation.movies.COLUMN_VOTE_AVERAGE, movies.getVote_average());
        value.put(DBCreation.movies.COLUMN_RELEASE_DATE, movies.getRelease_date());
        long id = db.insert(DBCreation.movies.TABLE_NAME, null, value);
        db.close();
        return id;
    }

    public ArrayList<Movies> get_Fav() {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Movies> moviesList = new ArrayList<>();
        Cursor cursor = db.query(DBCreation.movies.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<String> posters = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Movies movies = new Movies();
                movies.setPoster_path(cursor.getString(1));
                movies.setOverview(cursor.getString(2));
                movies.setId(cursor.getInt(3));
                movies.setOriginal_title(cursor.getString(4));
                movies.setVote_average(cursor.getString(5));
                movies.setRelease_date(cursor.getString(6));
                posters.add(cursor.getString(1));
                moviesList.add(movies);

            }
        }
        db.close();
        return moviesList;
    }

    public long delete_Fav(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long deletedRow = db.delete(DBCreation.movies.TABLE_NAME, DBCreation.movies.COLUMN_MOVIE_ID + " =?", new String[]{id});
        db.close();
        return deletedRow;
    }

    public String get_IDs() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DBCreation.movies.COLUMN_MOVIE_ID};
        Cursor cursor = db.query(DBCreation.movies.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(DBCreation.movies.COLUMN_MOVIE_ID);
            String name = cursor.getString(index1);
            buffer.append(name + ",");
        }
        db.close();
        return buffer.toString();
    }

}
