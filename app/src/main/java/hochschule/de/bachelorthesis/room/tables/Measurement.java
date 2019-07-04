package hochschule.de.bachelorthesis.room.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import hochschule.de.bachelorthesis.utility.MyMath;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


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

  public int getFoodId() {
    return foodId;
  }

  public int getUserHistoryId() {
    return userHistoryId;
  }

  public int getId() {
    return id;
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
   * @return All glucose values if the measurement is done. Null else.
   */
  public ArrayList<Integer> getAllGlucoseValuesAsList() {
    if (isDone()) {
      return new ArrayList<Integer>() {{
        add(glucoseStart);
        add(glucose15);
        add(glucose30);
        add(glucose45);
        add(glucose60);
        add(glucose75);
        add(glucose90);
        add(glucose105);
        add(glucose120);
      }};
    }

    return null;
  }

  public boolean isGi() {
    return gi;
  }

  // Checks if the measurement is done, as soon as one value is zero, the measurement cannot be finished yet.
  public boolean isDone() {
    return glucose15 != 0 && glucose30 != 0 && glucose45 != 0 && glucose60 != 0 && glucose75 != 0
        && glucose90 != 0 && glucose105 != 0 && glucose120 != 0;
  }

  /**
   * @return Returns the max glucose value if the measurement is done. If the measurement is not
   * done, return an error code -1.
   */
  public int getGlucoseMax() {
    if (isDone()) {
      return MyMath.getMaxFromArrayList(getAllGlucoseValuesAsList());
    }

    return -1;
  }

  public int getGlucoseAverage() {
    if (isDone()) {
      return MyMath.getAverageFromArrayList(getAllGlucoseValuesAsList());
    }

    return -1;
  }


  /* LISTS */
  public void removeNotFinishedMeasurements(List<Measurement> measurements) {
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

  public int getGlucoseMax(List<Measurement> measurements) {
    // Remove unfinished measurements
    removeNotFinishedMeasurements(measurements);

    if (measurements.size() == 0) {
      return -1;
    }

    if (measurements.size() == 1) {
      return measurements.get(0).getGlucoseMax();
    }

    int maxGlucose = measurements.get(0).getGlucoseMax();

    for (int i = 1; i < measurements.size(); ++i) {
      if (measurements.get(i).getGlucoseMax() > maxGlucose) {
        maxGlucose = measurements.get(i).getGlucoseMax();
      }
    }

    return maxGlucose;
  }

  public int getGlucoseAverageFromList(List<Measurement> measurements) {
    // Remove unfinished measurements
    removeNotFinishedMeasurements(measurements);

    if (measurements.size() == 0) {
      return -1;
    }

    // Add all glucose values from all measurements to one list
    ArrayList<Integer> allGlucoseValues = new ArrayList<>();

    for (Measurement m : measurements) {
      allGlucoseValues.addAll(m.getAllGlucoseValuesAsList());
    }

    return MyMath.getAverageFromArrayList(allGlucoseValues);
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
