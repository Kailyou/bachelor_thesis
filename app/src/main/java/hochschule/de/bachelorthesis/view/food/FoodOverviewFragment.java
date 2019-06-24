package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
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
import hochschule.de.bachelorthesis.utility.Samples;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FoodOverviewFragment extends Fragment {

  private FoodViewModel mViewModel;

  private int mFoodId;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setHasOptionsMenu(true);

    // view model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);
    mViewModel.loadOverviewFragment(Samples.getEmptyFood());

    assert getArguments() != null;
    mFoodId = getArguments().getInt("food_id");
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    // Init data binding
    final FragmentFoodOverviewBinding mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_food_overview, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

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
                // Get row count to admit how many measurements we have
                mViewModel.getMeasurementAmountRows(food.id).observe(getViewLifecycleOwner(),
                    new Observer<Integer>() {
                      @Override
                      public void onChanged(Integer integer) {

                        // Update model
                        mViewModel.loadOverviewFragment(food);

                        // Leave if there are no measurements
                        if (integer == 0) {
                          return;
                        }

                        Log.d("yolo", "measurements all: " + measurements.size());

                        // Remove unfinished measurements
                        measurements.get(0).removeNotFinishedMeasurements(measurements);

                        // Leave if there is no finished measurement
                        if (measurements.size() == 0) {
                          return;
                        }

                        // Max and average glucose
                        // For average, first save ALL measurements into one array
                        ArrayList<Integer> glucoseAll = new ArrayList<>();

                        for (Measurement m : measurements) {
                          glucoseAll.addAll(m.getAllMeasurements());
                        }

                        // Calculate average
                        int glucoseAverage = 0;
                        for (Integer i : glucoseAll) {
                          glucoseAverage += i;
                        }

                        glucoseAverage /= glucoseAll.size();

                        Log.d("yolo", "food id: " + food.id);
                        Log.d("yolo", "food name: " + food.getFoodName());
                        Log.d("yolo", "measurements finished: " + measurements.size());
                        Log.d("yolo", "row amount: " + integer);
                        Log.d("yolo", "max: " + measurements.get(0).getGlucoseMax(measurements));
                        Log.d("yolo", "average: " + glucoseAverage);
                        Log.d("yolo", "~~~~~~~~~~");

                        // Update text views
                        mBinding.amount.setText(String.valueOf(integer));
                        mBinding.glucoseMax.setText(
                            String.valueOf(measurements.get(0).getGlucoseMax(measurements)));
                        mBinding.glucoseAverage.setText(String.valueOf(glucoseAverage));
                      }
                    });
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

      new AlertDialog.Builder(getContext())
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

  private void delete() {
    final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);
    ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(Food food) {
        mViewModel.delete(food);

        // Navigate back to food fragment
        Navigation.findNavController(Objects.requireNonNull(getView()))
            .navigate(R.id.action_foodInfoFragment_to_foodFragment);
      }
    });
  }
}
