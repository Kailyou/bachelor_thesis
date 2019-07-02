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

  @ColumnInfo(name = "diabetes")
  private Boolean diabetes;

  public UserHistory(int age, int height, int weight,
      String sex, String fitness_level,
      Boolean medication, Boolean allergies, Boolean smoking, Boolean diabetes) {
    this.age = age;
    this.height = height;
    this.weight = weight;
    this.sex = sex;
    this.fitness_level = fitness_level;
    this.medication = medication;
    this.allergies = allergies;
    this.smoking = smoking;
    this.diabetes = diabetes;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserHistory userHistory = (UserHistory) o;
    return age == userHistory.getAge() &&
        height == userHistory.getHeight() &&
        weight == userHistory.getWeight() &&
        sex.equals(userHistory.getSex()) &&
        fitness_level.equals(userHistory.getFitness_level()) &&
        medication == userHistory.getMedication() &&
        allergies == userHistory.getAllergies() &&
        smoking == userHistory.getSmoking() &&
        diabetes == userHistory.getDiabetes();
  }

  public int getAge() {
    return age;
  }

  public int getHeight() {
    return height;
  }

  public int getWeight() {
    return weight;
  }

  public String getSex() {
    return sex;
  }

  public String getFitness_level() {
    return fitness_level;
  }

  public Boolean getMedication() {
    return medication;
  }

  public Boolean getAllergies() {
    return allergies;
  }

  public Boolean getSmoking() {
    return smoking;
  }

  public Boolean getDiabetes() {
    return diabetes;
  }
}