package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Measurement;

public class AddMeasurementViewModel extends AndroidViewModel {
    private Repository repository;

    // Time & Advance information
    private MutableLiveData<String> mTimestamp;
    private MutableLiveData<Integer> mAmount;
    private MutableLiveData<String> mTired;
    private MutableLiveData<String> mSex;

    // Measurement values
    private MutableLiveData<Integer> mv_0;
    private MutableLiveData<Integer> mv_15;
    private MutableLiveData<Integer> mv_30;
    private MutableLiveData<Integer> mv_45;
    private MutableLiveData<Integer> mv_60;
    private MutableLiveData<Integer> mv_75;
    private MutableLiveData<Integer> mv_90;
    private MutableLiveData<Integer> mv_105;
    private MutableLiveData<Integer> mv_120;


    public AddMeasurementViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void insert(Measurement measurement) { repository.insert(measurement);}


}
