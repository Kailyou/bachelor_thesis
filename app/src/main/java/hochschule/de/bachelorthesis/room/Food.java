package hochschule.de.bachelorthesis.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_table")
public class Food {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "food_name")
    private String foodName;

    @ColumnInfo(name = "brand_name")
    private String brandName;

    @ColumnInfo(name = "meta_text")
    private String metaText;

    public Food(String foodName, String brandName, String metaText) {
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
