package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.InverseMethod;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MeViewModel extends AndroidViewModel {
    private static int mId;
    private static MutableLiveData<Integer> mUserHistoryId;

    // Personal data
    private MutableLiveData<Integer> mUserAge;

    private MutableLiveData<Integer> mHeight;
    private MutableLiveData<Integer> mWeight;
    private MutableLiveData<String> mSex;

    // Lifestyle data
    private MutableLiveData<String> mFitnessLevel;
    private MutableLiveData<String> mMedication;
    private MutableLiveData<String> mAllergies;
    private MutableLiveData<String> mSmoking;


    public MeViewModel(@NonNull Application application) {
        super(application);
        mUserHistoryId = new MutableLiveData<>();
        mUserAge = new MutableLiveData<>();
        mHeight = new MutableLiveData<>();
        mWeight = new MutableLiveData<>();
        mSex = new MutableLiveData<>();
        mFitnessLevel = new MutableLiveData<>();
        mMedication = new MutableLiveData<>();
        mAllergies = new MutableLiveData<>();
        mSmoking = new MutableLiveData<>();
    }

    public void update(Integer userAge,
                       Integer height,
                       Integer weight,
                                String sex,
                                String fitnessLevel,
                                String medication,
                                String allergies,
                                String smoking) {
        mUserAge.setValue(userAge);
        mHeight.setValue(height);
        mWeight.setValue(weight);
        mSex.setValue(sex);
        mFitnessLevel.setValue(fitnessLevel);
        mMedication.setValue(medication);
        mAllergies.setValue(allergies);
        mSmoking.setValue(smoking);

        mUserHistoryId.setValue(mId++);
    }

    /*
     * GETTER
     */

    public MutableLiveData<Integer> getUserHistoryId() {
        return mUserHistoryId;
    }

    public MutableLiveData<Integer> getUserAge() {
        return mUserAge;
    }

    public LiveData<Integer> getHeight() {
        return mHeight;
    }

    public LiveData<Integer> getWeight() {
        return mWeight;
    }

    public LiveData<String> getSex() {
        return mSex;
    }

    public LiveData<String> getFitnessLevel() {
        return mFitnessLevel;
    }

    public LiveData<String> getMedication() {
        return mMedication;
    }

    public LiveData<String> getAllergies() {
        return mAllergies;
    }

    public LiveData<String> getSmoking() {
        return mSmoking;
    }
}
