package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import hochschule.de.bachelorthesis.model.Repository;

public class FoodInfoDataViewModel extends AndroidViewModel {

    private Repository repository;

    public FoodInfoDataViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
}
