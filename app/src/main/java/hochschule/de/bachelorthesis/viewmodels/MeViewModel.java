package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

public class MeViewModel extends AndroidViewModel {
  private Repository mRepository;
  private LiveData<UserHistory> mUserHistoryLatest;

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
    mUserHistoryLatest = mRepository.getUserHistoryLatest();

    mAge = new MutableLiveData<>();
    mAge.setValue(-1);

    mHeight = new MutableLiveData<>();
    mHeight.setValue(-1);

    mWeight = new MutableLiveData<>();
    mWeight.setValue(-1);

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
   */
  public synchronized void load(UserHistory uh) {
        updateViewModel(uh);
  }

  /**
   * Inserts a new user history to the database and update the viewModel.
   *
   * @param userHistory - The new userHistory object
   */
  public void insertUserHistory(UserHistory userHistory) {
    mRepository.insert(userHistory);
    updateViewModel(userHistory);
  }

  /**
   * Returns the latest user history from database.
   * @return A live data object with the latest UserHistory object
   */
  public LiveData<UserHistory> getUserHistoryLatest() {
    return mUserHistoryLatest;
  }

  private void updateViewModel(UserHistory userHistory) {
    if(userHistory == null)
      return;

    mAge.setValue(userHistory.getAge());
    mHeight.setValue(userHistory.getHeight());
    mWeight.setValue(userHistory.getWeight());
    mSex.setValue(userHistory.getSex());
    mFitnessLevel.setValue(userHistory.getFitness_level());
    mMedication.setValue(userHistory.getMedication());
    mAllergies.setValue(userHistory.getAllergies());
    mSmoking.setValue(userHistory.getSmoking());
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
