package hochschule.de.bachelorthesis.view.fragments.mainActivity;

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
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeEditBinding;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.view_model.fragments.MeViewModel;

public class MeEditFragment extends Fragment {
    private MeViewModel mViewModel;
    private FragmentMeEditBinding mBinding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable menu
        setHasOptionsMenu(true);

        // View model
        mViewModel = ViewModelProviders.of(this).get(MeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me_edit, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        // Spinner
        mBinding.dropdownSex.setAdapter(getAdapter(getResources().getStringArray(R.array.fragment_me_spinner_sex)));
        mBinding.dropdownFitnessLevel.setAdapter(getAdapter(getResources().getStringArray(R.array.fragment_me_spinner_fitness_level)));

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.me_edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_me) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        if(inPutOkay()) {
            updateViewModel();
            MyToast.createToast(getContext(), "Information saved.");
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
        MyToast.createToast(getContext(), msg);
    }

    private ArrayAdapter<String> getAdapter(String[] elements) {
        return new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.dropdown_menu_popup_item,
                elements);
    }
}
