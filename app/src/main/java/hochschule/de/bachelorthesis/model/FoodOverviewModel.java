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

  public FoodOverviewModel() {
    mIsFavorite = new MutableLiveData<>();
    mFoodName = new MutableLiveData<>();
    mBrandName = new MutableLiveData<>();
    mType = new MutableLiveData<>();
    mKiloCalories = new MutableLiveData<>();
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
}
