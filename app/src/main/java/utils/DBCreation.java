package utils;
import android.provider.BaseColumns;

/**
 * Created by ibra on 4/20/2016.
 */
public class DBCreation {
    public static class movies implements BaseColumns{
        public static final String TABLE_NAME="moviesDB";
        public static final String COLUMN_ID="id";
        public static final String COLUMN_POSTER_PATH="poster_path";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_MOVIE_ID="movie_id";
        public static final String COLUMN_ORIGINAL_TITLE ="original_title";
        public static final String COLUMN_VOTE_AVERAGE ="vote_average";
        public static final String COLUMN_RELEASE_DATE="release_date";
        public static final String CREATE_TABLE_MOVIE=
                "CREATE TABLE "+TABLE_NAME+"("
                       +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                       +COLUMN_POSTER_PATH+" TEXT, "
                       +COLUMN_OVERVIEW+" TEXT, "
                       +COLUMN_MOVIE_ID+" TEXT, "
                       + COLUMN_ORIGINAL_TITLE +" TEXT, "
                       + COLUMN_VOTE_AVERAGE +" INT, "
                       +COLUMN_RELEASE_DATE+" TEXT"
                       +")";
    }


}
