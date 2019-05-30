package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

public class AddMeasurementViewModel extends AndroidViewModel {
    private Repository mRepository;

    private LiveData<UserHistory> mLatestUserHistory;

    // General
    private boolean isDone;
    private boolean isGi;

    // Time & Advance information
    private MutableLiveData<String> mTimestamp;
    private MutableLiveData<Integer> mAmount;
    private MutableLiveData<String> mTired;

    // Measurement values
    private MutableLiveData<Integer> mValue0;
    private MutableLiveData<Integer> mValue15;
    private MutableLiveData<Integer> mValue30;
    private MutableLiveData<Integer> mValue45;
    private MutableLiveData<Integer> mValue60;
    private MutableLiveData<Integer> mValue75;
    private MutableLiveData<Integer> mValue90;
    private MutableLiveData<Integer> mValue105;
    private MutableLiveData<Integer> mValue120;

    public AddMeasurementViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
        mLatestUserHistory = mRepository.getUserHistoryLatest();

        mTimestamp = new MutableLiveData<>();
        mAmount = new MutableLiveData<>();
        mTired = new MutableLiveData<>();

        mValue0 = new MutableLiveData<>();
        mValue15 = new MutableLiveData<>();
        mValue30 = new MutableLiveData<>();
        mValue45 = new MutableLiveData<>();
        mValue60 = new MutableLiveData<>();
        mValue75 = new MutableLiveData<>();
        mValue90 = new MutableLiveData<>();
        mValue105 = new MutableLiveData<>();
        mValue120 = new MutableLiveData<>();
    }

    public void insert(Measurement measurement) { mRepository.insert(measurement);}

    public LiveData<UserHistory> getUserHistoryLatest() {
        if(mLatestUserHistory == null) {
            Log.d("yolo", "getUserHistoryId: is null alta");
        }
        return mLatestUserHistory;
    }


    /* GETTER */

    public boolean isDone() {
        return isDone;
    }

    public boolean isGi() {
        return isGi;
    }

    public MutableLiveData<String> getTimestamp() {
        return mTimestamp;
    }

    public MutableLiveData<Integer> getAmount() {
        return mAmount;
    }

    public MutableLiveData<String> getTired() {
        return mTired;
    }

    public MutableLiveData<Integer> getValue0() {
        return mValue0;
    }

    public MutableLiveData<Integer> getValue15() {
        return mValue15;
    }

    public MutableLiveData<Integer> getValue30() {
        return mValue30;
    }

    public MutableLiveData<Integer> getValue45() {
        return mValue45;
    }

    public MutableLiveData<Integer> getValue60() {
        return mValue60;
    }

    public MutableLiveData<Integer> getValue75() {
        return mValue75;
    }

    public MutableLiveData<Integer> getValue90() {
        return mValue90;
    }

    public MutableLiveData<Integer> getValue105() {
        return mValue105;
    }

    public MutableLiveData<Integer> getValue120() {
        return mValue120;
    }
}
