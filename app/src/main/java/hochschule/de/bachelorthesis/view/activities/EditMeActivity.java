package hochschule.de.bachelorthesis.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.ActivityEditMeBinding;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.view_model.fragments.MeViewModel;

public class EditMeActivity extends AppCompatActivity {

    private static final String TAG = "EditMeActivity";

    private ActivityEditMeBinding mBinding;

    private MeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change title
        setTitle("Edit user Data");

        // Data binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_me);

        // View model
        mViewModel = ViewModelProviders.of(this).get(MeViewModel.class);

        // Spinner
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        R.layout.dropdown_menu_popup_item,
                        getResources().getStringArray(R.array.fragment_me_spinner_sex));

        mBinding.dropdownSex.setAdapter(getAdapter(getResources().getStringArray(R.array.fragment_me_spinner_sex)));
        mBinding.dropdownFitnessLevel.setAdapter(getAdapter(getResources().getStringArray(R.array.fragment_me_spinner_fitness_level)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_me) {
                save();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Override this function to specify the options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_me_menu, menu);
        return true;    // displays the menu
    }

    private ArrayAdapter<String> getAdapter(String[] elements) {
        return new ArrayAdapter<>(this,
                R.layout.dropdown_menu_popup_item,
                elements);
    }

    private void save() {
        if(inPutOkay()) {
            updateViewModel();
            MyToast.createToast(this, "Information saved.");
        }
    }

    /**
     * Checks if the user input has been valid.
     * @return
     * returns true if the input was okay.
     * returns false otherwise.
     */
    private boolean inPutOkay() {
        // checks the text fields
        if(mBinding.age.getText() == null || mBinding.age.getText().toString().equals("")) {
            toast("Please enter your age.");
            return false;
        }

        if(mBinding.height.getText() == null || mBinding.height.getText().toString().equals("")) {
            toast("Please enter your height.");
            return false;
        }

        if(mBinding.weight.getText() == null || mBinding.weight.getText().toString().equals("")) {
            toast("Please enter your weight.");
            return false;
        }

        // checks the drop down menus
        if(mBinding.dropdownSex.getText() == null || mBinding.dropdownSex.getText().toString().equals("")) {
            toast("Please select the sex.");
            return false;
        }

        if(mBinding.dropdownFitnessLevel.getText() == null || mBinding.dropdownFitnessLevel.getText().toString().equals("")) {
            toast("Please select the fitness level.");
            return false;
        }

        return true;
    }

    /**
     * Updates the view model.
     */
    private void updateViewModel() {
        mViewModel.setUserAge(Objects.requireNonNull(mBinding.age.getText()).toString());
        mViewModel.setHeight(Objects.requireNonNull(mBinding.height.getText()).toString());
        mViewModel.setWeight(Objects.requireNonNull(mBinding.weight.getText()).toString());
        mViewModel.setSex(mBinding.dropdownSex.getText().toString());
        mViewModel.setFitnessLevel(mBinding.dropdownFitnessLevel.getText().toString());

        // Checkboxes
        if(mBinding.medication.isChecked()) {
            mViewModel.setMedication("Yes");
        }
        else {
            mViewModel.setMedication("No");
        }

        if(mBinding.allergies.isChecked()) {
            mViewModel.setAllergies("Yes");
        }
        else {
            mViewModel.setAllergies("No");
        }

        if(mBinding.smoking.isChecked()) {
            mViewModel.setSmoking("Yes");
        }
        else {
            mViewModel.setSmoking("No");
        }
    }

    private void toast(String msg) {
        MyToast.createToast(this, msg);
    }

    private boolean checkString(String s) {
        return s.equals("");
    }
}
