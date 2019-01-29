package hochschule.de.bachelorthesis.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_table")
public class Food {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "main_text")
    public String mainText;

    @ColumnInfo(name = "sub_text")
    public String subText;

    @ColumnInfo(name = "meta_text")
    public String metaText;

    public Food(String mainText, String subText, String metaText) {
        this.mainText = mainText;
        this.subText = subText;
        this.metaText = metaText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMainText() {
        return mainText;
    }

    public String getSubText() {
        return subText;
    }

    public String getMetaText() {
        return metaText;
    }
}
