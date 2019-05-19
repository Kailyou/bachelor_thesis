package hochschule.de.bachelorthesis.room.tables;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_table")
public class Food {
    // General
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "food_name")
    private String foodName;

    @ColumnInfo(name = "brand_name")
    private String brandName;

    @ColumnInfo(name = "food_type")
    private String foodType;

    @ColumnInfo(name = "is_favorite")
    private Boolean isFavorite;


    // Food data
    @ColumnInfo(name = "kilo_calories")
    private int kiloCalories;

    @ColumnInfo(name = "kilo_joule")
    private int kiloJoules;

    @ColumnInfo(name = "fat")
    private float fat;

    @ColumnInfo(name = "saturates")
    private float saturates;

    @ColumnInfo(name = "protein")
    private float protein;

    @ColumnInfo(name = "carbohydrates")
    private float carbohydrates;

    @ColumnInfo(name = "sugar")
    private float sugar;

    @ColumnInfo(name = "salt")
    private float salt;

    // Measurements
    @ColumnInfo(name = "measurements_done")
    private int measurementsDone;

    @ColumnInfo(name = "max_glucose")
    private int maxGlucose;

    @ColumnInfo(name = "average_glucose")
    private int averageGlucose;

    // Analyses
    @ColumnInfo(name = "rating")
    private String rating;

    @ColumnInfo(name = "personal_index")
    private int personalIndex;

    /**
     * Constructor
     */
    public Food(String foodName,
                String brandName,
                String foodType,
                int kiloCalories,
                int kiloJoules,
                float fat,
                float saturates,
                float protein,
                float carbohydrates,
                float sugar,
                float salt) {

        this.foodName = foodName;
        this.brandName = brandName;
        this.foodType = foodType;
        this.kiloCalories = kiloCalories;
        this.kiloJoules = kiloJoules;
        this.fat = fat;
        this.saturates = saturates;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.sugar = sugar;
        this.salt = salt;

        isFavorite = false;
        rating = "unrated";
    }

    /* GETTER */

    public String getFoodName() {
        return foodName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getFoodType() {
        return foodType;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public int getKiloCalories() {
        return kiloCalories;
    }

    public int getKiloJoules() {
        return kiloJoules;
    }

    public float getFat() {
        return fat;
    }

    public float getSaturates() {
        return saturates;
    }

    public float getProtein() {
        return protein;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public float getSugar() {
        return sugar;
    }

    public float getSalt() {
        return salt;
    }

    public int getMeasurementsDone() {
        return measurementsDone;
    }

    public int getMaxGlucose() {
        return maxGlucose;
    }

    public int getAverageGlucose() {
        return averageGlucose;
    }

    public String getRating() {
        return rating;
    }

    public int getPersonalIndex() {
        return personalIndex;
    }

    /* SETTER */

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public void setKiloCalories(int kiloCalories) {
        this.kiloCalories = kiloCalories;
    }

    public void setKiloJoules(int kiloJoules) {
        this.kiloJoules = kiloJoules;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public void setSaturates(float saturates) {
        this.saturates = saturates;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setSugar(float sugar) {
        this.sugar = sugar;
    }

    public void setSalt(float salt) {
        this.salt = salt;
    }

    public void setMeasurementsDone(int measurementsDone) {
        this.measurementsDone = measurementsDone;
    }

    public void setMaxGlucose(int maxGlucose) {
        this.maxGlucose = maxGlucose;
    }

    public void setAverageGlucose(int averageGlucose) {
        this.averageGlucose = averageGlucose;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setPersonalIndex(int personalIndex) {
        this.personalIndex = personalIndex;
    }
}
