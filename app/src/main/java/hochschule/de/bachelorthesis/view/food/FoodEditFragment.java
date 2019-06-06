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
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.Objects;

public class FoodEditFragment extends Fragment {

  private FoodViewModel mViewModel;

  private int mFoodId;

  private FragmentFoodEditBinding mBinding;

  private boolean hasChanged;


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

    // Spinner
    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(Objects.requireNonNull(getContext()),
            R.array.type, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    mBinding.type.setAdapter(adapter);

    mViewModel.getFoodById(mFoodId).observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(Food food) {
        mViewModel.loadDataFragment(food);
      }
    });

    // load the last user input by observing the view model object
    // filter has to be false otherwise auto complete will destroy the dropdown element.
    mViewModel.getFoodInfoDataModel().getType()
        .observe(getViewLifecycleOwner(), new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.type
                .setText(s, false);
          }
        });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.food_data_menu, menu);

  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.save) {
      if (inPutOkay()) {
        updateFood();
        return true;
      }
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
  private void updateFood() {

    // Get food object from database
    final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);

    ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(Food food) {
        ldf.removeObserver(this);

        // If measurements have been done, user cannot update the food object

        if (food.getAmountMeasurements() > 0) {
          toast(
              "You cannot change food once you entered measurements Already. Delete them and try again.");
          return;
        }

        String newFoodName = Objects.requireNonNull(mBinding.foodName.getText()).toString();
        String newBrandName = Objects.requireNonNull(mBinding.brandName.getText()).toString();
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
                Objects.requireNonNull(mBinding.carbohydrate.getText()).toString());
        float newSugars = Float
            .parseFloat(Objects.requireNonNull(mBinding.sugars.getText()).toString());
        float newSalt = Float
            .parseFloat(Objects.requireNonNull(mBinding.salt.getText()).toString());

        Food newFood = new Food(newFoodName, newBrandName, newFoodType,
            newKiloCalories, newKiloJoules, newFat, newSaturates, newProtein, newCarbohydrates,
            newSugars, newSalt);

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

          mViewModel.update(food);

          MyToast
              .createToast(getContext(), mBinding.foodName.getText().toString() + " updated...");
        }
        else {
          MyToast
              .createToast(getContext(), "You cannot update the same food!");
        }
      }
    });
  }

  private void toast(String msg) {
    MyToast.createToast(getContext(), msg);
  }
}
