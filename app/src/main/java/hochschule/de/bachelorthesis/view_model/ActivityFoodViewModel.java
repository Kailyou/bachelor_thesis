package hochschule.de.bachelorthesis.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import hochschule.de.bachelorthesis.model.Repository;

public class ActivityFoodViewModel extends AndroidViewModel {

    private Repository repository;

    public ActivityFoodViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
}
