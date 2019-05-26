package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;

public class FoodInfoViewModel extends AndroidViewModel {

    private Repository repository;

    private boolean mIsFavorite;

    /* OVERVIEW TAB */

    // General
    private MutableLiveData<String> mFoodName;
    private MutableLiveData<String> mBrandName;
    private MutableLiveData<String> mType;
    //private MutableLiveData<String> mKcal;

    // Measurements
    private MutableLiveData<Integer> mMeasurementsAmount;
    private MutableLiveData<Integer> mMaxGlucose;
    private MutableLiveData<Integer> mAverageGlucose;

    // Analyses
    private MutableLiveData<String> mRating;
    private MutableLiveData<Integer> mPersonalIndex;

    /* MEASURING TAB */

    /* FOOD DATA TAB */
    private MutableLiveData<Float> mEnergyKcal;
    private MutableLiveData<Float> mEnergyKJ;
    private MutableLiveData<Float> mFat;
    private MutableLiveData<Float> mSaturates;
    private MutableLiveData<Float> mProtein;
    private MutableLiveData<Float> mCarbohydrates;
    private MutableLiveData<Float> mSugar;
    private MutableLiveData<Float> mSalt;

    public FoodInfoViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);

        mFoodName = new MutableLiveData<>();
        mBrandName = new MutableLiveData<>();
        mType = new MutableLiveData<>();
        //mKcal = new MutableLiveData<>();

        mMeasurementsAmount = new MutableLiveData<>();
        mMaxGlucose = new MutableLiveData<>();
        mAverageGlucose = new MutableLiveData<>();

        mRating = new MutableLiveData<>();
        mPersonalIndex = new MutableLiveData<>();

        mEnergyKcal = new MutableLiveData<>();
        mEnergyKJ = new MutableLiveData<>();
        mFat = new MutableLiveData<>();
        mSaturates = new MutableLiveData<>();
        mProtein = new MutableLiveData<>();
        mCarbohydrates = new MutableLiveData<>();
        mSugar = new MutableLiveData<>();
        mSalt = new MutableLiveData<>();
    }

    /* TEST */
    public LiveData<Food> getFoodById(int id) {
        return  repository.getFoodById(id);
    }

    /* GETTER */

    public boolean isIsFavorite() {
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

    //public MutableLiveData<String> getKcal() {
       // return mKcal;
  //  }

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

    public MutableLiveData<Float> getEnergyKcal() {
        return mEnergyKcal;
    }

    public MutableLiveData<Float> getEnergyKJ() {
        return mEnergyKJ;
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

    public MutableLiveData<Float> getSugar() {
        return mSugar;
    }

    public MutableLiveData<Float> getSalt() {
        return mSalt;
    }

    /* SETTER */

    public void setIsFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
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

   // public void setKcal(String kCal) {
      //  mKcal.setValue(kCal);
    //}

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

    public void setEnergyKcal(Float energyKcal) {
        mEnergyKcal.setValue(energyKcal);
    }

    public void setEnergyKJ(Float energyKJ) {
        mEnergyKJ.setValue(energyKJ);
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

    public void setSugar(Float sugar) {
        mSugar.setValue(sugar);
    }

    public void setsSalt(Float salt) {
        mSalt.setValue(salt);
    }
}
