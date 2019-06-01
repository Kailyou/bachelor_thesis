package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

public class FoodInfoDataModel {

  private MutableLiveData<Float> mKiloCalories;
  private MutableLiveData<Float> mKiloJoules;
  private MutableLiveData<Float> mFat;
  private MutableLiveData<Float> mSaturates;
  private MutableLiveData<Float> mProtein;
  private MutableLiveData<Float> mCarbohydrate;
  private MutableLiveData<Float> mSugars;
  private MutableLiveData<Float> mSalt;


  public FoodInfoDataModel() {
    mKiloCalories = new MutableLiveData<>();
    mKiloJoules = new MutableLiveData<>();

    mFat = new MutableLiveData<>();
    mSaturates = new MutableLiveData<>();
    mProtein = new MutableLiveData<>();
    mCarbohydrate = new MutableLiveData<>();
    mSugars = new MutableLiveData<>();
    mSalt = new MutableLiveData<>();
  }

  /* GETTER */

  public MutableLiveData<Float> getKiloCalories() {
    return mKiloCalories;
  }

  public MutableLiveData<Float> getKiloJoules() {
    return mKiloJoules;
  }

  public MutableLiveData<Float> getFat() {
    return mFat;
  }

  public MutableLiveData<Float> getSaturates() {
    return mSaturates;
  }

  public MutableLiveData<Float> getProtein() {
    return mProtein;
  }

  public MutableLiveData<Float> getCarbohydrate() {
    return mCarbohydrate;
  }

  public MutableLiveData<Float> getSugars() {
    return mSugars;
  }

  public MutableLiveData<Float> getSalt() {
    return mSalt;
  }

  /* SETTER */

  public void setKiloCalories(Float kiloCalories) {
    mKiloCalories.setValue(kiloCalories);
  }

  public void setKiloJoules(Float kiloJoules) {
    mKiloJoules.setValue(kiloJoules);
  }

  public void setFat(Float fat) {
    mFat.setValue(fat);
  }

  public void setSaturates(Float saturates) {
    mSaturates.setValue(saturates);
  }

  public void setProtein(Float protein) {
    mProtein.setValue(protein);
  }

  public void setCarbohydrates(Float carbohydrates) {
    mCarbohydrate.setValue(carbohydrates);
  }

  public void setSugar(Float sugar) {
    mSugars.setValue(sugar);
  }

  public void setSalt(Float salt) {
    mSugars.setValue(salt);
  }
}
