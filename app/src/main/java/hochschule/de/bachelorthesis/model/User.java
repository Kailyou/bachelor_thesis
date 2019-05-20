package hochschule.de.bachelorthesis.model;

/**
 * Model user contains the business logic of a user.
 * Those user data will be used to save them with food's glucose values.
 */

//TODO user data needs to be persistent
public class User {

    // Only one database instance will be available for the whole APP.
    private static User INSTANCE;

    // Personal data
    private int age;
    private int height;
    private int weight;
    private String sex;

    // Lifestyle
    private String fitnessLevel;
    private String medication;
    private String allergies;
    private String smoking;


    public User(int age, int height, int weight, String sex, String fitnessLevel, String drugs, String allergies, String smoking) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.fitnessLevel = fitnessLevel;
        this.medication = drugs;
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

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
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
