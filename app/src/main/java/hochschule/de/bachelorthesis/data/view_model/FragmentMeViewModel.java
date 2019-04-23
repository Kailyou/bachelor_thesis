package hochschule.de.bachelorthesis.data.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FragmentMeViewModel extends AndroidViewModel {

    // Personal data
    private MutableLiveData<String> userAge;
    private MutableLiveData<String> height;
    private MutableLiveData<String> weight;
    private MutableLiveData<String> gender;

    // Lifestyle data
    private MutableLiveData<String> fitnessLevel;
    private MutableLiveData<String> medication;
    private MutableLiveData<String> allergies;
    private MutableLiveData<String> smoking;


    public FragmentMeViewModel(@NonNull Application application) {
        super(application);

        userAge = new MutableLiveData<>();
        height = new MutableLiveData<>();
        weight = new MutableLiveData<>();
        gender = new MutableLiveData<>();
        fitnessLevel = new MutableLiveData<>();
        medication = new MutableLiveData<>();
        allergies = new MutableLiveData<>();
        smoking = new MutableLiveData<>();
     }

    public MutableLiveData<String> getUserAge() {
        return userAge;
    }

    public LiveData<String> getHeight() {
        return height;
    }

    public LiveData<String> getWeight() {
        return weight;
    }

    public LiveData<String> getGender() {
        return gender;
    }

    public LiveData<String> getFitnessLevel() {
        return fitnessLevel;
    }

    public LiveData<String> getTakingMedication() {
        return medication;
    }

    public LiveData<String> getHasAllergies() {
         return allergies;
    }

    public LiveData<String> getIsSmoking() {
        return smoking;
    }

    public void setUserAge(String userAge) {
        this.userAge.setValue(userAge);
    }

    public void setHeight(String height) {
        this.height.setValue(height);
    }

    public void setWeight(String weight) {
        this.weight.setValue(weight);
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel.setValue(fitnessLevel);
    }

    public void setTakingMedication(String medication) {
        this.medication.setValue(medication);
    }

    public void setHasAllergies(String allergies) {
        this.allergies.setValue(allergies);
    }

    public void setIsSmoking(String smoking) {
        this.smoking.setValue(smoking);
    }
}
