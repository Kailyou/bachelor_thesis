package hochschule.de.bachelorthesis.room.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import hochschule.de.bachelorthesis.utility.MyMath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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
   * @return All glucose values as an array list.
   */
  private ArrayList<Integer> getAllGlucoseValuesAsList() {
    ArrayList<Integer> res = new ArrayList<>();
    res.add(glucoseStart);

    if (glucose15 != 0) {
      res.add(glucose15);
    }

    if (glucose30 != 0) {
      res.add(glucose30);
    }

    if (glucose45 != 0) {
      res.add(glucose45);
    }

    if (glucose60 != 0) {
      res.add(glucose60);
    }

    if (glucose75 != 0) {
      res.add(glucose75);
    }

    if (glucose90 != 0) {
      res.add(glucose90);
    }

    if (glucose105 != 0) {
      res.add(glucose105);
    }

    if (glucose120 != 0) {
      res.add(glucose120);
    }

    return res;
  }

  // Checks if the measurement is done, as soon as one value is zero, the measurement cannot be finished yet.
  private boolean isDone() {
    return glucose15 != 0 && glucose30 != 0 && glucose45 != 0 && glucose60 != 0 && glucose75 != 0
        && glucose90 != 0 && glucose105 != 0 && glucose120 != 0;
  }

  /**
   * @return Returns the max glucose value if the measurement is done. If the measurement is not
   * done, return 0.
   */
  public int getGlucoseMax() {
    ArrayList<Integer> glucoseValues = getAllGlucoseValuesAsList();

    if (glucoseValues == null) {
      return 0;
    } else {
      return MyMath.calculateMaxFromIntList(glucoseValues);
    }
  }

  /**
   * @return Returns the average glucose value if the measurement is done. If the measurement is not
   * done, return 0.
   */
  public float getGlucoseAverage() {
    return MyMath.calculateMeanFromIntegers(getAllGlucoseValuesAsList());
  }

  /**
   * @return Returns the integral of the measurement. If the measurement is not done, return 0.
   */
  public float getIntegral() {
    return MyMath.calculateMeanFromIntegers(getAllGlucoseValuesAsList());
  }

  public float getStandardDeviation() {
    return MyMath.calculateStandardDeviation(getAllGlucoseValuesAsList());
  }


  /* LISTS */

  /**
   * Removes unfinished measurements of a list.
   *
   * @param measurements The list to check for empty measurements.
   */
  public static void removeNotFinishedMeasurements(List<Measurement> measurements) {
    if (measurements.size() == 0) {
      return;
    }

    Iterator<Measurement> iterator = measurements.iterator();

    while (iterator.hasNext()) {
      if (!iterator.next().isDone()) {
        iterator.remove();
      }
    }
  }

  /**
   * This functions returns the max glucose from a list of measurements.
   *
   * Calculates the max glucose values for all measurements and compares then. Finally, returns the
   * greatest of them.
   *
   * @return Returns max glucose or 0 if the list is empty.
   */
  public static int getGlucoseMaxFromList(List<Measurement> measurements) {
    // Remove unfinished measurements
    removeNotFinishedMeasurements(measurements);

    // Return 0 is list is empty
    if (measurements.size() == 0) {
      return 0;
    }

    // Return the max of the first measurement, if there is only one measurement
    if (measurements.size() == 1) {
      return measurements.get(0).getGlucoseMax();
    }

    // Calculate the max glucose of all measurements and return
    int maxGlucose = measurements.get(0).getGlucoseMax();

    for (int i = 1; i < measurements.size(); ++i) {
      if (measurements.get(i).getGlucoseMax() > maxGlucose) {
        maxGlucose = measurements.get(i).getGlucoseMax();
      }
    }

    return maxGlucose;
  }

  /**
   * This functions returns the average glucose from a list of measurements.
   *
   * First taking all glucose values from all measurements into one list and then calculating the
   * average of those.
   *
   * @return Returns max glucose or 0 if the list is empty.
   */
  public static float getGlucoseAverageFromList(List<Measurement> measurements) {
    // Remove unfinished measurements
    removeNotFinishedMeasurements(measurements);

    // Return 0 is list is empty
    if (measurements.size() == 0) {
      return 0;
    }

    // Add all glucose values from all measurements to one list
    ArrayList<Integer> allGlucoseValues = new ArrayList<>();

    for (Measurement m : measurements) {
      allGlucoseValues.addAll(m.getAllGlucoseValuesAsList());
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
  public static float getAverageIntegralFromList(List<Measurement> measurements) {
    // Remove unfinished measurements
    removeNotFinishedMeasurements(measurements);

    // Return 0 is list is empty
    if (measurements.size() == 0) {
      return 0;
    }

    ArrayList<Float> allIntegrals = new ArrayList<>();

    // Calculate the integral for each measurement
    for (Measurement m : measurements) {
      // Add all glucose values from all measurements to one list
      allIntegrals.add(MyMath.calculateIntegral(m.getAllGlucoseValuesAsList()));
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
    // Remove unfinished measurements
    removeNotFinishedMeasurements(measurements);

    // Return 0 is list is empty
    if (measurements.size() == 0) {
      return 0;
    }

    ArrayList<Float> allStandardDeviations = new ArrayList<>();

    // Calculate the standard deviation for each measurement
    for (Measurement m : measurements) {
      // Add all glucose values from all measurements to one list
      allStandardDeviations.add(MyMath.calculateStandardDeviation(m.getAllGlucoseValuesAsList()));
    }

    return MyMath.calculateMeanFromFloats(allStandardDeviations);
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
