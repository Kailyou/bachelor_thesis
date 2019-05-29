package hochschule.de.bachelorthesis.view_model.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

public class AddMeasurementViewModel extends AndroidViewModel {
    private Repository repository;

    private LiveData<UserHistory> mLatestUserHistory;

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
        mLatestUserHistory = repository.getUserHistoryLatest();
    }

    public void insert(Measurement measurement) { repository.insert(measurement);}

    public LiveData<UserHistory> getUserHistoryId() {
        if(mLatestUserHistory == null) {
            Log.d("yolo", "getUserHistoryId: is null alta");
        }
        return mLatestUserHistory;
    }
}
