package hochschule.de.bachelorthesis.view_model.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import hochschule.de.bachelorthesis.model.Repository;

public class FoodInfoViewModel extends AndroidViewModel {

    private Repository repository;

    public FoodInfoViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
}
