package hochschule.de.bachelorthesis.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Food.class}, version = 1)
public abstract class FoodDatabase extends RoomDatabase {

    // Only one database instance will be available for the whole APP.
    private static FoodDatabase INSTANCE;

    public abstract FoodDao foodDao();

    // Synchronize just to make sure no two instances will be created
    public static synchronized FoodDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    FoodDatabase.class, "word_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        /**
         * Called when the database is created for the first time. This is called after all the
         * tables are created.
         *
         * @param db The database.
         */
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    /**
     * This task is used to get a first population for the database.
     */
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private FoodDao foodDao;

        public PopulateDbAsyncTask(FoodDatabase db) {
            foodDao = db.foodDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            foodDao.insert(new Food("Main Text 1", "Sub Text 1", "Meta Text 1"));
            foodDao.insert(new Food("Main Text 2", "Sub Text 2", "Meta Text 2"));
            foodDao.insert(new Food("Main Text 3", "Sub Text 3", "Meta Text 3"));

            return null;
        }
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }
}
