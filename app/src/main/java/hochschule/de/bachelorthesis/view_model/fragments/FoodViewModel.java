package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;

public class FoodViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Food>> allFood;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
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

    // testing if this works
    public LiveData<Food> getFoodById(int id) {
        return  repository.getFoodById(id);
    }

    public LiveData<List<Food>> getAllFood() {
        return allFood;
    }
}
