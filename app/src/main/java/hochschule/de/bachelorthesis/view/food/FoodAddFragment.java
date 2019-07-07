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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import hochschule.de.bachelorthesis.utility.Parser;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodAddBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

/**
 * @author Maik T.
 *
 * This class handles the add food feature.
 *
 * The user will be able to add a new food to the list by filling out the views and pressing the
 * save button on the toolbar. Also filled out view data will be restored as long as the user does
 * not quit the APP.
 *
 * The user can do the following actions:
 *
 * Save food: A new food object will be created and added to the list. Clear list: The views will be
 * cleared
 */
public class FoodAddFragment extends Fragment {

  private FragmentFoodAddBinding mBinding;

  private FoodViewModel mViewModel;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable menu
    setHasOptionsMenu(true);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_add, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

    // Spinner
    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(Objects.requireNonNull(getContext()),
            R.array.type, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    mBinding.type.setAdapter(adapter);

    // load the last user input by observing the view model object
    // filter has to be false otherwise auto complete will destroy the dropdown element.
    mViewModel.getFoodAddDataModel().getType()
        .observe(getViewLifecycleOwner(), new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.type
                .setText(s, false);
          }
        });

    return mBinding.getRoot();
  }

  /**
   * Saving the current state to the viewModel.
   *
   * Uses a function to parse a Float value.
   *
   * If the float value is empty, the result will be a -1.
   *
   * The view will convert that -1 to an empty String.
   */
  @Override
  public void onStop() {
    super.onStop();

    // The normal String values can just be taken
    mViewModel.getFoodAddDataModel().getFoodName().setValue(
        Objects.requireNonNull(mBinding.foodName.getText()).toString());

    mViewModel.getFoodAddDataModel().getBrandName()
        .setValue(Objects.requireNonNull(mBinding.brandName.getText()).toString());

    mViewModel.getFoodAddDataModel().getType().setValue(mBinding.type.getText().toString());

    // The exposed drop down
    // get the needed string out of the string array resource and updateFood the vm.
    mViewModel.getFoodAddDataModel().getType().setValue(mBinding.type.getText().toString());

    // The float values have to be parsed first
    mViewModel.getFoodAddDataModel().getKiloCalories().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.kiloCalories.getText()).toString()));

    mViewModel.getFoodAddDataModel().getKiloJoules().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.kiloJoules.getText()).toString()));

    mViewModel.getFoodAddDataModel().getFat().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.fat.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSaturates().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.saturates.getText()).toString()));

    mViewModel.getFoodAddDataModel().getProtein().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.protein.getText()).toString()));

    mViewModel.getFoodAddDataModel().getCarbohydrates().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.carbohydrate.getText()).toString()));

    mViewModel.getFoodAddDataModel().getKiloCalories().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.kiloCalories.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSugars().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.sugars.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSalt().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.salt.getText()).toString()));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.add_food_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {

      case R.id.save:
        if (inPutOkay()) {
          save();
        }
        return true;

      case R.id.clear:
        mViewModel.updateFoodAddModeL(new Food("", "", "",
            -1, -1, -1, -1, -1, -1, -1, -1));
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Checks if the user input has been valid.
   *
   * @return returns true if the input was okay. returns false otherwise.
   */
  private boolean inPutOkay() {
    // checks the text fields
    if (mBinding.foodName.getText() == null || mBinding.foodName.getText().toString().equals("")) {
      toast("Please enter the food's name.");
      return false;
    }

    if (mBinding.brandName.getText() == null || mBinding.brandName.getText().toString()
        .equals("")) {
      toast("Please enter the food's brand name.");
      return false;
    }

    if (mBinding.type.getText() == null || mBinding.type.getText().toString().equals("")) {
      toast("Please enter the food's type.");
      return false;
    }

    // checks the drop down menus
    if (mBinding.kiloCalories.getText() == null || mBinding.kiloCalories.getText().toString()
        .equals("")) {
      toast("Please enter the kilo calories.");
      return false;
    }

    if (mBinding.kiloJoules.getText() == null || mBinding.kiloJoules.getText().toString()
        .equals("")) {
      toast("Please enter the kilo joules.");
      return false;
    }

    if (mBinding.fat.getText() == null || mBinding.fat.getText().toString().equals("")) {
      toast("Please enter the fat.");
      return false;
    }

    if (mBinding.saturates.getText() == null || mBinding.saturates.getText().toString()
        .equals("")) {
      toast("Please enter the saturates.");
      return false;
    }

    if (mBinding.protein.getText() == null || mBinding.protein.getText().toString().equals("")) {
      toast("Please enter the protein.");
      return false;
    }

    if (mBinding.carbohydrate.getText() == null || mBinding.carbohydrate.getText().toString()
        .equals("")) {
      toast("Please enter the carbohydrate.");
      return false;
    }

    if (mBinding.sugars.getText() == null || mBinding.sugars.getText().toString().equals("")) {
      toast("Please enter the sugars.");
      return false;
    }

    if (mBinding.salt.getText() == null || mBinding.salt.getText().toString().equals("")) {
      toast("Please enter the salt.");
      return false;
    }

    return true;
  }

  /**
   * Save the food to the database.
   */
  private void save() {
    String foodName = Objects.requireNonNull(mBinding.foodName.getText()).toString();
    String brandName = Objects.requireNonNull(mBinding.brandName.getText()).toString();
    String type = mBinding.type.getText().toString();
    float kiloCalories = Float
        .parseFloat(Objects.requireNonNull(mBinding.kiloCalories.getText()).toString());
    float kiloJoules = Float
        .parseFloat(Objects.requireNonNull(mBinding.kiloJoules.getText()).toString());
    float fat = Float.parseFloat(Objects.requireNonNull(mBinding.fat.getText()).toString());
    float saturates = Float
        .parseFloat(Objects.requireNonNull(mBinding.saturates.getText()).toString());
    float protein = Float.parseFloat(Objects.requireNonNull(mBinding.protein.getText()).toString());
    float carbohydrate = Float
        .parseFloat(Objects.requireNonNull(mBinding.carbohydrate.getText()).toString());
    float sugars = Float.parseFloat(Objects.requireNonNull(mBinding.sugars.getText()).toString());
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
        carbohydrate,
        sugars,
        salt);

    mViewModel.insertFood(newFood);

    MySnackBar
        .createSnackBar(getContext(), mBinding.foodName.getText().toString() + "added to the list..");
  }

  private void toast(String msg) {
    MySnackBar.createSnackBar(getContext(), msg);
  }
}
