package hochschule.de.bachelorthesis.view.home;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import hochschule.de.bachelorthesis.databinding.FragmentHomeBinding;
import hochschule.de.bachelorthesis.databinding.FragmentMeBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.viewmodels.HomeViewModel;
import hochschule.de.bachelorthesis.viewmodels.MeViewModel;
import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;

public class HomeFragment extends Fragment {

  private HomeViewModel mViewModel;

  private FragmentHomeBinding mBinding;

  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Change title
    Objects.requireNonNull(getActivity()).setTitle("Home");

    // Enable menu
    setHasOptionsMenu(true);

    // View model
    mViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    // Init data binding
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_home, container, false);
    mBinding.setLifecycleOwner(this);

    loadActiveMeasurement();

    return mBinding.getRoot();
  }


  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.home_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    return super.onOptionsItemSelected(item);
  }

  private void loadActiveMeasurement() {

    // Load all measurements
    final LiveData<List<Measurement>> ldMeasurements = mViewModel.getAllMeasurements();
    ldMeasurements.observe(getViewLifecycleOwner(),
        new Observer<List<Measurement>>() {
          @Override
          public void onChanged(List<Measurement> measurements) {
            ldMeasurements.removeObserver(this);

            for (final Measurement measurement : measurements) {
              if (measurement.isActive()) {

                // Load food object
                final LiveData<Food> ldFood = mViewModel.getFoodById(measurement.getFoodId());
                ldFood.observe(getViewLifecycleOwner(), new Observer<Food>() {
                  @Override
                  public void onChanged(Food food) {
                    ldFood.removeObserver(this);

                    mBinding.foodName.setText(food.getFoodName());
                    mBinding.timeStarted
                        .setText(Converter.convertTimeStampToTimeStart(measurement.getTimeStamp()));
                    mBinding.timeEnded
                        .setText(Converter.convertTimeStampToTimeEnd(measurement.getTimeStamp()));
                    mBinding.interval.setText(String.valueOf(15));
                  }
                });
              }
            }
          }
        });
  }
}

