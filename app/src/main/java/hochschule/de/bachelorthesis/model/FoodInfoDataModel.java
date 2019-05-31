package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

public class FoodInfoDataModel {

    private MutableLiveData<Float> mKiloCalories;
    private MutableLiveData<Float> mKiloJoules;
    private MutableLiveData<Float> mFat;
    private MutableLiveData<Float> mSaturates;
    private MutableLiveData<Float> mProtein;
    private MutableLiveData<Float> mCarbohydrates;
    private MutableLiveData<Float> mSugar;
    private MutableLiveData<Float> mSalt;


    public FoodInfoDataModel() {
        mKiloCalories = new MutableLiveData<>();
        mKiloJoules = new MutableLiveData<>();

        mFat = new MutableLiveData<>();
        mSaturates = new MutableLiveData<>();
        mProtein = new MutableLiveData<>();
        mCarbohydrates = new MutableLiveData<>();
        mSugar = new MutableLiveData<>();
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

    public void setSugar(Float sugar) {
        mSugar.setValue(sugar);
    }

    public void setSalt(Float salt) {
        mSugar.setValue(salt);
    }
}
