package hochschule.de.bachelorthesis.view.food;

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
import hochschule.de.bachelorthesis.databinding.FragmentFoodAddBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

public class AddFoodFragment extends Fragment {
    private static final String TAG = "AddFoodFragment";

    private FragmentFoodAddBinding mBinding;

    private FoodViewModel mViewModel;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable menu
        setHasOptionsMenu(true);

        // View model
        mViewModel = ViewModelProviders.of(getActivity()).get(FoodViewModel.class);

        // Modify action bar
        // Objects.requireNonNull(getActivity().getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_add, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.type.setAdapter(adapter);

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_food_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if(inPutOkay()) {
                save();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save the food to the database.
     */
    private void save() {
        String foodName = Objects.requireNonNull(mBinding.foodName.getText()).toString();
        String brandName = Objects.requireNonNull(mBinding.brandName.getText()).toString();
        String type = mBinding.type.getText().toString();
        float kiloCalories = Float.parseFloat(Objects.requireNonNull(mBinding.energyKcal.getText()).toString());
        float kiloJoules = Float.parseFloat(Objects.requireNonNull(mBinding.energyKj.getText()).toString());
        float fat = Float.parseFloat(Objects.requireNonNull(mBinding.fat.getText()).toString());
        float saturates = Float.parseFloat(Objects.requireNonNull(mBinding.saturates.getText()).toString());
        float protein = Float.parseFloat(Objects.requireNonNull(mBinding.protein.getText()).toString());
        float carbohydrates = Float.parseFloat(Objects.requireNonNull(mBinding.carbohydrates.getText()).toString());
        float sugar = Float.parseFloat(Objects.requireNonNull(mBinding.sugar.getText()).toString());
        float salt = Float.parseFloat(Objects.requireNonNull(mBinding.salt.getText()).toString());

        Food newFood = new Food(
                foodName,
                brandName,
                type,
                kiloCalories,
                kiloJoules,
                fat,
                saturates,
                protein,
                carbohydrates,
                sugar,
                salt);

        mViewModel.insertFood(newFood);

        MyToast.createToast(getContext(), mBinding.foodName.getText().toString() + "added to the list..");
    }

    /**
     * Checks if the user input has been valid.
     * @return
     * returns true if the input was okay.
     * returns false otherwise.
     */
    private boolean inPutOkay() {
        // checks the text fields
        if(mBinding.foodName.getText() == null || mBinding.foodName.getText().toString().equals("")) {
            toast("Please enter the food's name.");
            return false;
        }

        if(mBinding.brandName.getText() == null || mBinding.brandName.getText().toString().equals("")) {
            toast("Please enter the food's brand name.");
            return false;
        }

        if(mBinding.type.getText() == null || mBinding.type.getText().toString().equals("")) {
            toast("Please enter the food's type.");
            return false;
        }

        // checks the drop down menus
        if(mBinding.energyKcal.getText() == null || mBinding.energyKcal.getText().toString().equals("")) {
            toast("Please enter the kilo calories.");
            return false;
        }

        if(mBinding.energyKj.getText() == null || mBinding.energyKj.getText().toString().equals("")) {
            toast("Please enter the kilo joules.");
            return false;
        }

        if(mBinding.fat.getText() == null || mBinding.fat.getText().toString().equals("")) {
            toast("Please enter the fat.");
            return false;
        }

        if(mBinding.saturates.getText() == null || mBinding.saturates.getText().toString().equals("")) {
            toast("Please enter the saturates.");
            return false;
        }

        if(mBinding.protein.getText() == null || mBinding.protein.getText().toString().equals("")) {
            toast("Please enter the protein.");
            return false;
        }

        if(mBinding.carbohydrates.getText() == null || mBinding.carbohydrates.getText().toString().equals("")) {
            toast("Please enter the carbohydrates.");
            return false;
        }

        if(mBinding.sugar.getText() == null || mBinding.sugar.getText().toString().equals("")) {
            toast("Please enter the sugar.");
            return false;
        }

        if(mBinding.salt.getText() == null || mBinding.salt.getText().toString().equals("")) {
            toast("Please enter the salt.");
            return false;
        }

        return true;
    }

    private void toast(String msg) {
        MyToast.createToast(getContext(), msg);
    }
}
