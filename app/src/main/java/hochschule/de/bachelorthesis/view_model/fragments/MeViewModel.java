package hochschule.de.bachelorthesis.view_model.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MeViewModel extends AndroidViewModel {
    // Personal data
    private MutableLiveData<String> userAge;
    private MutableLiveData<String> height;
    private MutableLiveData<String> weight;
    private MutableLiveData<String> sex;

    // Lifestyle data
    private MutableLiveData<String> fitnessLevel;
    private MutableLiveData<String> medication;
    private MutableLiveData<String> allergies;
    private MutableLiveData<String> smoking;


    public MeViewModel(@NonNull Application application) {
        super(application);
        userAge = new MutableLiveData<>();
        height = new MutableLiveData<>();
        weight = new MutableLiveData<>();
        sex = new MutableLiveData<>();
        fitnessLevel = new MutableLiveData<>();
        medication = new MutableLiveData<>();
        allergies = new MutableLiveData<>();
        smoking = new MutableLiveData<>();
     }

    /*
     * GETTER
      */
    public MutableLiveData<String> getUserAge() {
        return userAge;
    }

    public LiveData<String> getHeight() {
        return height;
    }

    public LiveData<String> getWeight() {
        return weight;
    }

    public LiveData<String> getSex() { return sex; }

    public LiveData<String> getFitnessLevel() {
        return fitnessLevel;
    }

    public LiveData<String> getMedication() {
        return medication;
    }

    public LiveData<String> getAllergies() {
         return allergies;
    }

    public LiveData<String> getSmoking() {
        return smoking;
    }

    /*
     * SETTER
     */

    public void setUserAge(String userAge) {
        this.userAge.setValue(userAge);
    }

    public void setHeight(String height) { this.height.setValue(height); }

    public void setWeight(String weight) {
        this.weight.setValue(weight);
    }

    public void setSex(String sex) { this.sex.setValue(sex); }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel.setValue(fitnessLevel);
    }

    public void setMedication(String medication) {
        this.medication.setValue(medication);
    }

    public void setAllergies(String allergies) {
        this.allergies.setValue(allergies);
    }

    public void setSmoking(String smoking) {
        this.smoking.setValue(smoking);
    }
}
