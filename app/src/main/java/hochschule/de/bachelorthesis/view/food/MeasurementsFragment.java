package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;
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
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.utility.Samples;
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
        Measurement header = new Measurement(0, 0, false, "", 0, "", "", false, false, false, false,
            false,
            0);
        measurements.add(0, header);
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
      case R.id.add_tmp_measurement_unfinished:
        createTemplateMeasurement(false);
        return true;
      case R.id.add_tmp_measurement_finished:
        createTemplateMeasurement(true);
        return true;
      case R.id.delete_measurements:
        deleteMeasurements();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void createTemplateMeasurement(final boolean finished) {

    // Load all measurements to check later if all has been finished
    final LiveData<List<Measurement>> ldMeasurements = mViewModel.getAllMeasurements();
    ldMeasurements.observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
      @Override
      public void onChanged(List<Measurement> allMeasurements) {
        ldMeasurements.removeObserver(this);

        // Only insert if no other measurement is active right now
        for (Measurement m : allMeasurements) {
          if (m.isActive()) {
            MySnackBar
                .createSnackBar(getContext(),
                    "Cannot add measurement because another one is still active!");

            return;
          }
        }

        // Load user data
        final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
        ldu.observe(getViewLifecycleOwner(), new Observer<UserHistory>() {
          @Override
          public void onChanged(final UserHistory userHistory) {
            ldu.removeObserver(this);

            // Leave if no user data has been set yet
            if (userHistory == null) {
              MySnackBar.createSnackBar(getContext(), "Enter user data first!");
              return;
            }

            Measurement templateMeasurement;

            // Create either an unfinished or a finished measurement
            if (finished) {
              templateMeasurement = Samples.getRandomMeasurement(
                  Objects.requireNonNull(getContext()), mFoodId, userHistory.id);
            } else {
              templateMeasurement = Samples
                  .getRandomMeasurementUnfinished(Objects.requireNonNull(getContext()), mFoodId,
                      userHistory.id);
            }

            mViewModel.insertMeasurement(templateMeasurement);
          }
        });
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
        mViewModel.updateFood(food);
      }
    });
  }
}

