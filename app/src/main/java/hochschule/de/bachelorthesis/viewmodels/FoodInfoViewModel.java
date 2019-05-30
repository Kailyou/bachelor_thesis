package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

public class FoodInfoViewModel extends AndroidViewModel {

    // Database
    private Repository mRepository;
    private LiveData<List<Measurement>> mAllMeasurements;

    /**
     * Food
     */
    // General
    private MutableLiveData<Boolean> isFavorite;
    private MutableLiveData<String> mFoodName;
    private MutableLiveData<String> mBrandName;
    private MutableLiveData<String> mType;

    // Food data
    private MutableLiveData<Float> mKiloCalories;
    private MutableLiveData<Float> mKiloJoules;
    private MutableLiveData<Float> mFat;
    private MutableLiveData<Float> mSaturates;
    private MutableLiveData<Float> mProtein;
    private MutableLiveData<Float> mCarbohydrates;
    private MutableLiveData<Float> mSugar;
    private MutableLiveData<Float> mSalt;

    /**
     * MEASUREMENT
     */

    // General
    private MutableLiveData<Integer> mMeasurementsAmount;
    private MutableLiveData<Integer> mMaxGlucose;
    private MutableLiveData<Integer> mAverageGlucose;
    private MutableLiveData<String> mRating;
    private MutableLiveData<Integer> mPersonalIndex;

    /* CURRENT MEASUREMENT */

    // General
    private boolean isDone;
    private boolean isGi;

    // Time & Advance information
    private MutableLiveData<String> mTimestamp;
    private MutableLiveData<Integer> mAmount;
    private MutableLiveData<String> mTired;

    // Measurement values
    private MutableLiveData<Integer> mValue0;
    private MutableLiveData<Integer> mValue15;
    private MutableLiveData<Integer> mValue30;
    private MutableLiveData<Integer> mValue45;
    private MutableLiveData<Integer> mValue60;
    private MutableLiveData<Integer> mValue75;
    private MutableLiveData<Integer> mValue90;
    private MutableLiveData<Integer> mValue105;
    private MutableLiveData<Integer> mValue120;


    public FoodInfoViewModel(@NonNull Application application) {
        super(application);

        mRepository = new Repository(application);
        mAllMeasurements = mRepository.getAllMeasurements();

        isFavorite = new MutableLiveData<>();
        mFoodName = new MutableLiveData<>();
        mBrandName = new MutableLiveData<>();
        mType = new MutableLiveData<>();
        mKiloCalories = new MutableLiveData<>();
        mKiloJoules = new MutableLiveData<>();

        mMeasurementsAmount = new MutableLiveData<>();
        mMaxGlucose = new MutableLiveData<>();
        mAverageGlucose = new MutableLiveData<>();

        mRating = new MutableLiveData<>();
        mPersonalIndex = new MutableLiveData<>();

        mFat = new MutableLiveData<>();
        mSaturates = new MutableLiveData<>();
        mProtein = new MutableLiveData<>();
        mCarbohydrates = new MutableLiveData<>();
        mSugar = new MutableLiveData<>();
        mSalt = new MutableLiveData<>();

        mTimestamp = new MutableLiveData<>();
        mAmount = new MutableLiveData<>();
        mTired = new MutableLiveData<>();
        mValue0 = new MutableLiveData<>();
        mValue15 = new MutableLiveData<>();
        mValue30 = new MutableLiveData<>();
        mValue45 = new MutableLiveData<>();
        mValue60 = new MutableLiveData<>();
        mValue75 = new MutableLiveData<>();
        mValue90 = new MutableLiveData<>();
        mValue105 = new MutableLiveData<>();
        mValue120 = new MutableLiveData<>();
    }

    public void load (int id, LifecycleOwner lco) {
        mRepository.getFoodById(id).observe(lco, new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                if(food == null) {
                    return;
                }

                //overview tab
                mFoodName.setValue(food.getFoodName());
                mBrandName.setValue(food.getBrandName());
                mType.setValue(food.getFoodType());
                mKiloCalories.setValue(food.getKiloCalories());
                mMeasurementsAmount.setValue(food.getMeasurementsDone());
                mMaxGlucose.setValue(food.getMaxGlucose());
                mAverageGlucose.setValue(food.getAverageGlucose());
                mRating.setValue(food.getRating());
                mPersonalIndex.setValue(food.getPersonalIndex());

                //food data tab
                mKiloCalories.setValue(food.getKiloCalories());
                mKiloJoules.setValue(food.getKiloJoules());
                mFat.setValue(food.getFat());
                mSaturates.setValue(food.getSaturates());
                mProtein.setValue(food.getProtein());
                mCarbohydrates.setValue(food.getCarbohydrates());
                mSugar.setValue(food.getSugar());
                mSalt.setValue(food.getSalt());
            }
        });
    }

    /* MEASUREMENTS */
    public void insert(Measurement measurement) { mRepository.insert(measurement);}

    public LiveData<List<Measurement>> getmAllMeasurements() {
        return mAllMeasurements;
    }

    public void addTemplateMeasurement() {

    }

    public void deleteAllMeasurementFromFoodWithId(int foodId) {

    }




    /* GETTER */

    public MutableLiveData<Boolean> getIsFavorite() {
        return isFavorite;
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

    public boolean isDone() {
        return isDone;
    }

    public boolean isGi() {
        return isGi;
    }

    public MutableLiveData<String> getTimestamp() {
        return mTimestamp;
    }

    public MutableLiveData<Integer> getAmount() {
        return mAmount;
    }

    public MutableLiveData<String> getTired() {
        return mTired;
    }

    public MutableLiveData<Integer> getValue0() {
        return mValue0;
    }

    public MutableLiveData<Integer> getValue15() {
        return mValue15;
    }

    public MutableLiveData<Integer> getValue30() {
        return mValue30;
    }

    public MutableLiveData<Integer> getValue45() {
        return mValue45;
    }

    public MutableLiveData<Integer> getValue60() {
        return mValue60;
    }

    public MutableLiveData<Integer> getValue75() {
        return mValue75;
    }

    public MutableLiveData<Integer> getValue90() {
        return mValue90;
    }

    public MutableLiveData<Integer> getValue105() {
        return mValue105;
    }

    public MutableLiveData<Integer> getValue120() {
        return mValue120;
    }
}
