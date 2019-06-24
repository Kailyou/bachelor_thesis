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
import hochschule.de.bachelorthesis.utility.Samples;
import java.util.ArrayList;
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

    // get passed food id
    assert getArguments() != null;
    mFoodId = getArguments().getInt("food_id");
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
    final AdapterMeasurements adapter = new AdapterMeasurements(navController);

    recyclerView.setAdapter(adapter);

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
        createTemplateMeasurement();
        return true;
      case R.id.delete_measurements:
        deleteMeasurements();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void createTemplateMeasurement() {
    final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
    ldu.observe(getViewLifecycleOwner(), new Observer<UserHistory>() {
      @Override
      public void onChanged(final UserHistory userHistory) {
        ldu.removeObserver(this);

        if (userHistory == null) {
          return;
        }

        buildsNewMeasurementAndUpdateDatabase(userHistory.id);

      }
    });
  }

  private void deleteMeasurements() {
    final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);
    ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(Food food) {
        ldf.removeObserver(this);

        // Update database
        mViewModel.deleteAllMeasurementFromFoodWithId(mFoodId);
        mViewModel.update(food);
      }
    });
  }

  private void buildsNewMeasurementAndUpdateDatabase(int userHistoryId) {

    final Measurement newMeasurement = Samples.getRandomMeasurement(
        Objects.requireNonNull(getContext()), mFoodId, userHistoryId);

    // Max glucose
    ArrayList<Integer> glucoseValues = new ArrayList<>();
    glucoseValues.add(newMeasurement.getGlucoseStart());
    glucoseValues.add(newMeasurement.getGlucose15());
    glucoseValues.add(newMeasurement.getGlucose30());
    glucoseValues.add(newMeasurement.getGlucose45());
    glucoseValues.add(newMeasurement.getGlucose60());
    glucoseValues.add(newMeasurement.getGlucose75());
    glucoseValues.add(newMeasurement.getGlucose90());
    glucoseValues.add(newMeasurement.getGlucose105());
    glucoseValues.add(newMeasurement.getGlucose120());

    mViewModel.insertMeasurement(newMeasurement);
  }
}

