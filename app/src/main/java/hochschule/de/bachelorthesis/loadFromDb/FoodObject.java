package hochschule.de.bachelorthesis.loadFromDb;

import java.util.List;

import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;

/**
 * This is a helper class to save information for a food object in
 */
public class FoodObject {

    private Food mFood;

    private List<Measurement> mAllMeasurements;
    private List<Measurement> mRefAllMeasurements;

    // Measurements
    private int mAmountMeasurement;
    private int mGlucoseMax;
    private int mGlucoseAvg;
    private int mGlucoseIncreaseMax;
    private int mStandardDeviation;

    private int mGi;

    public FoodObject(Food food, List<Measurement> allMeasurements) {

        mFood = food;

        if (food == null || allMeasurements == null) {
            return;
        }

        mAllMeasurements = allMeasurements;

        if (mAllMeasurements.size() == 0) {
            return;
        }

        mAmountMeasurement = mAllMeasurements.size();
        mGlucoseMax = Measurement.getGlucoseMaxFromList(mAllMeasurements);
        mGlucoseAvg = Math.round(Measurement.getGlucoseAverageFromList(mAllMeasurements));
        mGlucoseIncreaseMax = Measurement.getGlucoseIncreaseMaxFromList(mAllMeasurements);
        mStandardDeviation = Math.round(Measurement.getStandardDeviationFromList(mAllMeasurements));
    }

    public Food getFood() {
        return mFood;
    }

    public int getAmountMeasurement() {
        return mAmountMeasurement;
    }

    public int getGlucoseMax() {
        return mGlucoseMax;
    }

    public int getGlucoseAvg() {
        return mGlucoseAvg;
    }

    public int getGlucoseIncreaseMax() {
        return mGlucoseIncreaseMax;
    }

    public int getStandardDeviation() {
        return mStandardDeviation;
    }

    public int getGi() {
        return mGi;
    }

    public void setRefAllMeasurements(List<Measurement> refAllMeasurements) {
        mRefAllMeasurements = refAllMeasurements;

        if (mRefAllMeasurements.size() == 0) {
            return;
        }

        mGi = Math.round(Measurement.getGIFromList(mRefAllMeasurements, mAllMeasurements));
    }
}