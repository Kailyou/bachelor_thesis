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
    private String gender;

    // Lifestyle
    private String fitnessLevel;
    private String drugs;
    private String allergies;
    private String smoking;

    public User() {

    }

    public static synchronized User getINSTANCE() {
        if(INSTANCE == null) {
            INSTANCE = new User();
        }
        return INSTANCE;
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

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }

    public String getDrugs() {
        return drugs;
    }

    public void setDrugs(String drugs) {
        this.drugs = drugs;
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
