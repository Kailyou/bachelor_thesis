package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

public class FoodOverviewModel {

  // Menu
  private MutableLiveData<Boolean> mIsFavorite;

  // General
  private MutableLiveData<String> mFoodName;
  private MutableLiveData<String> mBrandName;
  private MutableLiveData<String> mType;
  private MutableLiveData<Float> mKiloCalories;

  // Measurements
  private MutableLiveData<Integer> mMeasurementsAmount;
  private MutableLiveData<Integer> mMaxGlucose;
  private MutableLiveData<Integer> mAverageGlucose;

  // Analyses
  private MutableLiveData<String> mRating;
  private MutableLiveData<Integer> mPersonalIndex;

  public FoodOverviewModel() {
    mIsFavorite = new MutableLiveData<>();
    mFoodName = new MutableLiveData<>();
    mBrandName = new MutableLiveData<>();
    mType = new MutableLiveData<>();
    mKiloCalories = new MutableLiveData<>();
    mMeasurementsAmount = new MutableLiveData<>();
    mMaxGlucose = new MutableLiveData<>();
    mAverageGlucose = new MutableLiveData<>();
    mRating = new MutableLiveData<>();
    mPersonalIndex = new MutableLiveData<>();
  }

  /* GETTER */

  public MutableLiveData<Boolean> isFavorite() {
    return mIsFavorite;
  }

  public MutableLiveData<String> getFoodName() {
    return mFoodName;
  }

  public MutableLiveData<String> getBrandName() {
    return mBrandName;
  }

  public MutableLiveData<String> getType() {
    return mType;
  }

  public MutableLiveData<Float> getKiloCalories() {
    return mKiloCalories;
  }

  public MutableLiveData<Integer> getMeasurementsAmount() {
    return mMeasurementsAmount;
  }

  public MutableLiveData<Integer> getMaxGlucose() {
    return mMaxGlucose;
  }

  public MutableLiveData<Integer> getAverageGlucose() {
    return mAverageGlucose;
  }

  public MutableLiveData<String> getRating() {
    return mRating;
  }

  public MutableLiveData<Integer> getPersonalIndex() {
    return mPersonalIndex;
  }

  /* SETTER */

  public void setFavorite(boolean isFavorite) {
    mIsFavorite.setValue(isFavorite);
  }

  public void setFoodName(String foodName) {
    mFoodName.setValue(foodName);
  }

  public void setBrandName(String brandName) {
    mBrandName.setValue(brandName);
  }

  public void setType(String type) {
    mType.setValue(type);
  }

  public void setKiloCalories(Float kiloCalories) {
    mKiloCalories.setValue(kiloCalories);
  }

  public void setMeasurementsAmount(Integer measurementsAmount) {
    mMeasurementsAmount.setValue(measurementsAmount);
  }

  public void setMaxGlucose(Integer maxGlucose) {
    mMaxGlucose.setValue(maxGlucose);
  }

  public void setAverageGlucose(Integer averageGlucose) {
    mAverageGlucose.setValue(averageGlucose);
  }

  public void setRating(String rating) {
    mRating.setValue(rating);
  }

  public void setPersonalIndex(Integer personalIndex) {
    mPersonalIndex.setValue(personalIndex);
  }
}
