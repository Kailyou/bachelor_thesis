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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.ArrayList;
import java.util.Objects;

public class MeasurementFragment extends Fragment {

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

    // get passed measurement id
    assert getArguments() != null;
    mMeasurementId = getArguments().getInt("measurement_id");

    // fab
    Bundle bundle = new Bundle();
    bundle.putInt("measurement_id", mMeasurementId);

    mBinding.fab.setOnClickListener(Navigation
        .createNavigateOnClickListener(R.id.action_measurementFragment_to_editMeasurementFragment,
            bundle));

    mViewModel.getMeasurementById(mMeasurementId).observe(getViewLifecycleOwner(),
        new Observer<Measurement>() {
          @Override
          public void onChanged(Measurement measurement) {
            if (measurement == null) {
              return;
            }

            /* Update text views */

            // Time information
            mBinding.date.setText(
                Converter.convertTimeStampToDate(measurement.getTimeStamp()));

            mBinding.timeStarted
                .setText(Converter.convertTimeStampToTimeStart(measurement.getTimeStamp()));

            mBinding.timeEnded
                .setText(Converter.convertTimeStampToTimeEnd(measurement.getTimeStamp()));

            // Advance information
            mBinding.isGi.setText(Converter.convertBoolean(measurement.isGi()));
            mBinding.amount.setText(String.valueOf(measurement.getAmount()));
            mBinding.stress.setText(measurement.getStress());
            mBinding.tired.setText(measurement.getTired());
            mBinding.physicallyActive
                .setText(Converter.convertBoolean(measurement.isPhysicallyActivity()));
            mBinding.alcoholConsumed
                .setText(Converter.convertBoolean(measurement.isAlcoholConsumed()));

            // Events
            mBinding.ill.setText(Converter.convertBoolean(measurement.isIll()));
            mBinding.medication.setText(Converter.convertBoolean(measurement.isMedication()));
            mBinding.period.setText(Converter.convertBoolean(measurement.isPeriod()));

            // Analyses
            mBinding.glucoseMax.setText(String.valueOf(measurement.getGlucoseMax()));
            mBinding.glucoseAverage.setText(String.valueOf((int) measurement.getGlucoseAverage()));
            mBinding.integral.setText(String.valueOf((int) measurement.getIntegral()));
            mBinding.standardDeviation
                .setText(String.valueOf((int) measurement.getStandardDeviation()));

            // Glucose Values
            mBinding.glucoseStart.setText(String.valueOf(measurement.getGlucoseStart()));
            mBinding.glucose15.setText(String.valueOf(measurement.getGlucose15()));
            mBinding.glucose30.setText(String.valueOf(measurement.getGlucose30()));
            mBinding.glucose45.setText(String.valueOf(measurement.getGlucose45()));
            mBinding.glucose60.setText(String.valueOf(measurement.getGlucose60()));
            mBinding.glucose75.setText(String.valueOf(measurement.getGlucose75()));
            mBinding.glucose90.setText(String.valueOf(measurement.getGlucose90()));
            mBinding.glucose105.setText(String.valueOf(measurement.getGlucose105()));
            mBinding.glucose120.setText(String.valueOf(measurement.getGlucose120()));

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
      new AlertDialog.Builder(Objects.requireNonNull(getContext()))
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
