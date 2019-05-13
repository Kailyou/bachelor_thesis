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
}
