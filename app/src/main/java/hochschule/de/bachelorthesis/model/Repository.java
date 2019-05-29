package hochschule.de.bachelorthesis.model;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

import hochschule.de.bachelorthesis.room.MeasurementDao;
import hochschule.de.bachelorthesis.room.UserHistoryDao;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.FoodDao;
import hochschule.de.bachelorthesis.room.FoodDatabase;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

/**
 * Is not part of the architecture components library
 * but it is considered best practice.
 */
public class Repository {

    private MeasurementDao mMeasurementDao;
    private FoodDao mFoodDao;
    private UserHistoryDao mUserHistoryDao;

    private LiveData<List<Food>> mAllFood;
    private LiveData<List<Measurement>> mAllMeasurements;

    public Repository(Application application) {
        FoodDatabase database = FoodDatabase.getDatabase(application);
        mMeasurementDao = database.measurementDao();
        mFoodDao = database.foodDao();
        mUserHistoryDao = database.userHistoryDao();
        mAllFood = mFoodDao.getAllFood();
        mAllMeasurements = mMeasurementDao.getAllMeasurements();
    }


    // API methods that will be used by outside (View Model)

    /**
     * MEASUREMENT
     */

    public void insert(Measurement measurement) {
        new InsertMeasurementAsyncTask(mMeasurementDao).execute(measurement);
    }

    // already executed on a background thread because of live data
    public LiveData<List<Measurement>> getAllMeasurements() {
        return mAllMeasurements;
    }

    /**
     * FOOD
     */

    public void insert(Food food) {
        new InsertFoodAsyncTask(mFoodDao).execute(food);
    }

    public void update(Food food) {
        new UpdateFoodAsyncTask(mFoodDao).execute(food);
    }

    public void delete(Food food) {
        new DeleteFoodAsyncTask(mFoodDao).execute(food);
    }

    public void deleteAllFood() {
        new DeleteAllFoodAsyncTask(mFoodDao).execute();
    }

    // already executed on a background thread because of live data
    public LiveData<List<Food>> getAllFood() {
        return mAllFood;
    }

    public LiveData<Food> getFoodById(int id) { return mFoodDao.getFoodById(id); }

    /**
     * USER HISTORY
     */

    public void insert(UserHistory userHistory) {
        new InsertUserHistoryAsyncTask(mUserHistoryDao).execute(userHistory);
    }

    // public LiveData<UserHistory> getUserHistoryById(int id) { return mUserHistoryDao.getById(id); }
    public LiveData<UserHistory> getUserHistoryLatest() {
        return mUserHistoryDao.getLatest();
    }

    /**
     * Classes for async tasks
     */

    /* MEASUREMENT */

    private static class InsertMeasurementAsyncTask extends AsyncTask<Measurement, Void, Void> {
        private MeasurementDao mMeasurementDao;

        private InsertMeasurementAsyncTask(MeasurementDao mFoodDao) {
            this.mMeasurementDao = mFoodDao;
        }

        @Override
        protected Void doInBackground(Measurement... measurements) {
            mMeasurementDao.insert(measurements[0]);
            return null;
        }
    }

    /* FOOD */

    private static class InsertFoodAsyncTask extends AsyncTask<Food, Void, Void> {
        private FoodDao mFoodDao;

        private InsertFoodAsyncTask(FoodDao mFoodDao) {
            this.mFoodDao = mFoodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mFoodDao.insert(foods[0]);
            return null;
        }
    }

    private static class InsertUserHistoryAsyncTask extends AsyncTask<UserHistory, Void, Void> {
        private UserHistoryDao mUserHistoryDao;

        private InsertUserHistoryAsyncTask(UserHistoryDao userHistoryDao) {
            this.mUserHistoryDao = userHistoryDao;
        }

        @Override
        protected Void doInBackground(UserHistory... userHistories) {
            mUserHistoryDao.insert(userHistories[0]);
            return null;
        }
    }

    private static class UpdateFoodAsyncTask extends AsyncTask<Food, Void, Void> {
        private FoodDao mFoodDao;

        private UpdateFoodAsyncTask(FoodDao mFoodDao) {
            this.mFoodDao = mFoodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mFoodDao.update(foods[0]);
            return null;
        }
    }

    private static class DeleteFoodAsyncTask extends AsyncTask<Food, Void, Void> {
        private FoodDao mFoodDao;

        private DeleteFoodAsyncTask(FoodDao mFoodDao) {
            this.mFoodDao = mFoodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mFoodDao.delete(foods[0]);
            return null;
        }
    }

    private static class DeleteAllFoodAsyncTask extends AsyncTask<Void, Void, Void> {
        private FoodDao mFoodDao;

        private DeleteAllFoodAsyncTask(FoodDao mFoodDao) {
            this.mFoodDao = mFoodDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mFoodDao.deleteAllFood();
            return null;
        }
    }
}
