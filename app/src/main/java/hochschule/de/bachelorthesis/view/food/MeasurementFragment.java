package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.icu.util.Measure;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.ArrayList;
import java.util.Objects;

public class MeasurementFragment extends Fragment {

  private static final String TAG = MeasurementFragment.class.getName();

  private FoodViewModel mViewModel;

  private FragmentMeasurementBinding mBinding;

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
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_measurement, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

    // get passed measurement id
    assert getArguments() != null;
    mMeasurementId = getArguments().getInt("measurement_id");

    // fab
    Bundle bundle = new Bundle();
    bundle.putInt("measurement_id", mMeasurementId);

    mBinding.fab.setOnClickListener(Navigation
        .createNavigateOnClickListener(R.id.action_measurementFragment_to_editMeasurementFragment));

    mViewModel.getMeasurementById(mMeasurementId).observe(getViewLifecycleOwner(),
        new Observer<Measurement>() {
          @Override
          public void onChanged(Measurement measurement) {
            mViewModel.loadMeasurementFragment(measurement);
            buildGraph(measurement);
          }
        });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.measurement_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.delete_measurement) {
      new AlertDialog.Builder(getContext())
          .setTitle("Delete Confirmation")
          .setMessage(
              "You are about to delete this measurement.\n\nIt cannot be restored at a later time!\n\nContinue?")
          .setIcon(android.R.drawable.ic_delete)
          .setPositiveButton(android.R.string.yes, new OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
              if (whichButton == -1) {
                deleteMeasurement();
              }
            }
          })
          .setNegativeButton(android.R.string.no, null).show();

      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void deleteMeasurement() {

    final LiveData<Measurement> ldm = mViewModel.getMeasurementById(mMeasurementId);

    ldm.observe(getViewLifecycleOwner(), new Observer<Measurement>() {
      @Override
      public void onChanged(Measurement measurement) {
        ldm.removeObserver(this);
        mViewModel.deleteMeasurement(measurement);
      }
    });
  }

  /**
   * Builds a line graph of the given measurement values
   *
   * @param measurement - The selected measurement
   *
   * TODO - maybe add something like the following point's won't be drawn if the previous one was
   * zero (wrong input?)
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

    LineDataSet set = new LineDataSet(values, "Glucose");
    set.setFillAlpha(110);
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setLineWidth(3f);  // how fat is the line
    set.setValueTextSize(10f);
    set.setValueTextColor(getResources().getColor(R.color.colorPrimary));

    XAxis xAxis = mBinding.test.getXAxis();
    xAxis.setAxisMinimum(0f);
    xAxis.setAxisMaximum(120f);
    xAxis.setPosition(XAxisPosition.BOTTOM);

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    dataSets.add(set);

    LineData data = new LineData(dataSets);
    mBinding.test.setData(data);
    mBinding.test.getDescription().setEnabled(false);
    mBinding.test.notifyDataSetChanged();
    mBinding.test.invalidate();
  }
}
