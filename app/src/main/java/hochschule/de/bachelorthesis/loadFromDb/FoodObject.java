package hochschule.de.bachelorthesis.loadFromDb;

import java.util.List;

import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;

/**
 * @author Maik Thielen
 * <p>
 * This is a helper class to save information for a food object in.
 */
public class FoodObject {

    private Food mFood;

    private List<Measurement> mAllMeasurements;

    // Measurements
    private Integer mAmountMeasurement = 0;
    private Integer mGlucoseMax = 0;
    private Integer mGlucoseAvg = 0;
    private Integer mGlucoseIncreaseMax = 0;
    private Integer mStandardDeviation = 0;

    private Integer mGi = 0;

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

    public Integer getAmountMeasurement() {
        return mAmountMeasurement;
    }

    public Integer getGlucoseMax() {
        return mGlucoseMax;
    }

    public Integer getGlucoseAvg() {
        return mGlucoseAvg;
    }

    public Integer getGlucoseIncreaseMax() {
        return mGlucoseIncreaseMax;
    }

    public Integer getStandardDeviation() {
        return mStandardDeviation;
    }

    public Integer getGi() {
        return mGi;
    }

    public void setRefAllMeasurements(List<Measurement> refAllMeasurements) {

        if (refAllMeasurements.size() == 0) {
            return;
        }

        mGi = Math.round(Measurement.getGIFromList(refAllMeasurements, mAllMeasurements));
    }
}