package hochschule.de.bachelorthesis.view_model;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import hochschule.de.bachelorthesis.other.FoodRepository;
import hochschule.de.bachelorthesis.room.Food;

/**
 * Model, where the Activty/Fragment will have a reference to.
 * It won't have a reference to the food repository itself (wrapper).
 */
public class FoodViewModel extends AndroidViewModel {

    private FoodRepository repository;
    private LiveData<List<Food>> allFood;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodRepository(application);
        allFood = repository.getAllFood();
    }

    public void insert(Food food) {
        repository.insert(food);
    }

    public void update(Food food) {
        repository.update(food);
    }

    public void delete(Food food) {
        repository.delete(food);
    }

    public void deleteAllNotes() {
        repository.deleteAllFood();
    }

    public LiveData<List<Food>> getAllFood() {
        return allFood;
    }
}
