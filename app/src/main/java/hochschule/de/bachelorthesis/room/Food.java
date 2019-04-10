package hochschule.de.bachelorthesis.room;

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

    // Food data
    @ColumnInfo(name = "kcal")
    private int kcal;

    @ColumnInfo(name = "fat")
    private double fat;

    @ColumnInfo(name = "saturates")
    private double saturates;

    @ColumnInfo(name = "protein")
    private double protein;

    @ColumnInfo(name = "carbohydrates")
    private double carbohydrates;

    @ColumnInfo(name = "sugar")
    private double sugar;

    @ColumnInfo(name = "salt")
    private double salt;

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
                int kcal,
                double fat,
                double saturates,
                double protein,
                double carbohydrates,
                double sugar,
                double salt) {

        this.foodName = foodName;
        this.brandName = brandName;
        this.foodType = foodType;
        this.kcal = kcal;
        this.fat = fat;
        this.saturates = saturates;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.sugar = sugar;
        this.salt = salt;

        rating = "unrated";
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getSaturates() {
        return saturates;
    }

    public void setSaturates(double saturates) {
        this.saturates = saturates;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }

    public double getSalt() {
        return salt;
    }

    public void setSalt(double salt) {
        this.salt = salt;
    }

    public int getMeasurementsDone() {
        return measurementsDone;
    }

    public void setMeasurementsDone(int measurementsDone) {
        this.measurementsDone = measurementsDone;
    }

    public int getMaxGlucose() {
        return maxGlucose;
    }

    public void setMaxGlucose(int maxGlucose) {
        this.maxGlucose = maxGlucose;
    }

    public int getAverageGlucose() {
        return averageGlucose;
    }

    public void setAverageGlucose(int averageGlucose) {
        this.averageGlucose = averageGlucose;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getPersonalIndex() {
        return personalIndex;
    }

    public void setPersonalIndex(int personalIndex) {
        this.personalIndex = personalIndex;
    }
}
