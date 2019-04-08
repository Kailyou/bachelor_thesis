package hochschule.de.bachelorthesis.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "measurement_table")
public class Measurements {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "start_value")
    private String foodName;

    @ColumnInfo(name = "5")
    private String brandName;

    @ColumnInfo(name = "10")
    private String metaText;

    @ColumnInfo(name = "fk_food")
    private int fkFood;

    public Measurements(String foodName, String brandName, String metaText) {
        this.foodName = foodName;
        this.brandName = brandName;
        this.metaText = metaText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getMetaText() {
        return metaText;
    }
}
