package hochschule.de.bachelorthesis.view.fragments.mainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.view_model.FragmentMeViewModel;
import hochschule.de.bachelorthesis.databinding.FragmentMeBinding;
import hochschule.de.bachelorthesis.lifecycle.FragmentMeObserver;
import hochschule.de.bachelorthesis.utility.MyToast;

public class MeFragment extends Fragment {
    private FragmentMeViewModel mFragmentMeViewModel;
    private FragmentMeBinding mBinding;
    private MenuItem edit;
    private MenuItem cancel;
    private MenuItem save;
    private boolean isEditing;
    private static final String SHARED_PREFS = "sharedPrefs";

    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Me");
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getLifecycle().addObserver(new FragmentMeObserver());

        // View model
        mFragmentMeViewModel = ViewModelProviders.of(this).get(FragmentMeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewmodel(mFragmentMeViewModel);

        initSpinner();

        loadData();

        return mBinding.getRoot();
    }


    /**
     * Initializes the spinners.
     */
    private void initSpinner() {
        // Age spinner
        List age = new ArrayList<Integer>();
        for (int i = 5; i <= 100; i++) {
            age.add(Integer.toString(i));
        }

        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mBinding.fragmentMeAgeEdit.setAdapter(spinnerArrayAdapter);

        // height spinner
        List height = new ArrayList<Integer>();
        for (int i = 100; i <= 210; i++) {
            height.add(Integer.toString(i));
        }

        spinnerArrayAdapter = new ArrayAdapter<Integer>(
                getContext(), android.R.layout.simple_spinner_item, height);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mBinding.fragmentMeHeightEdit.setAdapter(spinnerArrayAdapter);

        // weight spinner
        List weight = new ArrayList<Integer>();
        for (int i = 40; i <= 200; i++) {
            weight.add(Integer.toString(i));
        }

        spinnerArrayAdapter = new ArrayAdapter<Integer>(
                getContext(), android.R.layout.simple_spinner_item, weight);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mBinding.fragmentMeWeightEdit.setAdapter(spinnerArrayAdapter);

        // gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.fragment_me_spinner_gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.fragmentMeGenderEdit.setAdapter(adapter);

        // fitness level spinner
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.fragment_me_spinner_fitness_level, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.fragmentMeFitnessLevelEdit.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setHasOptionsMenu(true);
        inflater.inflate(R.menu.me_menu, menu);
        edit = menu.getItem(0);
        cancel = menu.getItem(1);
        save = menu.getItem(2);
    }

