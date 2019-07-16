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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodEditBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.Objects;

/**
 * @author thielenm
 *
 * This class contains the logic for editing a food.
 *
 * Once the class is created, there will be the before by the user chosen food object loaded and
 * with that food object, the texts of the textViews will be set.
 *
 * The user can edit the input and press the save button to eventually save the new food object to
 * the database.
 *
 * The new food object won't be saved, if nothing changed compared to the existing data.
 */
public class FoodEditFragment extends Fragment {

  private FoodViewModel mViewModel;

  private int mFoodId;

  private FragmentFoodEditBinding mBinding;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setHasOptionsMenu(true);

    // view model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_food_edit, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

    // get passed measurement id
    assert getArguments() != null;
    mFoodId = getArguments().getInt("food_id");

    // Dropdown list
    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(Objects.requireNonNull(getContext()),
            R.array.type, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    mBinding.type.setAdapter(adapter);

    // Load food object from database
    mViewModel.getFoodById(mFoodId).observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(Food food) {
        /* Update text views */

        updateTextViews(food.getFoodName(),
            food.getBrandName(), food.getFoodType(),
            food.getKiloCalories(), food.getKiloJoules(), food.getFat(),
            food.getSaturates(), food.getProtein(), food.getCarbohydrate(), food.getSugars(),
            food.getSalt()
        );
      }
    });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.food_edit_menu, menu);

  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save:
        if (isInputOkay()) {
          updateFood();
        }
        return true;

      // While clearing. Counts will be set to -1, so they
      // can be parsed to an empty String.
      case R.id.clear:
        /* Update text views */
        updateTextViews("",
            "", "",
            -1, -1, -1,
            -1, -1, -1, -1, -1
        );
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Updates the text views.
   *
   * @param foodName Name of the food
   * @param brandName Brand name of the food
   * @param type Type of the food
   * @param kiloCalories Kilo calories of the food
   * @param kiloJoules Kilo joules of the food
   * @param fat Fat of the food
   * @param saturates Saturates of the food
   * @param protein Protein of the food
   * @param carbohydrates Carbohydrates of the food
   * @param sugar Sugar of the food
   * @param salt Salt of the food
   */
  private void updateTextViews(String foodName, String brandName, String type,
      float kiloCalories, float kiloJoules, float fat, float saturates, float protein,
      float carbohydrates, float sugar, float salt) {
    /* Update text views */

    // General
    mBinding.foodName.setText(foodName);
    mBinding.brandName.setText(brandName);
    mBinding.type.setText(type, false);

    // Nutritional information
    mBinding.kiloCalories.setText(Converter.convertFloat(kiloCalories));
    mBinding.kiloJoules.setText(Converter.convertFloat(kiloJoules));
    mBinding.fat.setText(Converter.convertFloat(fat));
    mBinding.saturates.setText(Converter.convertFloat(saturates));
    mBinding.protein.setText(Converter.convertFloat(protein));
    mBinding.carbohydrates.setText(Converter.convertFloat(carbohydrates));
    mBinding.sugar.setText(Converter.convertFloat(sugar));
    mBinding.salt.setText(Converter.convertFloat(salt));
  }

  /**
   * Checks if the user input has been valid.
   *
   * @return returns true if the input was okay. returns false otherwise.
   */
  private boolean isInputOkay() {
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

    if (mBinding.carbohydrates.getText() == null || mBinding.carbohydrates.getText().toString()
        .equals("")) {
      toast("Please enter the carbohydrate.");
      return false;
    }

    if (mBinding.sugar.getText() == null || mBinding.sugar.getText().toString().equals("")) {
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
  private void updateFood() {

    // Get food object from database
    final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);

    ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(final Food food) {
        ldf.removeObserver(this);

        // If measurements have been done, user cannot updateFood the food object

        mViewModel.getMeasurementAmountRows(mFoodId).observe(getViewLifecycleOwner(),
            new Observer<Integer>() {
              @Override
              public void onChanged(Integer integer) {

                if (integer > 0) {
                  toast(
                      "You cannot change food once you entered measurements Already. Delete them and try again.");
                  return;
                }

                String newFoodName = Objects.requireNonNull(mBinding.foodName.getText()).toString();
                String newBrandName = Objects.requireNonNull(mBinding.brandName.getText())
                    .toString();
                String newFoodType = Objects.requireNonNull(mBinding.type.getText()).toString();

                float newKiloCalories = Float
                    .parseFloat(Objects.requireNonNull(mBinding.kiloCalories.getText()).toString());
                float newKiloJoules = Float
                    .parseFloat(Objects.requireNonNull(mBinding.kiloJoules.getText()).toString());
                float newFat = Float
                    .parseFloat(Objects.requireNonNull(mBinding.fat.getText()).toString());
                float newSaturates = Float
                    .parseFloat(Objects.requireNonNull(mBinding.saturates.getText()).toString());
                float newProtein = Float
                    .parseFloat(
                        Objects.requireNonNull(mBinding.protein.getText()).toString());
                float newCarbohydrates = Float
                    .parseFloat(
                        Objects.requireNonNull(mBinding.carbohydrates.getText()).toString());
                float newSugars = Float
                    .parseFloat(Objects.requireNonNull(mBinding.sugar.getText()).toString());
                float newSalt = Float
                    .parseFloat(Objects.requireNonNull(mBinding.salt.getText()).toString());

                Food newFood = new Food(newFoodName, newBrandName, newFoodType,
                    newKiloCalories, newKiloJoules, newFat, newSaturates, newProtein,
                    newCarbohydrates,
                    newSugars, newSalt);

                // Only update food, if something changed, so check if the new values are equal
                // with the old ones
                if (!newFood.equals(food)) {
                  food.setFoodName(newFoodName);
                  food.setBrandName(newBrandName);
                  food.setFoodType(newFoodType);
                  food.setKiloCalories(newKiloCalories);
                  food.setKiloJoules(newKiloJoules);
                  food.setFat(newFat);
                  food.setSaturates(newSaturates);
                  food.setCarbohydrate(newCarbohydrates);
                  food.setSugars(newSugars);
                  food.setSalt(newSalt);

                  mViewModel.updateFood(food);

                  MySnackBar
                      .createSnackBar(getContext(),
                          mBinding.foodName.getText().toString() + " updated...");
                } else {
                  MySnackBar
                      .createSnackBar(getContext(), "You cannot updateFood the same food!");
                }
              }
            });
      }
    });
  }

  /**
   * Helper function for faster SnackBar creation
   *
   * @param msg The message to display in the SnackBar
   */
  private void toast(String msg) {
    MySnackBar.createSnackBar(getContext(), msg);
  }
}
