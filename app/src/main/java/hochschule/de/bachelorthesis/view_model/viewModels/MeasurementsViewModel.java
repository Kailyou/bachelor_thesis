package hochschule.de.bachelorthesis.view_model.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;

public class MeasurementsViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Measurement>> allMeasurements;

    public MeasurementsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allMeasurements = repository.getAllMeasurements();
    }

    public void insert(Measurement measurement) {
        repository.insert(measurement);
    }

    public LiveData<List<Measurement>> getAllMeasurements() {
        return allMeasurements;
    }

    /*
    public void update(Measurement measurement) {
        repository.update(measurement);
    }

    public void delete(Measurement measurement) {
        repository.delete(measurement);
    }

    public void deleteAllNotes() {
        repository.deleteAllFood();
    }


    */
}
