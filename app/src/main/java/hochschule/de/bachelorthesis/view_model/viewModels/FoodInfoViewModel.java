package hochschule.de.bachelorthesis.view_model.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;

public class FoodInfoViewModel extends AndroidViewModel {

    private Repository mRepository;

    private boolean mIsFavorite;

    // Overview tab
    private MutableLiveData<String> mFoodName;
    private MutableLiveData<String> mBrandName;
    private MutableLiveData<String> mType;
    private MutableLiveData<Float> mKcal;
    private MutableLiveData<Integer> mMeasurementsAmount;
    private MutableLiveData<Integer> mMaxGlucose;
    private MutableLiveData<Integer> mAverageGlucose;
    private MutableLiveData<String> mRating;
    private MutableLiveData<Integer> mPersonalIndex;

    // Data tab
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

        mRepository = new Repository(application);

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
                mKcal.setValue(food.getKiloCalories());
                mMeasurementsAmount.setValue(food.getMeasurementsDone());
                mMaxGlucose.setValue(food.getMaxGlucose());
                mAverageGlucose.setValue(food.getAverageGlucose());
                mRating.setValue(food.getRating());
                mPersonalIndex.setValue(food.getPersonalIndex());

                //food data tab
                mEnergyKcal.setValue(food.getKiloCalories());
                mEnergyKJ.setValue(food.getKiloJoules());
                mFat.setValue(food.getFat());
                mSaturates.setValue(food.getSaturates());
                mProtein.setValue(food.getProtein());
                mCarbohydrates.setValue(food.getCarbohydrates());
                mSugar.setValue(food.getSugar());
                mSalt.setValue(food.getSalt());
            }
        });
    }

    public boolean ismIsFavorite() {
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

    public MutableLiveData<Float> getKcal() {
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
}