    /**
     * Blends one view out while the other will be set visible
     * @param view1 - first view will be set as invisible
     * @param view2 - second view will be set as visible
     */
    private void swapVisibility(View view1, View view2) {
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.VISIBLE);
    }

    /**
     * if the edit mode is enabled, the text views will be hided
     * and the edit ui elements will be shown instead of.
     */
    private void swapTextViewAndEditable() {
        if(!isEditing) {
            isEditing = true;
            swapVisibility(mBinding.fragmentMeAge, mBinding.fragmentMeAgeEdit);
            swapVisibility(mBinding.fragmentMeHeight, mBinding.fragmentMeHeightEdit);
            swapVisibility(mBinding.fragmentMeWeight, mBinding.fragmentMeWeightEdit);
            swapVisibility(mBinding.fragmentMeGender, mBinding.fragmentMeGenderEdit);
            swapVisibility(mBinding.fragmentMeFitnessLevel, mBinding.fragmentMeFitnessLevelEdit);
            swapVisibility(mBinding.fragmentMeMedication, mBinding.fragmentMeMedicationEdit);
            swapVisibility(mBinding.fragmentMeAllergies, mBinding.fragmentMeAllergiesEdit);
            swapVisibility(mBinding.fragmentMeSmoking, mBinding.fragmentMeSmokingEdit);
        }
        else
        {
            isEditing = false;
            swapVisibility(mBinding.fragmentMeAgeEdit, mBinding.fragmentMeAge);
            swapVisibility(mBinding.fragmentMeHeightEdit, mBinding.fragmentMeHeight);
            swapVisibility(mBinding.fragmentMeWeightEdit, mBinding.fragmentMeWeight);
            swapVisibility(mBinding.fragmentMeGenderEdit, mBinding.fragmentMeGender);
            swapVisibility(mBinding.fragmentMeFitnessLevelEdit, mBinding.fragmentMeFitnessLevel);
            swapVisibility(mBinding.fragmentMeMedicationEdit, mBinding.fragmentMeMedication);
            swapVisibility(mBinding.fragmentMeAllergiesEdit, mBinding.fragmentMeAllergies);
            swapVisibility(mBinding.fragmentMeSmokingEdit, mBinding.fragmentMeSmoking);
        }
    }

    /**
     * Helper function which returns the String of a selected item of a radio group.
     * @param radiogroup
     * @return
     */
    private String getSelectedRadioButtonText(RadioGroup radiogroup) {
        // return empty string if no button  has been selected
        if(radiogroup.getCheckedRadioButtonId() == -1)
            return "";

        // return string of selected button
        int radioButtonID = radiogroup.getCheckedRadioButtonId();
        View radioButton = radiogroup.findViewById(radioButtonID);
        int idx = radiogroup.indexOfChild(radioButton);
        RadioButton r = (RadioButton) radiogroup.getChildAt(idx);
        return r.getText().toString();
    }

    private void updateViewModel(String age, String height, String weight, String gender,
                                 String fitnessLevel, String medication, String allergies, String smoking) {
        mFragmentMeViewModel.setUserAge(age);
        mFragmentMeViewModel.setHeight(height);
        mFragmentMeViewModel.setWeight(weight);
        mFragmentMeViewModel.setGender(gender);
        mFragmentMeViewModel.setFitnessLevel(fitnessLevel);
        mFragmentMeViewModel.setTakingMedication(medication);
        mFragmentMeViewModel.setHasAllergies(allergies);
        mFragmentMeViewModel.setIsSmoking(smoking);
    }

    private void saveData() {
        // personal data
        String age = mBinding.fragmentMeAgeEdit.getSelectedItem().toString();
        String height = mBinding.fragmentMeHeightEdit.getSelectedItem().toString();
        String weight = mBinding.fragmentMeWeightEdit.getSelectedItem().toString();
        String gender = mBinding.fragmentMeGenderEdit.getSelectedItem().toString();

        // lifestyle data
        String fitnessLevel = mBinding.fragmentMeFitnessLevelEdit.getSelectedItem().toString();
        String medication = getSelectedRadioButtonText(mBinding.fragmentMeMedicationEdit);
        String allergies = getSelectedRadioButtonText(mBinding.fragmentMeAllergiesEdit);
        String smoking = getSelectedRadioButtonText(mBinding.fragmentMeSmokingEdit);

        // save persistent
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.save_fragment_me_age), age);
        editor.putString(getString(R.string.save_fragment_me_height), height);
        editor.putString(getString(R.string.save_fragment_me_weight), weight);
        editor.putString(getString(R.string.save_fragment_me_gender), gender);
        editor.putString(getString(R.string.save_fragment_me_fitness_level), fitnessLevel);
        editor.putString(getString(R.string.save_fragment_me_medication), medication);
        editor.putString(getString(R.string.save_fragment_me_allergies), allergies);
        editor.putString(getString(R.string.save_fragment_me_smoking), smoking);
        editor.apply();

        updateViewModel(age, height, weight, gender, fitnessLevel, medication, allergies, smoking);
        MyToast.createToast(getContext(), "Data saved");
    }

    private void loadData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String age = sharedPreferences.getString(getString(R.string.save_fragment_me_age),"nope");
        String height = sharedPreferences.getString(getString(R.string.save_fragment_me_height),"nope");
        String weight = sharedPreferences.getString(getString(R.string.save_fragment_me_weight),"nope");
        String gender = sharedPreferences.getString(getString(R.string.save_fragment_me_gender),"nope");
        String fitnessLevel = sharedPreferences.getString(getString(R.string.save_fragment_me_fitness_level),"nope");
        String medication = sharedPreferences.getString(getString(R.string.save_fragment_me_medication),"nope");
        String allergies = sharedPreferences.getString(getString(R.string.save_fragment_me_allergies),"nope");
        String smoking = sharedPreferences.getString(getString(R.string.save_fragment_me_smoking),"nope");

        updateViewModel(age, height, weight, gender, fitnessLevel, medication, allergies, smoking);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_me_edit:
                swapTextViewAndEditable();
                edit.setVisible(false);
                cancel.setVisible(true);
                save.setVisible(true);
                return true;
            case R.id.menu_me_cancel:
                swapTextViewAndEditable();
                cancel.setVisible(false);
                save.setVisible(false);
                edit.setVisible(true);
                return true;
            case R.id.menu_me_save:
                swapTextViewAndEditable();
                cancel.setVisible(false);
                save.setVisible(false);
                edit.setVisible(true);
                saveData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
