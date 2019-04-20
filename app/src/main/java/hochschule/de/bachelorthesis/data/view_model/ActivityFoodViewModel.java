package hochschule.de.bachelorthesis.data.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import hochschule.de.bachelorthesis.other.FoodRepository;

public class ActivityFoodViewModel extends AndroidViewModel {

    private FoodRepository repository;

    public ActivityFoodViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodRepository(application);
    }
}
