package hochschule.de.bachelorthesis.room.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import hochschule.de.bachelorthesis.room.tables.Food;

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

    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "user_history_id")
    private int userHistoryId;

    @ColumnInfo(name = "time_stamp")
    private String timeStamp;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "stress")
    private String stress;

    @ColumnInfo(name = "tired")
    private String tired;

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
                       String timeStamp, int amount, String stress, String tired,
                       int glucoseStart, int glucose15, int glucose30, int glucose45, int glucose60,
                       int glucose75, int glucose90, int glucose105, int glucose120) {
        this.foodId = foodId;
        this.userHistoryId = userHistoryId;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.stress = stress;
        this.tired = tired;
        this.glucoseStart = glucoseStart;
        this.glucose15 = glucose15;
        this.glucose30 = glucose30;
        this.glucose45 = glucose45;
        this.glucose60 = glucose60;
        this.glucose75 = glucose75;
        this.glucose90 = glucose90;
        this.glucose105 = glucose105;
        this.glucose120 = glucose120;
    }


    /* GETTER */

    public int getFoodId() {
        return foodId;
    }

    public int getUserHistoryId() {
        return userHistoryId;
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

    /* SETTER */

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public void setUserHistoryId(int userHistoryId) {
        this.userHistoryId = userHistoryId;
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
}
