package hochschule.de.bachelorthesis.view_model.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import hochschule.de.bachelorthesis.model.Repository;

public class FoodInfoViewModel extends AndroidViewModel {

    private Repository repository;

    private boolean mIsFavorite;

    /* OVERVIEW TAB */

    // General
    private MutableLiveData<String> mFoodName;
    private MutableLiveData<String> mBrandName;
    private MutableLiveData<String> mType;
    private MutableLiveData<String> mKcal;

    // Measurements
    private MutableLiveData<Integer> mMeasurementsAmount;
    private MutableLiveData<Integer> mMaxGlucose;
    private MutableLiveData<Integer> mAverageGlucose;

    // Analyses
    private MutableLiveData<String> mRating;
    private MutableLiveData<Integer> mPersonalIndex;

    /* MEASURING TAB */

    /* FOOD DATA TAB */
    private MutableLiveData<Integer> mEnergyKcal;
    private MutableLiveData<Integer> mEnergyKJ;
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
        mKcal = new MutableLiveData<>();

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

    public MutableLiveData<String> getKcal() {
        return mKcal;
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

    public MutableLiveData<Integer> getEnergyKcal() {
        return mEnergyKcal;
    }

    public MutableLiveData<Integer> getEnergyKJ() {
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

    public void setFoodName(MutableLiveData<String> foodName) {
        mFoodName = foodName;
    }

    public void setBrandName(MutableLiveData<String> brandName) {
        mBrandName = brandName;
    }

    public void setType(MutableLiveData<String> type) {
        mType = type;
    }

    public void setKcal(MutableLiveData<String> kCal) {
        mKcal = kCal;
    }

    public void setMeasurementsAmount(MutableLiveData<Integer> measurementsAmount) {
        mMeasurementsAmount = measurementsAmount;
    }

    public void setMaxGlucose(MutableLiveData<Integer> maxGlucose) {
        mMaxGlucose = maxGlucose;
    }

    public void setAverageGlucose(MutableLiveData<Integer> averageGlucose) {
        mAverageGlucose = averageGlucose;
    }

    public void setRating(MutableLiveData<String> rating) {
        mRating = rating;
    }

    public void setPersonalIndex(MutableLiveData<Integer> personalIndex) {
        mPersonalIndex = personalIndex;
    }

    public void setEnergyKcal(MutableLiveData<Integer> energyKcal) {
        mEnergyKcal = energyKcal;
    }

    public void setEnergyKJ(MutableLiveData<Integer> energyKJ) {
        mEnergyKJ = energyKJ;
    }

    public void setFat(MutableLiveData<Float> fat) {
        mFat = fat;
    }

    public void setSaturates(MutableLiveData<Float> saturates) {
        mSaturates = saturates;
    }

    public void setProtein(MutableLiveData<Float> protein) {
        mProtein = protein;
    }

    public void setCarbohydrates(MutableLiveData<Float> carbohydrates) {
        mCarbohydrates = carbohydrates;
    }

    public void setSugar(MutableLiveData<Float> sugar) {
        mSugar = sugar;
    }

    public void setsSalt(MutableLiveData<Float> salt) {
        mSalt = salt;
    }
}
