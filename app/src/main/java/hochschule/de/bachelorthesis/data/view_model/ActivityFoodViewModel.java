package hochschule.de.bachelorthesis.data.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import hochschule.de.bachelorthesis.data.Repository;

public class ActivityFoodViewModel extends AndroidViewModel {

    private Repository repository;

    public ActivityFoodViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
}
