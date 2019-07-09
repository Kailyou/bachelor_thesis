package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import hochschule.de.bachelorthesis.model.GraphModelSingle;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import java.util.List;

public class GraphsViewModel extends AndroidViewModel {

  // Database
  private Repository mRepository;
  private LiveData<List<Food>> mAllFoods;
  private LiveData<List<Measurement>> mAllMeasurements;

  // Model
  private GraphModelSingle mGraphModel;


  public GraphsViewModel(@NonNull Application application) {
    super(application);

    mRepository = new Repository(application);
    mAllFoods = mRepository.getAllFoods();
    mAllMeasurements = mRepository.getAllMeasurements();

    mGraphModel = new GraphModelSingle();
  }


  /* FOOD */

  /**
   * Gets all foods from the database.
   *
   * @return - A Live data list with all foods.
   */
  public LiveData<List<Food>> getAllFoods() {
    return mAllFoods;
  }

  /**
   * @param foodName Name of the food
   * @param brandName Brand name of the food
   * @return A live data object with the food
   */
  public LiveData<Food> getFoodByFoodNameAndBrandName(String foodName, String brandName) {
    return mRepository.getFoodByFoodNameAndBrandName(foodName, brandName);
  }


  /* MEASUREMENT */

  /**
   * Gets all measurements from the food objects from the database.
   *
   * @param foodId - The Id of the food.
   * @return - A live data list of measurements of the food.
   */
  public LiveData<List<Measurement>> getAllMeasurementsByFoodId(int foodId) {
    return mRepository.getAllMeasurementsByFoodId(foodId);
  }


  /* GETTER */
  public GraphModelSingle getGraphSingleModel() {
    return mGraphModel;
  }

}
