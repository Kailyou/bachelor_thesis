package hochschule.de.bachelorthesis.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.Food;

public class AddFoodViewModel extends AndroidViewModel {
    private Repository repository;

    public AddFoodViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void insert(Food food) {
        repository.insert(food);
    }
}