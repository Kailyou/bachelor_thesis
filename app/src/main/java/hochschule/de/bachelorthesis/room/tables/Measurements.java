package hochschule.de.bachelorthesis.room.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import hochschule.de.bachelorthesis.room.tables.Food;

// A foreign key with cascade delete means that if a record in the parent table is deleted,
// then the corresponding records in the child table will automatically be deleted
@Entity(tableName = "measurement_table",
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

public class Measurements {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "time_stamp")
    private long timeStamp;

    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "user_history_id")
    private int userHistoryId;

    public Measurements(long timeStamp, int foodId, int userHistoryId) {
        this.timeStamp = timeStamp;
        this.foodId = foodId;
        this.userHistoryId = userHistoryId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getUserHistoryId() {
        return userHistoryId;
    }

    public void setUserHistoryId(int userHistoryId) {
        this.userHistoryId = userHistoryId;
    }
}
