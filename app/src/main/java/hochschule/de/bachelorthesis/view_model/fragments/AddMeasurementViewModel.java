package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;

public class AddMeasurementViewModel extends AndroidViewModel {
    private Repository repository;

    public AddMeasurementViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void insert(Food food) {
        repository.insert(food);
    }
}
