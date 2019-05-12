package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import hochschule.de.bachelorthesis.model.Repository;

public class FoodInfoOverviewViewModel extends AndroidViewModel {

    private Repository repository;

    public FoodInfoOverviewViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
}
