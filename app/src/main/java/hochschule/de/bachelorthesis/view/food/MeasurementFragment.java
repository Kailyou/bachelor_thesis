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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.Objects;

public class MeasurementFragment extends Fragment {

  private static final String TAG = MeasurementFragment.class.getName();

  private FoodViewModel mViewModel;

  private int mMeasurementId;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
    FragmentMeasurementBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_measurement, container, false);
    binding.setLifecycleOwner(getViewLifecycleOwner());
    binding.setVm(mViewModel);

    // get passed measurement id
    assert getArguments() != null;
    mMeasurementId = getArguments().getInt("measurement_id");

    // fab
    Bundle bundle = new Bundle();
    bundle.putInt("measurement_id", mMeasurementId);

    binding.fab.setOnClickListener(Navigation
        .createNavigateOnClickListener(R.id.action_measurementFragment_to_editMeasurementFragment));

    mViewModel.getMeasurementById(mMeasurementId).observe(getViewLifecycleOwner(),
        new Observer<Measurement>() {
          @Override
          public void onChanged(Measurement measurement) {
            mViewModel.loadMeasurementFragment(measurement);
          }
        });

    return binding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.measurement_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId()
        == R.id.delete_measurement) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
