package hochschule.de.bachelorthesis.view_model.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.ObservableAndroidViewModel;

public class MeViewModel extends ObservableAndroidViewModel {
    private Repository repository;

    // Personal data
    private MutableLiveData<Integer> mAge;
    private MutableLiveData<Integer> mHeight;
    private MutableLiveData<Integer> mWeight;
    private MutableLiveData<String> mSex;

    // Lifestyle data
    private MutableLiveData<String> mFitnessLevel;
    private MutableLiveData<Boolean> mAllergies;
    private MutableLiveData<Boolean> mSmoking;
    private MutableLiveData<Boolean> mMedication;


    public MeViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        mAge = new MutableLiveData<>();
        mHeight = new MutableLiveData<>();
        mWeight = new MutableLiveData<>();
        mSex = new MutableLiveData<>();
        mFitnessLevel = new MutableLiveData<>();
        mAllergies = new MutableLiveData<>();
        mSmoking = new MutableLiveData<>();
        mMedication = new MutableLiveData<>();
    }

    public void load(LifecycleOwner lco) {
        repository.getUserHistoryLatest().observe(lco, new Observer<UserHistory>() {
            @Override
            public void onChanged(UserHistory userHistory) {
                if (userHistory == null) {
                    return;
                }

                mAge.setValue(userHistory.getAge());
                mHeight.setValue(userHistory.getHeight());
                mWeight.setValue(userHistory.getWeight());
                mSex.setValue(userHistory.getSex());
                mFitnessLevel.setValue(userHistory.getFitness_level());
                mMedication.setValue(userHistory.getMedication());
                mAllergies.setValue(userHistory.getAllergies());
                mSmoking.setValue(userHistory.getSmoking());
            }
        });
    }

    public void insert(Integer age,
                       Integer height,
                       Integer weight,
                       String sex,
                       String fitnessLevel,
                       Boolean medication,
                       Boolean allergies,
                       Boolean smoking) {

        mAge.setValue(age);
        mHeight.setValue(height);
        mWeight.setValue(weight);
        mSex.setValue(sex);
        mFitnessLevel.setValue(fitnessLevel);
        mAllergies.setValue(allergies);
        mSmoking.setValue(smoking);

        UserHistory userHistory = new UserHistory(age, height, weight, sex, fitnessLevel, medication, allergies, smoking);
        repository.insert(userHistory);
    }

    /*
     * GETTER
     */

    public MutableLiveData<Integer> getAge() {
        return mAge;
    }

    public MutableLiveData<Integer> getHeight() {
        return mHeight;
    }

    public MutableLiveData<Integer> getWeight() {
        return mWeight;
    }

    public MutableLiveData<String> getSex() {
        return mSex;
    }

    public MutableLiveData<String> getFitnessLevel() {
        return mFitnessLevel;
    }

    public MutableLiveData<Boolean> getAllergies() {
        return mAllergies;
    }

    public MutableLiveData<Boolean> getSmoking() {
        return mSmoking;
    }

    public MutableLiveData<Boolean> getMedication() {
        return mMedication;
    }
}
