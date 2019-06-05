package hochschule.de.bachelorthesis.view.food;

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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.MyMath;
import hochschule.de.bachelorthesis.utility.Samples;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementsBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.AdapterMeasurements;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

public class MeasurementsFragment extends Fragment {

  private FoodViewModel mViewModel;

  private int mFoodId;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);

    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    FragmentMeasurementsBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_measurements, container, false);
    binding.setLifecycleOwner(getViewLifecycleOwner());
    binding.setViewModel(mViewModel);

    // RecyclerView
    RecyclerView recyclerView = binding.recyclerView;
    //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
    recyclerView.setHasFixedSize(true);

    // Adapter
    NavController navController = Navigation
        .findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment_host);
    final AdapterMeasurements adapter = new AdapterMeasurements(getContext(), navController,
        mFoodId);
    recyclerView.setAdapter(adapter);

    // get passed food id
    assert getArguments() != null;
    mFoodId = getArguments().getInt("food_id");

    // loading the measurement entries
    mViewModel.getAllMeasurementsById(mFoodId).observe(this, new Observer<List<Measurement>>() {
      @Override
      public void onChanged(List<Measurement> measurements) {
        adapter.setMeasurements(measurements);
      }
    });

    // fab
    Bundle bundle = new Bundle();
    bundle.putInt("food_id", mFoodId);
    binding.add.setOnClickListener(Navigation
        .createNavigateOnClickListener(R.id.action_foodInfoFragment_to_addMeasurement, bundle));

    return binding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.measurements_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add_tmp_measurement:
        createTemplateMeasurement(mFoodId);
        return true;
      case R.id.delete_measurements:
        mViewModel.deleteAllMeasurementFromFoodWithId(mFoodId);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void createTemplateMeasurement(final int foodId) {
    final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
    ldu.observe(getViewLifecycleOwner(), new Observer<UserHistory>() {
      @Override
      public void onChanged(final UserHistory userHistory) {
        ldu.removeObserver(this);

        if (userHistory == null) {
          return;
        }

        // Get food object
        final LiveData<Food> ldf = mViewModel.getFoodById(foodId);
        ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
          @Override
          public void onChanged(final Food food) {
            ldf.removeObserver(this);

            // Get all measurements
            final LiveData<List<Measurement>> ldlm = mViewModel.getAllMeasurementsById(foodId);
            ldlm.observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
              @Override
              public void onChanged(List<Measurement> measurements) {
                ldlm.removeObserver(this);
                test(userHistory, food, measurements);
              }
            });
          }
        });
      }
    });
  }

  private void test(UserHistory userHistory, Food food, List<Measurement> measurements) {
    final Measurement newMeasurement = Samples.getRandomMeasurement(
        Objects.requireNonNull(getContext()), mFoodId, userHistory.id);

    /* Update Measurement */

    // Max glucose
    Integer[] glucoseValuesArray = {
        newMeasurement.getGlucoseStart(),
        newMeasurement.getGlucose15(),
        newMeasurement.getGlucose30(),
        newMeasurement.getGlucose45(),
        newMeasurement.getGlucose60(),
        newMeasurement.getGlucose75(),
        newMeasurement.getGlucose90(),
        newMeasurement.getGlucose105(),
        newMeasurement.getGlucose120()
    };

    ArrayList<Integer> glucoseValuesList = new ArrayList<>(Arrays.asList(glucoseValuesArray));

    newMeasurement.setGlucoseMax(MyMath.getMaxFromArrayList(glucoseValuesList));
    newMeasurement.setGlucoseAvg(MyMath.getAverageFromArrayList(glucoseValuesList));

    // Increment measurement amount
    food.setAmountMeasurements(food.getAmountMeasurements() + 1);

    int glucoseMax = MyMath.getMaxFromArrayList(glucoseValuesList);

    if (glucoseMax > food.getMaxGlucose()) {
      Log.d("yolo", "test: joa");
      food.setMaxGlucose(glucoseMax);
    }

    // Add glucose values from current measurement
    ArrayList<Integer> measurementsAll = new ArrayList<>(glucoseValuesList);

    // Add glucose values from old measurements
    for (Measurement m : measurements) {
      measurementsAll.add(m.getGlucoseStart());
      measurementsAll.add(m.getGlucose15());
      measurementsAll.add(m.getGlucose30());
      measurementsAll.add(m.getGlucose45());
      measurementsAll.add(m.getGlucose60());
      measurementsAll.add(m.getGlucose75());
      measurementsAll.add(m.getGlucose90());
      measurementsAll.add(m.getGlucose105());
      measurementsAll.add(m.getGlucose120());
    }

    food.setAverageGlucose(MyMath.getAverageFromArrayList(measurementsAll));

    mViewModel.update(food);
    mViewModel.insertMeasurement(newMeasurement, food);
  }
}

