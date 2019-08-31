package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

/**
 * This model class will be used to store data of the add food view.
 * <p>
 * This will affect, once the user leaves the add food fragment to save the input and when the user
 * opens the view again, the data will be restored.
 */
public class FoodAddModel {

    // About the food
    private MutableLiveData<String> mSelectedFood;
    private MutableLiveData<String> mFoodName;
    private MutableLiveData<String> mBrandName;
    private MutableLiveData<String> mType;

    // Nutritional information
    private MutableLiveData<Float> mKiloCalories;
    private MutableLiveData<Float> mKiloJoules;
    private MutableLiveData<Float> mFat;
    private MutableLiveData<Float> mSaturates;
    private MutableLiveData<Float> mProtein;
    private MutableLiveData<Float> mCarbohydrates;
    private MutableLiveData<Float> mSugars;
    private MutableLiveData<Float> mSalt;


    public FoodAddModel() {
        mSelectedFood = new MutableLiveData<>();
        mFoodName = new MutableLiveData<>();
        mBrandName = new MutableLiveData<>();
        mType = new MutableLiveData<>();

        mKiloCalories = new MutableLiveData<>();
        mKiloJoules = new MutableLiveData<>();
        mFat = new MutableLiveData<>();
        mSaturates = new MutableLiveData<>();
        mProtein = new MutableLiveData<>();
        mCarbohydrates = new MutableLiveData<>();
        mSugars = new MutableLiveData<>();
        mSalt = new MutableLiveData<>();
    }

    /* GETTER */

    public MutableLiveData<String> getSelectedFood() {
        return mSelectedFood;
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

    public MutableLiveData<Float> getCarbohydrates() {
        return mCarbohydrates;
    }

    public MutableLiveData<Float> getSugars() {
        return mSugars;
    }

    public MutableLiveData<Float> getSalt() {
        return mSalt;
    }

    /* SETTER */

    public void setSelectedFood(String selectedFood) {
        mSelectedFood.setValue(selectedFood);
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
        mCarbohydrates.setValue(carbohydrates);
    }

    public void setSugars(Float sugars) {
        mSugars.setValue(sugars);
    }

    public void setSalt(Float salt) {
        mSalt.setValue(salt);
    }
}
