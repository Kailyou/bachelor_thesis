package hochschule.de.bachelorthesis.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.Samples;

@Database(entities = {Food.class, Measurement.class,
    UserHistory.class}, version = 1, exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {

  // Only one database instance will be available for the whole APP.
  private static FoodDatabase INSTANCE;

  public abstract MeasurementDao measurementDao();

  public abstract FoodDao foodDao();

  public abstract UserHistoryDao userHistoryDao();

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

    private PopulateDbAsyncTask(FoodDatabase db) {
      foodDao = db.foodDao();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      foodDao.insert(Samples.getGlucose());
      foodDao.insert(Samples.getApple());
      foodDao.insert(Samples.getPizza());
      foodDao.insert(Samples.getCoke());

      return null;
    }
  }

  public static void destroyInstance() {
    INSTANCE = null;
  }
}
