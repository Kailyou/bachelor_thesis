package hochschule.de.bachelorthesis.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import hochschule.de.bachelorthesis.other.FoodRepository;
import hochschule.de.bachelorthesis.room.Food;
import io.reactivex.Flowable;

public class ActivityFoodViewModel extends AndroidViewModel {

    private FoodRepository repository;

    public ActivityFoodViewModel(@NonNull Application application) {
        super(application);
        repository = new FoodRepository(application);
    }
}
