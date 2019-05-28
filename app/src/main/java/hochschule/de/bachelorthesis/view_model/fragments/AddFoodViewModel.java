package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.Food;

public class AddFoodViewModel extends AndroidViewModel {
    private Repository repository;

    // Personal data
    private String foodName;
    private String brandName;
    private String type;

    // Lifestyle data
    private float kiloCalories;
    private float kiloJoules;
    private float fat;
    private float saturates;
    private float protein;
    private float carbohydrates;
    private float sugar;
    private float salt;

    public AddFoodViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void insert(Food food) {
        repository.insert(food);
    }

    /* GETTER */

    public String getFoodName() {
        return foodName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getType() {
        return type;
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

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public float getSugar() {
        return sugar;
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

    public void setType(String type) {
        this.type = type;
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

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setSugar(float sugar) {
        this.sugar = sugar;
    }

    public void setSalt(float salt) {
        this.salt = salt;
    }
}
