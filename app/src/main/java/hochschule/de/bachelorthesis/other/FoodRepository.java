package hochschule.de.bachelorthesis.other;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import hochschule.de.bachelorthesis.room.Food;
import hochschule.de.bachelorthesis.room.FoodDao;
import hochschule.de.bachelorthesis.room.FoodDatabase;
import io.reactivex.Flowable;

/**
 * Is not part of the architecture components library
 * but it is considered best practice.
 */
public class FoodRepository {

    private FoodDao foodDao;
    private LiveData<List<Food>> allFood;

    public FoodRepository(Application application) {
        FoodDatabase database = FoodDatabase.getDatabase(application);
        this.foodDao = database.foodDao();
        this.allFood = this.foodDao.getAllFood();
    }

    /**
     * API methods that will be used by outside (View Model)
     */
    public void insert(Food food) {
        new InsertFoodAsyncTask(foodDao).execute(food);
    }

    public void update(Food food) {
        new UpdateFoodAsyncTask(foodDao).execute(food);
    }

    public void delete(Food food) {
        new DeleteFoodAsyncTask(foodDao).execute(food);
    }

    public void deleteAllFood() {
        new DeleteAllFoodAsyncTask(foodDao).execute();
    }

    // already executed on a background threat
    public LiveData<List<Food>> getAllFood() {
        return allFood;
    }

    /**
     * Classes for async tasks
     */
    private static class InsertFoodAsyncTask extends AsyncTask<Food, Void, Void> {
        private FoodDao mFoodDao;

        public InsertFoodAsyncTask(FoodDao mFoodDao) {
            this.mFoodDao = mFoodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mFoodDao.insert(foods[0]);
            return null;
        }
    }

    /*
    private static class GetFodoByIdTask extends AsyncTask<Food, Void, Void> {
        private FoodDao mFoodDao;

        public InsertFoodAsyncTask(FoodDao mFoodDao) {
            this.mFoodDao = mFoodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            mFoodDao.insert(foods[0]);
            return null;
        }
    }
    */

    private static class UpdateFoodAsyncTask extends AsyncTask<Food, Void, Void> {
        private FoodDao mFoodDao;

        public UpdateFoodAsyncTask(FoodDao mFoodDao) {
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

        public DeleteFoodAsyncTask(FoodDao mFoodDao) {
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

        public DeleteAllFoodAsyncTask(FoodDao mFoodDao) {
            this.mFoodDao = mFoodDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mFoodDao.deleteAllFood();
            return null;
        }
    }
}
