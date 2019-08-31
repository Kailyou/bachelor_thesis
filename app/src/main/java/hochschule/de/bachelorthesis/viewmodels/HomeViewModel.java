package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;

/**
 * @author Maik Thielen
 * <p>
 * This ViewModel class contains the data for the home related classes.
 */
public class HomeViewModel extends AndroidViewModel {

    private Repository mRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    /**
     * Updates the measurement object in the table.
     *
     * @param measurement - Measurement object to update
     */
    public void updateMeasurement(Measurement measurement) {
        mRepository.update(measurement);
    }

    /**
     * Gets the food by ID.
     *
     * @param id - The food's id.
     * @return - A live data object with the food.
     */
    public LiveData<Food> getFoodById(int id) {
        return mRepository.getFoodById(id);
    }

    public LiveData<List<Measurement>> getAllMeasurements() {
        return mRepository.getAllMeasurements();
    }

    public LiveData<Measurement> getMeasurementById(int id) {
        return mRepository.getMeasurementById(id);
    }
}
