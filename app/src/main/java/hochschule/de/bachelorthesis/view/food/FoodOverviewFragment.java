package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.navigation.Navigation;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodOverviewBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.utility.Samples;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.List;
import java.util.Objects;


/**
 * @author thielenm
 *
 * Displays the overview about the loaded food object.
 *
 * This fragment will appear after the user selected a food in the foods fragment.
 *
 * The food object behind the selected food will be loaded from the database and the text views will
 * be updated to show the information.
 *
 * TODO: Rating and Personal index
 */
public class FoodOverviewFragment extends Fragment {

  private FoodViewModel mViewModel;

  private int mFoodId;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setHasOptionsMenu(true);

    // view model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);

    assert getArguments() != null;
    mFoodId = getArguments().getInt("food_id");
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    // Init data binding
    final FragmentFoodOverviewBinding mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_food_overview, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());

    // get passed food id
    assert getArguments() != null;
    int foodId = getArguments().getInt("food_id");

    // Get food from database
    mViewModel.getFoodById(foodId).observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(final Food food) {
        // Get all measurements for that food
        mViewModel.getAllMeasurementsById(food.id).observe(getViewLifecycleOwner(),
            new Observer<List<Measurement>>() {
              @Override
              public void onChanged(final List<Measurement> measurements) {
                // Remove unfinished measurements
                Measurement.removeNotFinishedMeasurements(measurements);

                // Leave if there are no measurements
                if (measurements.size() == 0) {
                  return;
                }

                /* Update text views */

                // General
                mBinding.foodName.setText(food.getFoodName());
                mBinding.brandName.setText(food.getBrandName());
                mBinding.type.setText(food.getFoodType());
                mBinding.kiloCalories.setText(Converter.convertFloat(food.getKiloCalories()));

                // Measurements
                mBinding.amount.setText(String.valueOf(measurements.size()));

                mBinding.glucoseMax
                    .setText(String.valueOf(Measurement.getGlucoseMaxFromList(measurements)));

                mBinding.glucoseAverage.setText(
                    String.valueOf((int) Measurement.getGlucoseAverageFromList(measurements)));

                mBinding.integral.setText(
                    String.valueOf((int) Measurement.getAverageIntegralFromList(measurements)));

                mBinding.stdev.setText(
                    String.valueOf((int) Measurement.getStandardDeviationFromList(measurements)));
              }
            });
      }
    });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.food_overview_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.delete) {

      new AlertDialog.Builder(Objects.requireNonNull(getContext()))
          .setTitle("Delete Confirmation")
          .setMessage(
              "You are about to delete this food.\n\nIt cannot be restored at a later time!\n\nContinue?")
          .setIcon(android.R.drawable.ic_delete)
          .setPositiveButton(android.R.string.yes, new OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
              if (whichButton == -1) {
                delete();
              }
            }
          })
          .setNegativeButton(android.R.string.no, null).show();
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Loads and deletes the selected food from the database and navigate back.
   */
  private void delete() {
    // Load the food object which should be deleted
    final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);
    ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(Food food) {
        ldf.removeObserver(this);
        mViewModel.deleteFood(food);

        // Navigate back to food fragment
        Navigation.findNavController(Objects.requireNonNull(getView()))
            .navigate(R.id.action_foodInfoFragment_to_foodFragment);
      }
    });
  }
}
