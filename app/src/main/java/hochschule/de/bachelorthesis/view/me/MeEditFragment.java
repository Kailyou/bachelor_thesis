package hochschule.de.bachelorthesis.view.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.viewmodels.MeViewModel;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeEditBinding;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.MySnackBar;

/**
 * @author Maik Thielen
 * <p>
 * This class handles the User edit feature.
 * <p>
 * The user will be able to update his user data by filling out the views and pressing the save
 * button on the toolbar.
 * <p>
 * After that, there will be a new user history object created, which will be passed to the
 * database.
 * <p>
 * The user is not able to delete old user histories since he does not know about them.
 */
public class MeEditFragment extends Fragment {

    private MeViewModel mViewModel;
    private FragmentMeEditBinding mBinding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable menu
        setHasOptionsMenu(true);

        // View model
        mViewModel =
                ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me_edit, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        // dropdown
        mBinding.sex
                .setAdapter(getAdapter(getResources().getStringArray(R.array.fragment_me_spinner_sex)));

        mBinding.fitnessLevel.setAdapter(
                getAdapter(getResources().getStringArray(R.array.fragment_me_spinner_fitness_level)));

        final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
        ldu.observe(this, new Observer<UserHistory>() {
            @Override
            public void onChanged(UserHistory uh) {
                if (uh == null) {
                    return;
                }

                /* Update text views */

                updateTextViews(uh.getAge(), uh.getHeight(), uh.getWeight(), uh.getSex(),
                        uh.getFitness_level(), uh.getMedication(), uh.getAllergies(), uh.getSmoking(),
                        uh.getDiabetes());
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.me_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (isInputOk()) {
                    save();
                }
                return true;

            // While clearing. Counts will be set to -1, so they
            // can be parsed to an empty String.
            case R.id.clear:
                updateTextViews(-1, -1, -1, "", "", false, false, false, false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the text views.
     *
     * @param age          Age of user
     * @param height       Height of user
     * @param weight       Height of user
     * @param sex          Physical gender of user
     * @param fitnessLevel Fitness level of user
     * @param medication   Medication status of user
     * @param allergies    Allergies status of user
     * @param smoking      Smoking status of user
     * @param diabetes     Diabetes status of user
     */
    private void updateTextViews(int age, int height, int weight,
                                 String sex, String fitnessLevel, boolean medication, boolean allergies, boolean smoking,
                                 boolean diabetes) {

        // Personal data
        mBinding.age.setText(Converter.convertInteger(age));
        mBinding.height.setText(Converter.convertInteger(height));
        mBinding.weight.setText(Converter.convertInteger(weight));
        mBinding.sex.setText(sex, false);

        // Lifestyle
        mBinding.fitnessLevel.setText(fitnessLevel, false);
        mBinding.medication.setChecked(medication);
        mBinding.allergies.setChecked(allergies);
        mBinding.smoking.setChecked(smoking);
        mBinding.diabetes.setChecked(diabetes);
    }

    /**
     * If the user input is okay, save the data by passing the new user history data to the database
     * VM will update itself.
     */
    private void save() {
        int age = Integer.parseInt(Objects.requireNonNull(mBinding.age.getText()).toString());
        int height = Integer.parseInt(Objects.requireNonNull(mBinding.height.getText()).toString());
        int weight = Integer.parseInt(Objects.requireNonNull(mBinding.weight.getText()).toString());
        String sex = mBinding.sex.getText().toString();
        String fitnessLevel = mBinding.fitnessLevel.getText().toString();
        boolean medication = mBinding.medication.isChecked();
        boolean allergies = mBinding.allergies.isChecked();
        boolean smoking = mBinding.smoking.isChecked();
        boolean diabetes = mBinding.diabetes.isChecked();

        final UserHistory uh = new UserHistory(age, height, weight, sex, fitnessLevel, medication,
                allergies, smoking, diabetes);

        final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
        ldu.observe(getViewLifecycleOwner(), new Observer<UserHistory>() {
            @Override
            public void onChanged(UserHistory userHistory) {
                ldu.removeObserver(this);

                // Only save the new one, if something changed
                if (uh.equals(userHistory)) {
                    snackBar("You cannot update user data if nothing changed. Change data and try again.");
                } else {
                    mViewModel.insertUserHistory(uh);
                    snackBar("User data updated successfully!");
                }
            }
        });
    }

    /**
     * Checks if the user input has been valid.
     *
     * @return returns true if the input was okay. returns false otherwise.
     */
    private boolean isInputOk() {
        // checks the text fields
        if (mBinding.age.getText() == null || mBinding.age.getText().toString().equals("")) {
            snackBar("Please enter your age.");
            return false;
        }

        if (mBinding.height.getText() == null || mBinding.height.getText().toString().equals("")) {
            snackBar("Please enter your height.");
            return false;
        }

        if (mBinding.weight.getText() == null || mBinding.weight.getText().toString().equals("")) {
            snackBar("Please enter your weight.");
            return false;
        }

        // checks the drop down menus
        if (mBinding.sex.getText() == null || mBinding.sex.getText().toString()
                .equals("")) {
            snackBar("Please select the sex.");
            return false;
        }

        if (mBinding.fitnessLevel.getText() == null || mBinding.fitnessLevel.getText()
                .toString().equals("")) {
            snackBar("Please select the fitness level.");
            return false;
        }

        return true;
    }

    /**
     * Helper function which will an ArrayAdapter object
     *
     * @param elements - The String array to build the adapter with
     * @return - Returns an ArrayAdapter of the given Strings.
     */
    private ArrayAdapter<String> getAdapter(String[] elements) {
        return new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.dropdown_menu_popup_item, elements);
    }

    /**
     * Helper function for faster SnackBar creation
     *
     * @param msg The message to display in the SnackBar
     */
    private void snackBar(String msg) {
        MySnackBar.createSnackBar(Objects.requireNonNull(getContext()), Objects.requireNonNull(getView()), msg);
    }
}