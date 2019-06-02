package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.ObservableAndroidViewModel;

public class MeViewModel extends ObservableAndroidViewModel {

  private Repository mRepository;

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
    mRepository = new Repository(application);
    mAge = new MutableLiveData<>();
    mHeight = new MutableLiveData<>();
    mWeight = new MutableLiveData<>();
    mSex = new MutableLiveData<>();
    mFitnessLevel = new MutableLiveData<>();
    mAllergies = new MutableLiveData<>();
    mSmoking = new MutableLiveData<>();
    mMedication = new MutableLiveData<>();
  }

  /**
   * Loads the last current user data.
   *
   * With the latest userHistory reference, the most recent user history data will be loaded and the
   * viewModel will be updated with those data.
   *
   * This will affect the me view to update itself.
   *
   * @param lco - life cycle owner
   */
  public void load(LifecycleOwner lco) {
    mRepository.getUserHistoryLatest().observe(lco, new Observer<UserHistory>() {
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

  /**
   * Inserts a new user history to the database and update the viewModel.
   *
   * @param userHistory - omegalul
   */
  public void insertUserHistory(UserHistory userHistory) {
    mRepository.insert(userHistory);

    updateViewModel(userHistory.getAge(), userHistory.getHeight(), userHistory.getWeight(),
        userHistory.getSex(), userHistory.getFitness_level(),
        userHistory.getMedication(), userHistory.getAllergies(), userHistory.getSmoking());
  }

  /**
   * DEBUG ONLY
   *
   * Add a template user to the database and update the viewModel with those data.
   */
  public void addTemplateUser() {
    UserHistory userHistory = new UserHistory(29, 173, 88, "male", "low", false, false, false);
    insertUserHistory(userHistory);
  }

  /**
   * DEBUG ONLY
   *
   * Removes all existing user history objects from the database and updates the viewModel.
   * -1 will be converted to an empty String.
   *
   */
  public void deleteAllUserHistoryIds() {
    mRepository.deleteAllUserHistories();
    updateViewModel(-1, -1, -1, "", "", false, false, false);
  }

  private void updateViewModel(int age, int height, int weight, String sex,
      String fitnessLevel, boolean medication,
      boolean allergies, boolean smoking) {
    mAge.setValue(age);
    mHeight.setValue(height);
    mWeight.setValue(weight);
    mSex.setValue(sex);
    mFitnessLevel.setValue(fitnessLevel);
    mMedication.setValue(medication);
    mAllergies.setValue(allergies);
    mSmoking.setValue(smoking);
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
