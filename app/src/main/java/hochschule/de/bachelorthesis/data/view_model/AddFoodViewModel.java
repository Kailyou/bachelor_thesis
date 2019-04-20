package hochschule.de.bachelorthesis.data.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import hochschule.de.bachelorthesis.other.FoodRepository;
import hochschule.de.bachelorthesis.room.Food;

public class AddFoodViewModel extends AndroidViewModel {
    private FoodRepository repository;

    public AddFoodViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodRepository(application);
    }

    public void insert(Food food) {
        repository.insert(food);
    }
}
