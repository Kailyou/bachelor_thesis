package hochschule.de.bachelorthesis.model;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.FoodDao;
import hochschule.de.bachelorthesis.room.FoodDatabase;

/**
 * Is not part of the architecture components library
 * but it is considered best practice.
 */
public class Repository {

    private FoodDao mFoodDao;
    private LiveData<List<Food>> mAllFood;
    private static Food mResult;

    public Repository(Application application) {
        FoodDatabase database = FoodDatabase.getDatabase(application);
        mFoodDao = database.foodDao();
        mAllFood = mFoodDao.getAllFood();
    }

    /**
     * API methods for the MeViewModel
     */


    /**
     * API methods that will be used by outside (View Model)
     * FoodViewModel
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
     * Classes for async tasks
     */

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
