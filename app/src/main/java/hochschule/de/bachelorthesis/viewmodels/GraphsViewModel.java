package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import java.util.List;

public class GraphsViewModel extends AndroidViewModel {

  private Repository mRepository;
  private LiveData<List<Food>> mAllFoods;
  private LiveData<List<Measurement>> mAllMeasurements;
  private LiveData<UserHistory> mUserHistoryLatest;


  public GraphsViewModel(@NonNull Application application) {
    super(application);

    mRepository = new Repository(application);
    mAllFoods = mRepository.getAllFoods();
    mAllMeasurements = mRepository.getAllMeasurements();
    mUserHistoryLatest = mRepository.getUserHistoryLatest();
  }

  /**
   * Gets all foods from the database.
   *
   * @return - A Live data list with all foods.
   */
  public LiveData<List<Food>> getAllFoods() {
    return mAllFoods;
  }

  public LiveData<List<Measurement>> getAllMeasurements() {
    return mAllMeasurements;
  }

  /**
   * Gets all measurements from the food objects from the database.
   *
   * @param foodId - The Id of the food.
   * @return - A live data list of measurements of the food.
   */
  public LiveData<List<Measurement>> getAllMeasurementsByFoodId(int foodId) {
    return mRepository.getAllMeasurementsByFoodId(foodId);
  }
}
