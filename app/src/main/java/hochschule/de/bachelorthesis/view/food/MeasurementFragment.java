package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementBinding;
import hochschule.de.bachelorthesis.loadFromDb.MeasurementObject;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

/**
 * @author Maik Thielen
 * <p>
 * First, there will be the measurement object loaded.
 * <p>
 * Then, the views will be updated to display the data of the measurement.
 * <p>
 * A graph will be built, which shows the current glucose progress.
 * <p>
 * The user is able to delete the measurement by clicking on the delete button in the action bar.
 */
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
                    public void onChanged(final Measurement measurement) {
                        if (measurement == null) {
                            return;
                        }

                        // LOAD REF FOOD
                        final LiveData<Food> ldf = mViewModel.getFoodByFoodNameAndBrandName("Glucose", "Reference Product");
                        ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
                            @Override
                            public void onChanged(Food refFood) {
                                ldf.removeObserver(this);

                                // If there is no ref food, just update the text views, GI will be
                                // 0 in this case.
                                if (refFood == null) {
                                    MeasurementObject mo = new MeasurementObject(measurement, null);
                                    updateTextViews(mo);
                                    return;
                                }

                                // LOAD ALL MEASUREMENTS FOR REF FOOD
                                final LiveData<List<Measurement>> ldm = mViewModel.getAllMeasurementsById(refFood.id);
                                ldm.observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
                                    @Override
                                    public void onChanged(List<Measurement> refMeasurements) {
                                        ldm.removeObserver(this);

                                        // Build the Measurement object
                                        MeasurementObject mo = new MeasurementObject(measurement, refMeasurements);
                                        updateTextViews(mo);
                                    }
                                });
                            }
                        });

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

    /**
     * Deletes the measurement
     */
    private void deleteMeasurement() {

        // LOAD MEASUREMENT
        final LiveData<Measurement> ldm = mViewModel.getMeasurementById(mMeasurementId);
        ldm.observe(getViewLifecycleOwner(), new Observer<Measurement>() {
            @Override
            public void onChanged(Measurement measurement) {
                ldm.removeObserver(this);

                if (measurement == null) {
                    return;
                }

                mViewModel.deleteMeasurement(measurement);

                // LOAD FOOD
                final LiveData<Food> ldf = mViewModel.getFoodById(measurement.getFoodId());
                ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
                    @Override
                    public void onChanged(Food food) {
                        ldf.removeObserver(this);

                        if (food == null) {
                            return;
                        }

                        // Navigate back
                        Bundle bundle = new Bundle();
                        bundle.putInt("food_id", food.id);
                        bundle.putString("title", food.getFoodName());

                        Navigation.findNavController(Objects.requireNonNull(getView()))
                                .navigate(R.id.action_measurementFragment_to_foodHostFragment, bundle);
                    }
                });
            }
        });
    }

    /**
     * Updates the text views
     *
     * @param mo the measurement object, which contains all data for a measurement
     */
    private void updateTextViews(MeasurementObject mo) {

        // Time information
        mBinding.date.setText(mo.getDate());
        mBinding.timeStarted.setText(mo.getTimeStarted());
        mBinding.timeEnded.setText(mo.getTimeEnded());

        // Advance information
        mBinding.isGi.setText(Converter.convertBoolean(getContext(), mo.getMeasurement().isGi()));
        mBinding.amount.setText(String.valueOf(mo.getMeasurement().getAmount()));
        mBinding.stress.setText(mo.getMeasurement().getStress());
        mBinding.tired.setText(mo.getMeasurement().getTired());
        mBinding.physicallyActive
                .setText(Converter.convertBoolean(getContext(), mo.getMeasurement().isPhysicallyActivity()));
        mBinding.alcoholConsumed
                .setText(Converter.convertBoolean(getContext(), mo.getMeasurement().isAlcoholConsumed()));

        // Events
        mBinding.ill.setText(Converter.convertBoolean(getContext(), mo.getMeasurement().isIll()));
        mBinding.medication.setText(Converter.convertBoolean(getContext(), mo.getMeasurement().isMedication()));
        mBinding.period.setText(Converter.convertBoolean(getContext(), mo.getMeasurement().isPeriod()));

        // Analyses
        mBinding.glucoseMax.setText(String.valueOf(mo.getGlucoseMax()));
        mBinding.glucoseAverage.setText(String.valueOf(mo.getGlucoseAvg()));
        mBinding.glucoseIncreaseMax.setText(String.valueOf(mo.getGlucoseIncreaseMax()));
        mBinding.standardDeviation.setText(String.valueOf(mo.getStandardDeviation()));

        // Set text color depending of the GI result
        if (mo.getGi() < 55)
            mBinding.gi.setTextColor(getResources().getColor(R.color.gi_low));
        else if (mo.getGi() < 71)
            mBinding.gi.setTextColor(getResources().getColor(R.color.gi_mid));
        else
            mBinding.gi.setTextColor(getResources().getColor(R.color.gi_high));

        mBinding.gi.setTypeface(null, Typeface.BOLD);
        mBinding.gi.setText(String.valueOf(mo.getGi()));

        // Glucose Values
        mBinding.glucoseStart.setText(String.valueOf(mo.getMeasurement().getGlucoseStart()));
        mBinding.glucose15.setText(String.valueOf(mo.getMeasurement().getGlucose15()));
        mBinding.glucose30.setText(String.valueOf(mo.getMeasurement().getGlucose30()));
        mBinding.glucose45.setText(String.valueOf(mo.getMeasurement().getGlucose45()));
        mBinding.glucose60.setText(String.valueOf(mo.getMeasurement().getGlucose60()));
        mBinding.glucose75.setText(String.valueOf(mo.getMeasurement().getGlucose75()));
        mBinding.glucose90.setText(String.valueOf(mo.getMeasurement().getGlucose90()));
        mBinding.glucose105.setText(String.valueOf(mo.getMeasurement().getGlucose105()));
        mBinding.glucose120.setText(String.valueOf(mo.getMeasurement().getGlucose120()));
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
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
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
