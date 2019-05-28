package hochschule.de.bachelorthesis.room.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// A foreign key with cascade delete means that if a record in the parent table is deleted,
// then the corresponding records in the child table will automatically be deleted
@Entity(tableName = "measurement_data_table",
        foreignKeys = @ForeignKey(entity = Measurement.class,
                parentColumns = "id",
                childColumns = "measurement_id",
                onDelete = ForeignKey.CASCADE)
)

public class MeasurementsData {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "measurement_id")
    private long measurementId;

    @ColumnInfo(name = "time")
    private int time;

    @ColumnInfo(name = "value")
    private int value;

    public MeasurementsData(long measurementId, int time, int value) {
        this.measurementId = measurementId;
        this.time = time;
        this.value = value;
    }

    public long getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(long measurementId) {
        this.measurementId = measurementId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
