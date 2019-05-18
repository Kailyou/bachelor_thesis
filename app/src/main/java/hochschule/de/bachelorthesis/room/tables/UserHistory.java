package hochschule.de.bachelorthesis.room.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "user_history_table")
public class UserHistory {
    @PrimaryKey(autoGenerate = true)
    public int measurementId;

    /* USER DATA* */
    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "height")
    private int height;

    @ColumnInfo(name = "weight")
    private int weight;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "fitness_level")
    private String fitness_level;

    @ColumnInfo(name = "medication")
    private String medication;

    @ColumnInfo(name = "allergies")
    private String allergies;

    @ColumnInfo(name = "smoking")
    private String smoking;

    public UserHistory(int age, int height, int weight, String gender, String fitness_level, String medication, String allergies, String smoking) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.fitness_level = fitness_level;
        this.medication = medication;
        this.allergies = allergies;
        this.smoking = smoking;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFitness_level() {
        return fitness_level;
    }

    public void setFitness_level(String fitness_level) {
        this.fitness_level = fitness_level;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }
}