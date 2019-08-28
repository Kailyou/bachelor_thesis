package hochschule.de.bachelorthesis.loadFromDb;

import java.util.List;

import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;

/**
 * This is a helper class to save information for a measurement in
 */
public class MeasurementObject {

    private Measurement mMeasurement;

    private String mDate;
    private String mTimeStarted;
    private String mTimeEnded;

    // data
    private int mGlucoseMax;
    private int mGlucoseAvg;
    private int mGlucoseIncreaseMax;
    private int mStandardDeviation;

    private int mGi;

    public MeasurementObject(Measurement measurement, List<Measurement> refMeasurements) {
        mMeasurement = measurement;

        if (measurement == null || measurement.isActive()) {
            return;
        }

        // First set time related data
        mDate = Converter.convertTimeStampToDate(mMeasurement.getTimeStamp());
        mTimeStarted = Converter.convertTimeStampToTimeStart(mMeasurement.getTimeStamp());
        mTimeEnded = Converter.convertTimeStampToTimeEnd(measurement.getTimeStamp());

        // Then calculate data which can be shown even if the measurement is not done yet
        mGlucoseMax = mMeasurement.getGlucoseMax();
        mGlucoseAvg = Math.round(mMeasurement.getGlucoseAverage());
        mGlucoseIncreaseMax = mMeasurement.getGlucoseIncreaseMax();
        mStandardDeviation = Math.round(mMeasurement.getStandardDeviation());

        if (refMeasurements == null) {
            return;
        }

        Measurement.removeNonGiMeasurements(refMeasurements);
        Measurement.removeNotFinishedMeasurements(refMeasurements);

        if (refMeasurements.size() == 0) {
            return;
        }

        mGi = Math.round(mMeasurement.getGi(refMeasurements));
    }

    public Measurement getMeasurement() {
        return mMeasurement;
    }

    public String getDate() {
        return mDate;
    }

    public String getTimeStarted() {
        return mTimeStarted;
    }

    public String getTimeEnded() {
        return mTimeEnded;
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
}