package hochschule.de.bachelorthesis.model;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

import hochschule.de.bachelorthesis.room.daos.MeasurementDao;
import hochschule.de.bachelorthesis.room.daos.UserHistoryDao;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.daos.FoodDao;
import hochschule.de.bachelorthesis.room.FoodDatabase;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

/**
 * Is not part of the architecture components library but it is considered best practice.
 */
public class Repository {

    private MeasurementDao mMeasurementDao;
    private FoodDao mFoodDao;
    private UserHistoryDao mUserHistoryDao;

    public Repository(Application application) {
        FoodDatabase database = FoodDatabase.getDatabase(application);
        mMeasurementDao = database.measurementDao();
        mFoodDao = database.foodDao();
        mUserHistoryDao = database.userHistoryDao();
    }

    // API methods that will be used by outside (View Model)

    /**
     * MEASUREMENT
     */

    public void insert(Measurement measurement) {
        new InsertMeasurementAsyncTask(mMeasurementDao).execute(measurement);
    }

    public void update(Measurement measurement) {
        new UpdateMeasurementAsyncTask(mMeasurementDao).execute(measurement);
    }

    public void delete(Measurement measurement) {
        new DeleteMeasurementAsyncTask(mMeasurementDao).execute(measurement);
    }

    // already executed on a background thread because of live data
    public LiveData<List<Measurement>> getAllMeasurements() {
        return mMeasurementDao.getAllMeasurements();
    }

    // already executed on a background thread because of live data
    public LiveData<List<Measurement>> getAllMeasurementsByFoodId(int id) {
        return mMeasurementDao.getAllMeasurementsByFoodId(id);
    }

    // already executed on a background thread because of live data
    public LiveData<Measurement> getMeasurementById(int id) {
        return mMeasurementDao.getMeasurementById(id);
    }

    public LiveData<Integer> getMeasurementAmountRowsByFoodId(int foodId) {
        return mMeasurementDao.getRowCount(foodId);
    }

    public void deleteAllMeasurementsWithId(int id) {
        new DeleteAllMeasurementsWithIdAsyncTask(mMeasurementDao).execute(id);
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
    public LiveData<List<Food>> getAllFoods() {
        return mFoodDao.getAllFoods();
    }

    public LiveData<Food> getFoodById(int id) {
        return mFoodDao.getFoodById(id);
    }

    public LiveData<Food> getFoodByFoodNameAndBrandName(String foodName, String brandName) {
        return mFoodDao.getFoodByNameAndBrandName(foodName, brandName);
    }

    /**
     * USER HISTORY
     */

    public void insert(UserHistory userHistory) {
        new InsertUserHistoryAsyncTask(mUserHistoryDao).execute(userHistory);
    }

    // Already background
    public LiveData<UserHistory> getUserHistoryLatest() {
        return mUserHistoryDao.getLatest();
    }


    /**
     * Classes for async tasks
     */

    /* MEASUREMENT */

    private static class InsertMeasurementAsyncTask extends AsyncTask<Measurement, Void, Void> {

        private MeasurementDao mMeasurementDao;

        private InsertMeasurementAsyncTask(MeasurementDao foodDao) {
            mMeasurementDao = foodDao;
        }

        @Override
        protected Void doInBackground(Measurement... measurements) {
            mMeasurementDao.insert(measurements[0]);
            return null;
        }
    }

    private static class UpdateMeasurementAsyncTask extends AsyncTask<Measurement, Void, Void> {

        private MeasurementDao mMeasurementDao;

        private UpdateMeasurementAsyncTask(MeasurementDao measurementDao) {
            mMeasurementDao = measurementDao;
        }

        @Override
        protected Void doInBackground(Measurement... measurements) {
            mMeasurementDao.update(measurements[0]);
            return null;
        }
    }

    private static class DeleteMeasurementAsyncTask extends AsyncTask<Measurement, Void, Void> {

        private MeasurementDao mMeasurementDao;

        private DeleteMeasurementAsyncTask(MeasurementDao measurementDao) {
            mMeasurementDao = measurementDao;
        }

        @Override
        protected Void doInBackground(Measurement... measurements) {
            mMeasurementDao.delete(measurements[0]);
            return null;
        }
    }

    private static class DeleteAllMeasurementsWithIdAsyncTask extends AsyncTask<Integer, Void, Void> {

        private MeasurementDao mMeasurementDao;

        private DeleteAllMeasurementsWithIdAsyncTask(MeasurementDao measurementDao) {
            mMeasurementDao = measurementDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mMeasurementDao.deleteAllMeasurementsWithFoodId(integers[0]);
            return null;
        }
    }



    /* FOOD */

    private static class InsertFoodAsyncTask extends AsyncTask<Food, Void, Void> {

        private FoodDao mFoodDao;

        private InsertFoodAsyncTask(FoodDao foodDao) {
            mFoodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mFoodDao.insert(foods[0]);
            return null;
        }
    }


    private static class UpdateFoodAsyncTask extends AsyncTask<Food, Void, Void> {

        private FoodDao mFoodDao;

        private UpdateFoodAsyncTask(FoodDao foodDao) {
            mFoodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mFoodDao.update(foods[0]);
            return null;
        }
    }

    private static class DeleteFoodAsyncTask extends AsyncTask<Food, Void, Void> {

        private FoodDao mFoodDao;

        private DeleteFoodAsyncTask(FoodDao foodDao) {
            mFoodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mFoodDao.delete(foods[0]);
            return null;
        }
    }

    private static class DeleteAllFoodAsyncTask extends AsyncTask<Void, Void, Void> {

        private FoodDao mFoodDao;

        private DeleteAllFoodAsyncTask(FoodDao foodDao) {
            mFoodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mFoodDao.deleteAllFoods();
            return null;
        }
    }

    /* User History */

    private static class InsertUserHistoryAsyncTask extends AsyncTask<UserHistory, Void, Void> {

        private UserHistoryDao mUserHistoryDao;

        private InsertUserHistoryAsyncTask(UserHistoryDao userHistoryDao) {
            mUserHistoryDao = userHistoryDao;
        }

        @Override
        protected Void doInBackground(UserHistory... userHistories) {
            mUserHistoryDao.insert(userHistories[0]);
            return null;
        }
    }
}
