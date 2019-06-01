package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hochschule.de.bachelorthesis.model.CurrentMeasurementModel;
import hochschule.de.bachelorthesis.model.FoodInfoDataModel;
import hochschule.de.bachelorthesis.model.FoodInfoOverviewModel;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

public class FoodInfoViewModel extends AndroidViewModel {
    // Database
    private Repository mRepository;
    private LiveData<List<Measurement>> mAllMeasurements;
    private LiveData<UserHistory> mUserHistoryLatest;
    private LifecycleOwner mLco;

    private FoodInfoOverviewModel mFoodInfoOverviewModel;
    private FoodInfoDataModel mFoodInfoDataModel;
    private CurrentMeasurementModel mCurrentMeasurementModel;


    public FoodInfoViewModel(@NonNull Application application) {
        super(application);

        mRepository = new Repository(application);
        mAllMeasurements = mRepository.getAllMeasurements();
        mUserHistoryLatest = mRepository.getUserHistoryLatest();

        mFoodInfoOverviewModel = new FoodInfoOverviewModel();
        mFoodInfoDataModel = new FoodInfoDataModel();
        mCurrentMeasurementModel = new CurrentMeasurementModel();
    }

    public void load(int id, LifecycleOwner lco) {
        mRepository.getFoodById(id).observe(lco, new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                if (food == null) {
                    return;
                }

                //overview tab
                mFoodInfoOverviewModel.setFoodName(food.getFoodName());
                mFoodInfoOverviewModel.setBrandName(food.getBrandName());
                mFoodInfoOverviewModel.setType(food.getFoodType());
                mFoodInfoOverviewModel.setKiloCalories(food.getKiloCalories());
                mFoodInfoOverviewModel.setMeasurementsAmount(food.getAmountMeasurements());
                mFoodInfoOverviewModel.setMaxGlucose(food.getMaxGlucose());
                mFoodInfoOverviewModel.setAverageGlucose(food.getAverageGlucose());
                mFoodInfoOverviewModel.setRating(food.getRating());
                mFoodInfoOverviewModel.setPersonalIndex(food.getPersonalIndex());

                //food data tab
                mFoodInfoDataModel.setKiloJoules(food.getKiloCalories());
                mFoodInfoDataModel.setKiloJoules(food.getKiloJoules());
                mFoodInfoDataModel.setFat(food.getFat());
                mFoodInfoDataModel.setSaturates(food.getSaturates());
                mFoodInfoDataModel.setProtein(food.getProtein());
                mFoodInfoDataModel.setCarbohydrates(food.getCarbohydrates());
                mFoodInfoDataModel.setSugar(food.getSugar());
                mFoodInfoDataModel.setSalt(food.getSalt());
            }
        });
    }

    public void loadEditMeasurement(int measurementId, int foodId) {

    }

    /* MEASUREMENTS */
    public void insert(Measurement measurement) {
        mRepository.insert(measurement);
    }

    public LiveData<List<Measurement>> getAllMeasurementsById(int id) {
        return mRepository.getAllMeasurementsById(id);
    }

    public LiveData<UserHistory> getUserHistoryLatest() {
        return mUserHistoryLatest;
    }

    /**
     * DEBUG ONLY
     * <p>
     * inserts a test measurement to the table.
     *
     * @param lco - The lifecycle owner, needed for the observe function
     */
    public void addTemplateMeasurement(final LifecycleOwner lco, final int foodId) {
        getUserHistoryLatest().observe(lco, new Observer<UserHistory>() {
            @Override
            public void onChanged(UserHistory userHistory) {
                if (userHistory == null) {
                    return;
                }

                // Build timestamp
                Date date = new Date(); // current date and time

                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());
                String timeStamp = sdf.format(date);

                insert(new Measurement(foodId, userHistory.id, timeStamp, 100, "not stressed", "not tired", 100));
            }
        });
    }

    public void deleteAllMeasurementFromFoodWithId(int foodId) {
        mRepository.deleteAllMeasurementsWithId(foodId);
    }


    /**
     * GETTER
     */

    /* OVERVIEW */
    public MutableLiveData<Boolean> getIsFavorite() {
        return mFoodInfoOverviewModel.isFavorite();
    }

    public MutableLiveData<String> getFoodName() {
        return mFoodInfoOverviewModel.getFoodName();
    }

    public MutableLiveData<String> getBrandName() {
        return mFoodInfoOverviewModel.getBrandName();
    }

    public MutableLiveData<String> getType() {
        return mFoodInfoOverviewModel.getType();
    }

    public MutableLiveData<Integer> getMeasurementsAmount() {
        return mFoodInfoOverviewModel.getMeasurementsAmount();
    }

    public MutableLiveData<Integer> getMaxGlucose() {
        return mFoodInfoOverviewModel.getMaxGlucose();
    }

    public MutableLiveData<Integer> getAverageGlucose() {
        return mFoodInfoOverviewModel.getAverageGlucose();
    }

    public MutableLiveData<String> getRating() {
        return mFoodInfoOverviewModel.getRating();
    }

    public MutableLiveData<Integer> getPersonalIndex() {
        return mFoodInfoOverviewModel.getPersonalIndex();
    }

    /* FOOD DATA */

    public MutableLiveData<Float> getKiloCalories() {
        return mFoodInfoDataModel.getKiloCalories();
    }

    public MutableLiveData<Float> getKiloJoules() {
        return mFoodInfoDataModel.getKiloJoules();
    }


    public MutableLiveData<Float> getFat() {
        return mFoodInfoDataModel.getFat();
    }

    public MutableLiveData<Float> getSaturates() {
        return mFoodInfoDataModel.getSaturates();
    }

    public MutableLiveData<Float> getProtein() {
        return mFoodInfoDataModel.getProtein();
    }

    public MutableLiveData<Float> getCarbohydrates() {
        return mFoodInfoDataModel.getCarbohydrates();
    }

    public MutableLiveData<Float> getSugar() {
        return mFoodInfoDataModel.getSugar();
    }

    public MutableLiveData<Float> getSalt() {
        return mFoodInfoDataModel.getSalt();
    }


    /* Measurements */
    public boolean isDone() {
        return mCurrentMeasurementModel.isDone();
    }

    public MutableLiveData<Boolean> isGi() {
        return mCurrentMeasurementModel.isGi();
    }

    public MutableLiveData<String> getTimestamp() {
        return mCurrentMeasurementModel.getTimestamp();
    }

    public MutableLiveData<Integer> getAmount() {
        return mCurrentMeasurementModel.getAmount();
    }

    public MutableLiveData<String> getTired() {
        return mCurrentMeasurementModel.getTired();
    }

    public MutableLiveData<Integer> getValue0() {
        return mCurrentMeasurementModel.getValue0();
    }

    public MutableLiveData<Integer> getValue15() {
        return mCurrentMeasurementModel.getValue15();
    }

    public MutableLiveData<Integer> getValue30() {
        return mCurrentMeasurementModel.getValue30();
    }

    public MutableLiveData<Integer> getValue45() {
        return mCurrentMeasurementModel.getValue45();
    }

    public MutableLiveData<Integer> getValue60() {
        return mCurrentMeasurementModel.getValue60();
    }

    public MutableLiveData<Integer> getValue75() {
        return mCurrentMeasurementModel.getValue75();
    }

    public MutableLiveData<Integer> getValue90() {
        return mCurrentMeasurementModel.getValue90();
    }

    public MutableLiveData<Integer> getValue105() {
        return mCurrentMeasurementModel.getValue105();
    }

    public MutableLiveData<Integer> getValue120() {
        return mCurrentMeasurementModel.getValue120();
    }
}
