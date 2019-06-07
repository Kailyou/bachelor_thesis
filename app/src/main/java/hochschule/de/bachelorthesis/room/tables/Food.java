package hochschule.de.bachelorthesis.room.tables;

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
  private float kiloCalories;

  @ColumnInfo(name = "kilo_joules")
  private float kiloJoules;

  @ColumnInfo(name = "fat")
  private float fat;

  @ColumnInfo(name = "saturates")
  private float saturates;

  @ColumnInfo(name = "protein")
  private float protein;

  @ColumnInfo(name = "carbohydrate")
  private float carbohydrate;

  @ColumnInfo(name = "sugars")
  private float sugars;

  @ColumnInfo(name = "salt")
  private float salt;


  /**
   * Constructor
   */
  public Food(String foodName,
      String brandName,
      String foodType,
      float kiloCalories,
      float kiloJoules,
      float fat,
      float saturates,
      float protein,
      float carbohydrate,
      float sugars,
      float salt) {

    this.foodName = foodName;
    this.brandName = brandName;
    this.foodType = foodType;
    this.kiloCalories = kiloCalories;
    this.kiloJoules = kiloJoules;
    this.fat = fat;
    this.saturates = saturates;
    this.protein = protein;
    this.carbohydrate = carbohydrate;
    this.sugars = sugars;
    this.salt = salt;

    isFavorite = false;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Food food = (Food) o;
    return foodName.equals(food.getFoodName()) &&
        brandName.equals(food.getBrandName()) &&
        foodType.equals(food.getFoodType()) &&
        kiloCalories == food.getKiloCalories() &&
        kiloJoules == food.getKiloJoules() &&
        fat == food.getFat() &&
        saturates == food.getSaturates() &&
        protein == food.getProtein() &&
        carbohydrate == food.getCarbohydrate() &&
        sugars == food.getSugars() &&
        salt == food.getSalt();
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

  public float getKiloCalories() {
    return kiloCalories;
  }

  public float getKiloJoules() {
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

  public float getCarbohydrate() {
    return carbohydrate;
  }

  public float getSugars() {
    return sugars;
  }

  public float getSalt() {
    return salt;
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

  public void setKiloCalories(float kiloCalories) {
    this.kiloCalories = kiloCalories;
  }

  public void setKiloJoules(float kiloJoules) {
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

  public void setCarbohydrate(float carbohydrate) {
    this.carbohydrate = carbohydrate;
  }

  public void setSugars(float sugars) {
    this.sugars = sugars;
  }

  public void setSalt(float salt) {
    this.salt = salt;
  }
}
