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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementsBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.AdapterMeasurements;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

public class MeasurementsFragment extends Fragment {

  private static final String TAG = MeasurementsFragment.class.getName();

  private FoodViewModel mViewModel;

  private int mFoodId;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(FoodViewModel.class);

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

        final LiveData<Food> ldf = mViewModel.getFoodById(foodId);
        ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
          @Override
          public void onChanged(Food food) {
            ldf.removeObserver(this);

            // Build timestamp
            Date date = new Date(); // current date and time

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());
            String timeStamp = sdf.format(date);

            Measurement newMeasurement = new Measurement(foodId, userHistory.id, timeStamp, 100,
                "test", "not tired",
                100);

            // Random measurement, starting with 100 and ending with 100
            int[] measurements = new int[] {
                100,
                MyMath.getRandomInt(100, 110),
                MyMath.getRandomInt(110, 135),
                MyMath.getRandomInt(135, 150),
                MyMath.getRandomInt(150, 200),
                MyMath.getRandomInt(150, 180),
                MyMath.getRandomInt(135, 145),
                MyMath.getRandomInt(100, 110),
                MyMath.getRandomInt(100, 110),
                100
            };

            newMeasurement.setGlucoseStart(measurements[0]);
            newMeasurement.setGlucose15(measurements[1]);
            newMeasurement.setGlucose30(measurements[2]);
            newMeasurement.setGlucose45(measurements[3]);
            newMeasurement.setGlucose60(measurements[4]);
            newMeasurement.setGlucose75(measurements[5]);
            newMeasurement.setGlucose90(measurements[6]);
            newMeasurement.setGlucose105(measurements[7]);
            newMeasurement.setGlucose120(measurements[8]);
            newMeasurement.setDone(true);

            // update the food object
            food.setAmountMeasurements(food.getAmountMeasurements()+1);

            int glucoseMax = MyMath.getMaxFromArray(measurements);

            if(glucoseMax > food.getMaxGlucose()) {
              food.setMaxGlucose(glucoseMax);
            }

            mViewModel.update(food);

            mViewModel.insertMeasurement(newMeasurement, food);
          }
        });
      }
    });
  }
}
