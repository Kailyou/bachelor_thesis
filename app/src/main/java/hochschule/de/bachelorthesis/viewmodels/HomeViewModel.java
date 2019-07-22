package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

  private Repository mRepository;

  public HomeViewModel(@NonNull Application application) {
    super(application);
    mRepository = new Repository(application);
  }

  /**
   * Gets the food by ID.
   *
   * @param id - The food's id.
   * @return - A live data object with the food.
   */
  public LiveData<Food> getFoodById(int id) {
    return mRepository.getFoodById(id);
  }

  public LiveData<List<Measurement>> getAllMeasurements() {
    return mRepository.getAllMeasurements();
  }
}
