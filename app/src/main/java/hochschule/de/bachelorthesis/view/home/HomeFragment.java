package hochschule.de.bachelorthesis.view.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
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
import hochschule.de.bachelorthesis.databinding.FragmentHowToUseBinding;
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

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;

    private HomeViewModel mViewModel;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable menu
        setHasOptionsMenu(true);

        // View model
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(HomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_home, container, false);
        loadActiveMeasurement();
        mBinding.setLifecycleOwner(this);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("app_first_run", MODE_PRIVATE);

        // for test
        Navigation.findNavController(Objects.requireNonNull(getView()))
                .navigate(R.id.action_homeFragment_to_onboardingHostFragment);

        /*
        // Navigate to how to use fragment on first use
        if (prefs.getBoolean("first_run", true)) {
            Navigation.findNavController(Objects.requireNonNull(getView()))
                    .navigate(R.id.action_homeFragment_to_onboardingHostFragment);
            prefs.edit().putBoolean("first_run", false).apply();
        }
        */
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.legal_notifications:
                Navigation.findNavController(Objects.requireNonNull(getView()))
                        .navigate(R.id.action_homeFragment_to_legalNotificationsFragment);
                return true;

            case R.id.how_to_use:

                return true;
        }

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

                        boolean isMeasurementActive = false;

                        for (final Measurement measurement : measurements) {
                            if (measurement.isActive()) {

                                isMeasurementActive = true;

                                // Load food object
                                final LiveData<Food> ldFood = mViewModel.getFoodById(measurement.getFoodId());
                                ldFood.observe(getViewLifecycleOwner(), new Observer<Food>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onChanged(Food food) {
                                        ldFood.removeObserver(this);

                                        // Enable the views
                                        mBinding.noMeasurementActive.setVisibility(View.GONE);
                                        mBinding.containerFoodName.setVisibility(View.VISIBLE);
                                        mBinding.containerTimeStarted.setVisibility(View.VISIBLE);
                                        mBinding.containerEndTime.setVisibility(View.VISIBLE);
                                        mBinding.containerInterval.setVisibility(View.VISIBLE);
                                        mBinding.lineChart.setVisibility(View.VISIBLE);
                                        mBinding.update.setVisibility(View.VISIBLE);

                                        mBinding.foodName.setText(food.getFoodName());
                                        mBinding.timeStarted
                                                .setText(Converter.convertTimeStampToTimeStart(measurement.getTimeStamp()));
                                        mBinding.timeEnded
                                                .setText(Converter.convertTimeStampToTimeEnd(measurement.getTimeStamp()));
                                        mBinding.interval.setText(getResources().getString(R.string.minutes_15));
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

                        // If no measurement is active, swap out the text views to inform the user, there is
                        // no measurement active right now.
                        if (!isMeasurementActive) {
                            mBinding.noMeasurementActive.setVisibility(View.VISIBLE);
                            mBinding.containerFoodName.setVisibility(View.GONE);
                            mBinding.containerTimeStarted.setVisibility(View.GONE);
                            mBinding.containerEndTime.setVisibility(View.GONE);
                            mBinding.containerInterval.setVisibility(View.GONE);
                            mBinding.lineChart.setVisibility(View.GONE);
                            mBinding.update.setVisibility(View.GONE);
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

