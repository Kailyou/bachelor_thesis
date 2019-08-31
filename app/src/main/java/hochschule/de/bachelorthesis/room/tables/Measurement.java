package hochschule.de.bachelorthesis.room.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hochschule.de.bachelorthesis.utility.MyMath;


// A foreign key with cascade delete means that if a record in the parent table is deleted,
// then the corresponding records in the child table will automatically be deleted
@Entity(tableName = "measurement_table",
        indices = {@Index("food_id"), @Index("user_history_id")},
        foreignKeys = {
                @ForeignKey(entity = Food.class,
                        parentColumns = "id",
                        childColumns = "food_id",
                        onDelete = ForeignKey.CASCADE),

                @ForeignKey(entity = UserHistory.class,
                        parentColumns = "id",
                        childColumns = "user_history_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class Measurement {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // foreign keys
    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "user_history_id")
    private int userHistoryId;

    @ColumnInfo(name = "gi")
    private boolean gi;

    // Time
    @ColumnInfo(name = "time_stamp")
    private String timeStamp;

    // Advance information
    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "stress")
    private String stress;

    @ColumnInfo(name = "tired")
    private String tired;

    // Other events
    @ColumnInfo(name = "physically_activity") // last 48 hours?
    private boolean physicallyActivity;

    @ColumnInfo(name = "alcohol_consumed")
    private boolean alcoholConsumed;

    @ColumnInfo(name = "ill")
    private boolean ill;

    @ColumnInfo(name = "medication")
    private boolean medication;

    @ColumnInfo(name = "period")
    private boolean period;

    // glucose values
    @ColumnInfo(name = "glucose_start")
    private int glucoseStart;

    @ColumnInfo(name = "glucose_15")
    private int glucose15;

    @ColumnInfo(name = "glucose_30")
    private int glucose30;

    @ColumnInfo(name = "glucose_45")
    private int glucose45;

    @ColumnInfo(name = "glucose_60")
    private int glucose60;

    @ColumnInfo(name = "glucose_75")
    private int glucose75;

    @ColumnInfo(name = "glucose_90")
    private int glucose90;

    @ColumnInfo(name = "glucose_105")
    private int glucose105;

    @ColumnInfo(name = "glucose_120")
    private int glucose120;


    public Measurement(int foodId, int userHistoryId,
                       boolean gi,
                       String timeStamp,
                       int amount, String stress, String tired,
                       boolean physicallyActivity,
                       boolean alcoholConsumed,
                       boolean ill,
                       boolean medication,
                       boolean period,
                       int glucoseStart) {
        this.gi = gi;
        this.foodId = foodId;
        this.userHistoryId = userHistoryId;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.stress = stress;
        this.tired = tired;
        this.physicallyActivity = physicallyActivity;
        this.alcoholConsumed = alcoholConsumed;
        this.ill = ill;
        this.medication = medication;
        this.period = period;
        this.glucoseStart = glucoseStart;
    }

    /* GETTER */

    public int getId() {
        return id;
    }

    public int getFoodId() {
        return foodId;
    }

    public int getUserHistoryId() {
        return userHistoryId;
    }

    public boolean isGi() {
        return gi;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getAmount() {
        return amount;
    }

    public String getStress() {
        return stress;
    }

    public String getTired() {
        return tired;
    }

    public boolean isPhysicallyActivity() {
        return physicallyActivity;
    }

    public boolean isAlcoholConsumed() {
        return alcoholConsumed;
    }

    public boolean isIll() {
        return ill;
    }

    public boolean isMedication() {
        return medication;
    }

    public boolean isPeriod() {
        return period;
    }

    public int getGlucoseStart() {
        return glucoseStart;
    }

    public int getGlucose15() {
        return glucose15;
    }

    public int getGlucose30() {
        return glucose30;
    }

    public int getGlucose45() {
        return glucose45;
    }

    public int getGlucose60() {
        return glucose60;
    }

    public int getGlucose75() {
        return glucose75;
    }

    public int getGlucose90() {
        return glucose90;
    }

    public int getGlucose105() {
        return glucose105;
    }

    public int getGlucose120() {
        return glucose120;
    }

    /**
     * @param type 0 = glucose increase (value - start), 1 = glucose value
     * @return All glucose values as an array list.
     */
    private ArrayList<Integer> getAllGlucoseValuesAsList(int type) {
        ArrayList<Integer> res = new ArrayList<>();

        switch (type) {
            case 0:
                res.add(0); // start - start
                res.add(glucose15 - glucoseStart);
                res.add(glucose30 - glucoseStart);
                res.add(glucose45 - glucoseStart);
                res.add(glucose60 - glucoseStart);
                res.add(glucose75 - glucoseStart);
                res.add(glucose90 - glucoseStart);
                res.add(glucose105 - glucoseStart);
                res.add(glucose120 - glucoseStart);
                break;
            case 1:
                res.add(glucoseStart);
                res.add(glucose15);
                res.add(glucose30);
                res.add(glucose45);
                res.add(glucose60);
                res.add(glucose75);
                res.add(glucose90);
                res.add(glucose105);
                res.add(glucose120);
                break;
        }

        return res;
    }

    // Checks if the measurement still is active
    public boolean isActive() {
        return glucose15 == 0 || glucose30 == 0 || glucose45 == 0 || glucose60 == 0 || glucose75 == 0
                || glucose90 == 0 || glucose105 == 0 || glucose120 == 0;
    }

    /**
     * @return Returns the max glucose increase if the measurement is done. If the measurement is not
     * done, return 0.
     */
    public int getGlucoseIncreaseMax() {
        ArrayList<Integer> glucoseValues = getAllGlucoseValuesAsList(1);

        if (glucoseValues == null) {
            return 0;
        } else {
            return MyMath.calculateMaxFromIntList(glucoseValues) - glucoseStart;
        }
    }

    /**
     * This function does not return 0 if measurement is not done to display the current average for
     * unfinished measurements.
     *
     * @return Returns the max glucose value
     */
    public int getGlucoseMax() {
        ArrayList<Integer> glucoseValues = getAllGlucoseValuesAsList(1);

        if (glucoseValues == null) {
            return 0;
        } else {
            return MyMath.calculateMaxFromIntList(glucoseValues);
        }
    }

    /**
     * Get all glucose values in a list and remove the zero glucose values. Then calculate the
     * average (mean) and return it.
     *
     * @return Returns the average glucose value.
     */
    public float getGlucoseAverage() {
        ArrayList<Integer> glucoseValues = getAllGlucoseValuesAsList(1);

        removeZeroEntriesFromList(glucoseValues);

        return MyMath.calculateMeanFromIntegers(glucoseValues);
    }

    /**
     * @return Returns the integral of the measurement. If the measurement is not done, return 0.
     */
    public float getIntegral() {
        return MyMath.calculateIntegral(getAllGlucoseValuesAsList(0));
    }

    /**
     * @return Returns the standard deviation. If the measurement is not done, return 0.
     */
    public float getStandardDeviation() {
        ArrayList<Integer> glucoseValues = getAllGlucoseValuesAsList(1);

        removeZeroEntriesFromList(glucoseValues);

        return MyMath.calculateStandardDeviation(glucoseValues);
    }

    /**
     * @param refMeasurements List of all measurements of the reference (Glucose).
     * @return Returns the GI. If the measurement for either the test product or the reference product
     * is not done, return 0.
     */
    public float getGi(List<Measurement> refMeasurements) {
        return MyMath.calculateGI(getAllGlucoseValuesAsList(0), refMeasurements);
    }


    /* LISTS */

    /**
     * Removes all entries which are lower or equal to zero.
     * This is used to remove the zero entries from the glucose list.
     * Also, the values smaller than zero come from the increase because (0 - glucose Start) is negative,
     * so also remove those.
     *
     * @param list - The list
     */
    private void removeZeroEntriesFromList(ArrayList<Integer> list) {
        if (list.size() == 0) {
            return;
        }

        Iterator<Integer> iterator = list.iterator();

        while (iterator.hasNext()) {
            if (iterator.next() <= 0) {
                iterator.remove();
            }
        }
    }

    /**
     * Removes unfinished measurements of a list.
     *
     * @param measurements The list to check for empty measurements.
     */
    public static void removeNotFinishedMeasurements(List<Measurement> measurements) {
        if (measurements == null || measurements.size() == 0) {
            return;
        }

        Iterator<Measurement> iterator = measurements.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().isActive()) {
                iterator.remove();
            }
        }
    }

    /**
     * Removes non-gi-measurements of a list.
     *
     * @param measurements The list to check for gi measurements.
     */
    public static void removeNonGiMeasurements(List<Measurement> measurements) {
        if (measurements == null || measurements.size() == 0) {
            return;
        }

        Iterator<Measurement> iterator = measurements.iterator();

        while (iterator.hasNext()) {
            if (!iterator.next().isGi()) {
                iterator.remove();
            }
        }
    }

    /**
     * This functions returns the max glucose increase from a list of measurements.
     * <p>
     * Calculates the max glucose increase values for all measurements and compares then. Finally,
     * returns the greatest of them.
     *
     * @return Returns max glucose or 0 if the list is empty.
     */
    public static int getGlucoseIncreaseMaxFromList(List<Measurement> measurements) {
        if (measurements == null) {
            return 0;
        }

        List<Measurement> measurementsCopy = new ArrayList<>(measurements);

        // Remove unfinished measurements
        removeNotFinishedMeasurements(measurementsCopy);

        // Return 0 if list is empty
        if (measurementsCopy.size() == 0) {
            return 0;
        }

        ArrayList<Integer> allMaxValues = new ArrayList<>();
        for (Measurement m : measurementsCopy) {
            allMaxValues.add(MyMath.calculateMaxFromIntList(m.getAllGlucoseValuesAsList(0)));
        }

        return MyMath.calculateMaxFromIntList(allMaxValues);
    }

    /**
     * This functions returns the max glucose from a list of measurements.
     * <p>
     * Calculates the max glucose values for all measurements and compares then. Finally, returns the
     * greatest of them.
     *
     * @return Returns max glucose or 0 if the list is empty.
     */
    public static int getGlucoseMaxFromList(List<Measurement> measurements) {
        if (measurements == null) {
            return 0;
        }

        List<Measurement> measurementsCopy = new ArrayList<>(measurements);

        // Remove unfinished measurements
        removeNotFinishedMeasurements(measurementsCopy);

        // Return 0 if list is empty
        if (measurementsCopy.size() == 0) {
            return 0;
        }

        // Return the max of the first measurement, if there is only one measurement
        if (measurementsCopy.size() == 1) {
            return measurementsCopy.get(0).getGlucoseMax();
        }

        // Calculate the max glucose of all measurements and return
        int maxGlucose = measurementsCopy.get(0).getGlucoseMax();

        for (int i = 1; i < measurementsCopy.size(); ++i) {
            if (measurementsCopy.get(i).getGlucoseMax() > maxGlucose) {
                maxGlucose = measurementsCopy.get(i).getGlucoseMax();
            }
        }

        return maxGlucose;
    }

    /**
     * This functions returns the average glucose from a list of measurements.
     * <p>
     * First taking all glucose values from all measurements into one list and then calculating the
     * average of those.
     *
     * @return Returns max glucose or 0 if the list is empty.
     */
    public static float getGlucoseAverageFromList(List<Measurement> measurements) {

        if (measurements == null) {
            return 0;
        }

        List<Measurement> measurementsCopy = new ArrayList<>(measurements);

        // Remove unfinished measurements
        removeNotFinishedMeasurements(measurementsCopy);

        // Return 0 if list is empty
        if (measurementsCopy.size() == 0) {
            return 0;
        }

        // Add all glucose values from all measurements to one list
        ArrayList<Integer> allGlucoseValues = new ArrayList<>();

        for (Measurement m : measurementsCopy) {
            allGlucoseValues.addAll(m.getAllGlucoseValuesAsList(1));
        }

        return MyMath.calculateMeanFromIntegers(allGlucoseValues);
    }

    /**
     * Since there cannot be a integral calculated with more than one measurement, this function will
     * calculate the integral for each measurement and return the average.
     *
     * @param measurements List of measurements.
     * @return The average integral
     */
    public static float getIntegralFromList(List<Measurement> measurements) {

        if (measurements == null) {
            return 0;
        }

        List<Measurement> measurementsCopy = new ArrayList<>(measurements);

        // Remove unfinished measurements
        removeNotFinishedMeasurements(measurementsCopy);

        // Return 0 if list is empty
        if (measurementsCopy.size() == 0) {
            return 0;
        }

        ArrayList<Float> allIntegrals = new ArrayList<>();

        // Calculate the integral for each measurement
        for (Measurement m : measurementsCopy) {
            // Add all glucose values from all measurements to one list
            allIntegrals.add(m.getIntegral());
        }

        return MyMath.calculateMeanFromFloats(allIntegrals);
    }

    /**
     * Since there cannot be a deviation calculated with more than one measurement, this function will
     * calculate the deviation for each measurement and return the average.
     *
     * @param measurements List of measurements.
     * @return The average deviation
     */
    public static float getStandardDeviationFromList(List<Measurement> measurements) {

        if (measurements == null) {
            return 0;
        }

        List<Measurement> measurementsCopy = new ArrayList<>(measurements);

        // Remove unfinished measurements
        removeNotFinishedMeasurements(measurementsCopy);

        // Return 0 if list is empty
        if (measurementsCopy.size() == 0) {
            return 0;
        }

        ArrayList<Float> allStandardDeviations = new ArrayList<>();

        // Calculate the standard deviation for each measurement
        for (Measurement m : measurementsCopy) {
            // Add all glucose values from all measurements to one list
            allStandardDeviations.add(m.getStandardDeviation());
        }

        return MyMath.calculateMeanFromFloats(allStandardDeviations);
    }

    public static float getGIFromList(List<Measurement> refMeasurements, List<Measurement> measurements) {

        if (refMeasurements == null || measurements == null) {
            return 0;
        }

        List<Measurement> refMeasurementsCopy = new ArrayList<>(refMeasurements);
        List<Measurement> measurementsCopy = new ArrayList<>(measurements);

        // Remove all non-gi-measurements
        removeNonGiMeasurements(refMeasurementsCopy);
        removeNonGiMeasurements(measurementsCopy);

        // Remove unfinished measurements
        removeNotFinishedMeasurements(refMeasurementsCopy);
        removeNotFinishedMeasurements(measurementsCopy);

        // Return 0 if one list is empty
        if (refMeasurementsCopy.size() == 0 || measurementsCopy.size() == 0) {
            return 0;
        }

        ArrayList<Float> allGis = new ArrayList<>();

        // Calculate the GI for each measurement
        for (Measurement m : measurementsCopy) {
            // Add all glucose values from all measurements to one list
            allGis.add(m.getGi(refMeasurementsCopy));
        }

        return MyMath.calculateMeanFromFloats(allGis);
    }

    /* SETTER */

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setStress(String stress) {
        this.stress = stress;
    }

    public void setTired(String tired) {
        this.tired = tired;
    }

    public void setPhysicallyActivity(boolean physicallyActivity) {
        this.physicallyActivity = physicallyActivity;
    }

    public void setAlcoholConsumed(boolean alcoholConsumed) {
        this.alcoholConsumed = alcoholConsumed;
    }

    public void setIll(boolean ill) {
        this.ill = ill;
    }

    public void setMedication(boolean medication) {
        this.medication = medication;
    }

    public void setPeriod(boolean period) {
        this.period = period;
    }

    public void setGlucoseStart(int glucoseStart) {
        this.glucoseStart = glucoseStart;
    }

    public void setGlucose15(int glucose15) {
        this.glucose15 = glucose15;
    }

    public void setGlucose30(int glucose30) {
        this.glucose30 = glucose30;
    }

    public void setGlucose45(int glucose45) {
        this.glucose45 = glucose45;
    }

    public void setGlucose60(int glucose60) {
        this.glucose60 = glucose60;
    }

    public void setGlucose75(int glucose75) {
        this.glucose75 = glucose75;
    }

    public void setGlucose90(int glucose90) {
        this.glucose90 = glucose90;
    }

    public void setGlucose105(int glucose105) {
        this.glucose105 = glucose105;
    }

    public void setGlucose120(int glucose120) {
        this.glucose120 = glucose120;
    }

    public void setGi(boolean gi) {
        this.gi = gi;
    }
}
