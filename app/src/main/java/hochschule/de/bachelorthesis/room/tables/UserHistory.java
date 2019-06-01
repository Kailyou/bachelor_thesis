package hochschule.de.bachelorthesis.room.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_history_table")
public class UserHistory {

  @PrimaryKey(autoGenerate = true)
  public int id;

  /* USER DATA* */
  @ColumnInfo(name = "age")
  private int age;

  @ColumnInfo(name = "height")
  private int height;

  @ColumnInfo(name = "weight")
  private int weight;

  @ColumnInfo(name = "sex")
  private String sex;

  @ColumnInfo(name = "fitness_level")
  private String fitness_level;

  @ColumnInfo(name = "medication")
  private Boolean medication;

  @ColumnInfo(name = "allergies")
  private Boolean allergies;

  @ColumnInfo(name = "smoking")
  private Boolean smoking;

  public UserHistory(int age, int height, int weight,
      String sex, String fitness_level,
      Boolean medication, Boolean allergies, Boolean smoking) {
    this.age = age;
    this.height = height;
    this.weight = weight;
    this.sex = sex;
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

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getFitness_level() {
    return fitness_level;
  }

  public void setFitness_level(String fitness_level) {
    this.fitness_level = fitness_level;
  }

  public Boolean getMedication() {
    return medication;
  }

  public void setMedication(Boolean medication) {
    this.medication = medication;
  }

  public Boolean getAllergies() {
    return allergies;
  }

  public void setAllergies(Boolean allergies) {
    this.allergies = allergies;
  }

  public Boolean getSmoking() {
    return smoking;
  }

  public void setSmoking(Boolean smoking) {
    this.smoking = smoking;
  }
}