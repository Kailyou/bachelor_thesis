package hochschule.de.bachelorthesis.view.home;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import hochschule.de.bachelorthesis.databinding.FragmentHomeBinding;
import hochschule.de.bachelorthesis.databinding.FragmentMeBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.viewmodels.HomeViewModel;
import hochschule.de.bachelorthesis.viewmodels.MeViewModel;
import java.util.ArrayList;
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
                    buildGraph(measurement);

                    // fab
                    // navigate to the update fragment and pass the food's id.
                    Bundle bundle = new Bundle();
                    bundle.putInt("measurement_id", measurement.id);
                    mBinding.update.setOnClickListener(Navigation
                        .createNavigateOnClickListener(R.id.action_homeFragment_to_updateFragment,
                            bundle));
                  }
                });
              }
            }
          }
        });
  }

  /**
   * Builds a line graph of the given measurement values
   *
   * @param measurement The selected measurement
   */
  private void buildGraph(Measurement measurement) {
    if (measurement == null) {
      return;
    }

    // test
    ArrayList<Entry> values = new ArrayList<>();
    values.add(new Entry(0, measurement.getGlucoseStart()));
    if (measurement.getGlucose15() != 0) {
      values.add(new Entry(15, measurement.getGlucose15()));
    }

    if (measurement.getGlucose30() != 0) {
      values.add(new Entry(30, measurement.getGlucose30()));
    }

    if (measurement.getGlucose45() != 0) {
      values.add(new Entry(45, measurement.getGlucose45()));
    }

    if (measurement.getGlucose60() != 0) {
      values.add(new Entry(60, measurement.getGlucose60()));
    }

    if (measurement.getGlucose75() != 0) {
      values.add(new Entry(75, measurement.getGlucose75()));
    }

    if (measurement.getGlucose90() != 0) {
      values.add(new Entry(90, measurement.getGlucose90()));
    }

    if (measurement.getGlucose105() != 0) {
      values.add(new Entry(105, measurement.getGlucose105()));
    }

    if (measurement.getGlucose120() != 0) {
      values.add(new Entry(120, measurement.getGlucose120()));
    }

    // Overall settings
    mBinding.lineChart.setTouchEnabled(false);

    // X-Axis
    XAxis xAxis = mBinding.lineChart.getXAxis();
    xAxis.setAxisMinimum(0f);
    xAxis.setAxisMaximum(120f);
    xAxis.setPosition(XAxisPosition.BOTTOM);

    LineDataSet set = new LineDataSet(values, "Glucose");
    set.setFillAlpha(110);
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setLineWidth(3f);  // how fat is the line
    set.setValueTextSize(10f);
    set.setValueTextColor(getResources().getColor(R.color.colorPrimary));

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    dataSets.add(set);

    LineData data = new LineData(dataSets);
    mBinding.lineChart.setData(data);
    mBinding.lineChart.getDescription().setEnabled(false);
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.invalidate();
  }
}

