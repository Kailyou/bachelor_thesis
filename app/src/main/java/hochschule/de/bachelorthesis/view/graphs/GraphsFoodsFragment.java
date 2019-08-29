package hochschule.de.bachelorthesis.view.graphs;

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

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentGraphsFoodsBinding;
import hochschule.de.bachelorthesis.loadFromDb.FoodObject;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.BarChartValueFormatter;
import hochschule.de.bachelorthesis.utility.MyMath;
import hochschule.de.bachelorthesis.viewmodels.GraphsViewModel;

public class GraphsFoodsFragment extends Fragment {

    private FragmentGraphsFoodsBinding mBinding;

    private ArrayList<FoodObject> mFoodObjects = new ArrayList<>();

    private ArrayList<IBarDataSet> mDataSets = new ArrayList<>();

    private GraphsViewModel mViewModel;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Plan");
        super.onCreate(savedInstanceState);

        // View model
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(GraphsViewModel.class);

        // Enable menu
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_graphs_foods, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        loadFoodDataAndBuildChart();

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.graphs_all_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.graphs_all_glucose_increase_max:
                mViewModel.getGraphAllModel().setChartType(0);
                loadFoodDataAndBuildChart();
                return true;

            case R.id.graphs_all_glucose_max:
                mViewModel.getGraphAllModel().setChartType(1);
                loadFoodDataAndBuildChart();
                return true;

            case R.id.graphs_all_glucose_avg:
                mViewModel.getGraphAllModel().setChartType(2);
                loadFoodDataAndBuildChart();
                return true;

            case R.id.graphs_all_stdev:
                mViewModel.getGraphAllModel().setChartType(3);
                loadFoodDataAndBuildChart();
                return true;

            case R.id.graphs_all_animate:
                mBinding.chart.animateY(1000);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This function loads all foods, then loads all measurements for each food, calculates the max
     * and average glucose and saves all into one wrapper object. Finally the function to draw the
     * graphs will be called after all data has been collected.
     */
    private void loadFoodDataAndBuildChart() {
        clearData();

        // First load all food objects
        mViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(final List<Food> foods) {

                for (final Food food : foods) {

                    mViewModel.getAllMeasurementsByFoodId(food.id).observe(getViewLifecycleOwner(),
                            new Observer<List<Measurement>>() {
                                @Override
                                public void onChanged(List<Measurement> measurements) {
                                    // Create food data object and save it to the list
                                    mFoodObjects.add(new FoodObject(food, measurements));

                                    // If all values has been collected, build the graph
                                    if (mFoodObjects.size() == foods.size()) {
                                        buildChart();
                                    }
                                }
                            });
                }
            }
        });
    }

    /**
     * Takes the wrapper object and removes all objects where one value is zero, which removes all
     * food objects with not at least one finished measurement inside. This needs to be done here
     * because if I do it in the load function, my finish condition is not working anymore.
     * (mFoodObjects.length == foods.size).
     * <p>
     * Depending on the selected chart type to display, call the correct function and build the
     * chart.
     */
    private void buildChart() {
        if (mFoodObjects.size() == 0) {
            return;
        }

        // Remove all unfinished measurements
        Iterator<FoodObject> iterator = mFoodObjects.iterator();

        while (iterator.hasNext()) {
            FoodObject current = iterator.next();
            if (current.getGlucoseMax() == 0
                    || current.getGlucoseAvg() < 0.1f) {
                iterator.remove();
            }
        }

        // Diverse settings for the bar chart
        mBinding.chart.getDescription().setEnabled(false);
        mBinding.chart.setTouchEnabled(false);
        mBinding.chart.getLegend().setEnabled(false);
        mBinding.chart.animateY(1000);

        // X Axis (left)
        XAxis xAxis = mBinding.chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxisPosition.BOTTOM); // Shown left instead of right

        // Y Axis left (top)
        YAxis topAxis = mBinding.chart.getAxisLeft();
        topAxis.setAxisMinimum(0);

        // Y Axis right (bottom)
        YAxis bottomAxis = mBinding.chart.getAxisRight();
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setDrawLabels(false);

        // Check which graph to build
        switch (mViewModel.getGraphAllModel().getChartType()) {
            case 0:
                createChartGlucoseIncreaseMax();
                break;
            case 1:
                createChartGlucoseMax();
                break;
            case 2:
                createChartGlucoseAverage();
                break;
            case 3:
                createChartGlucoseStandardDeviation();
                break;
            default:
                throw new IllegalStateException("Unexpected switch case!");
        }
    }


    /**
     * First sort the data in glucose increase max order. (From low to max)
     * <p>
     * then get the data for the chart and call the function, which will
     * <p>
     * set up the data and show the graph.
     */
    private void createChartGlucoseIncreaseMax() {
        // Update header
        mBinding.header.setText(getResources().getString(R.string.header_glucose_increase_max));

        // Sort
        Collections.sort(mFoodObjects, new Comparator<FoodObject>() {
            @Override
            public int compare(FoodObject o1, FoodObject o2) {
                return o1.getGlucoseIncreaseMax().compareTo(o2.getGlucoseIncreaseMax());
            }
        });

        Collections.reverse(mFoodObjects);

        ArrayList<Integer> glucoseIncreaseMaxValues = new ArrayList<>();

        for (int i = 0; i < mFoodObjects.size(); i++) {
            glucoseIncreaseMaxValues.add(mFoodObjects.get(i).getGlucoseIncreaseMax());
        }

        // Set max
        mBinding.chart.getAxisLeft()
                .setAxisMaximum(MyMath.calculateMaxFromIntList(glucoseIncreaseMaxValues) + 50);

        // Entries
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        for (int i = 0; i < glucoseIncreaseMaxValues.size(); ++i) {
            dataValues.add(new BarEntry(i, glucoseIncreaseMaxValues.get(i)));
        }

        // Set
        finishGraph(new BarDataSet(dataValues, "Glucose Increase Max."));
    }

    /**
     * First sort the data in glucose max order. (From low to max)
     * <p>
     * then get the data for the chart and call the function, which will
     * <p>
     * set up the data and show the graph.
     */
    private void createChartGlucoseMax() {
        // Update header
        mBinding.header.setText(getResources().getString(R.string.header_glucose_max));

        // Sort
        Collections.sort(mFoodObjects, new Comparator<FoodObject>() {
            @Override
            public int compare(FoodObject o1, FoodObject o2) {
                return o1.getGlucoseMax().compareTo(o2.getGlucoseMax());
            }
        });

        Collections.reverse(mFoodObjects);

        ArrayList<Integer> glucoseMaxValues = new ArrayList<>();

        for (int i = 0; i < mFoodObjects.size(); i++) {
            glucoseMaxValues.add(mFoodObjects.get(i).getGlucoseMax());
        }

        // Set max
        mBinding.chart.getAxisLeft()
                .setAxisMaximum(MyMath.calculateMaxFromIntList(glucoseMaxValues) + 200);

        // Entries
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        for (int i = 0; i < glucoseMaxValues.size(); ++i) {
            dataValues.add(new BarEntry(i, glucoseMaxValues.get(i)));
        }

        // Set
        finishGraph(new BarDataSet(dataValues, "Glucose Max."));
    }

    /**
     * First sort the data in glucose average order. (From low to max)
     * <p>
     * then get the data for the chart and call the function, which will
     * <p>
     * set up the data and show the graph.
     */
    private void createChartGlucoseAverage() {
        // Update header
        mBinding.header.setText(getResources().getString(R.string.header_glucose_average));

        // Sort
        Collections.sort(mFoodObjects, new Comparator<FoodObject>() {
            @Override
            public int compare(FoodObject o1, FoodObject o2) {
                return o1.getGlucoseAvg().compareTo(o2.getGlucoseAvg());
            }
        });

        Collections.reverse(mFoodObjects);

        ArrayList<Float> glucoseAverageValues = new ArrayList<>();

        for (int i = 0; i < mFoodObjects.size(); i++) {
            glucoseAverageValues.add((float) mFoodObjects.get(i).getGlucoseAvg());
        }

        // Set max
        mBinding.chart.getAxisLeft()
                .setAxisMaximum(MyMath.calculateMaxFromFloatList(glucoseAverageValues) + 200);

        // Entries
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        for (int i = 0; i < glucoseAverageValues.size(); ++i) {
            dataValues.add(new BarEntry(i, glucoseAverageValues.get(i)));
        }

        // Set
        finishGraph(new BarDataSet(dataValues, "Average"));
    }

    /**
     * First sort the data in standard deviation order. (From low to max)
     * <p>
     * then get the data for the chart and call the function, which will
     * <p>
     * set up the data and show the graph.
     */
    private void createChartGlucoseStandardDeviation() {
        // Update header
        mBinding.header.setText(getResources().getString(R.string.standard_deviation));

        // Sort
        Collections.sort(mFoodObjects, new Comparator<FoodObject>() {
            @Override
            public int compare(FoodObject o1, FoodObject o2) {
                return o1.getStandardDeviation().compareTo(o2.getStandardDeviation());
            }
        });

        Collections.reverse(mFoodObjects);

        ArrayList<Float> glucoseStdevValues = new ArrayList<>();

        for (int i = 0; i < mFoodObjects.size(); i++) {
            glucoseStdevValues.add((float) mFoodObjects.get(i).getStandardDeviation());
        }

        // Set max
        mBinding.chart.getAxisLeft()
                .setAxisMaximum(MyMath.calculateMaxFromFloatList(glucoseStdevValues) + 20);

        // Entries
        ArrayList<BarEntry> dataValues = new ArrayList<>();
        for (int i = 0; i < glucoseStdevValues.size(); ++i) {
            dataValues.add(new BarEntry(i, glucoseStdevValues.get(i)));
        }

        // Set
        finishGraph(new BarDataSet(dataValues, "Standard Deviation"));
    }

    /**
     * Finish the graph by setting the label, the data, doing some final formatting and notify the
     * changes to the chart itself to display them.
     */
    private void finishGraph(BarDataSet set) {
        // Set label
        XAxis xAxis = mBinding.chart.getXAxis();
        String[] labels = new String[mFoodObjects.size()];

        for (int i = 0; i < mFoodObjects.size(); i++) {
            labels[i] = mFoodObjects.get(i).getFood().getFoodName();
        }

        xAxis.setLabelCount(labels.length);
        xAxis.setValueFormatter(new BarChartValueFormatter(labels));

        // Format
        set.setColor(getResources().getColor(R.color.colorPrimary));
        set.setValueTextSize(10f);
        set.setValueTextColor(getResources().getColor(R.color.colorPrimary));
        mDataSets.add(set);

        // Add data
        BarData data = new BarData(mDataSets);
        data.setBarWidth(0.5f);
        mBinding.chart.setData(data);

        // Notify changes
        mBinding.chart.notifyDataSetChanged();
        mBinding.chart.invalidate();
    }

    /**
     * Clear all data used for building the chart.
     */
    private void clearData() {
        mFoodObjects.clear();
        mDataSets.clear();
        mBinding.chart.notifyDataSetChanged();
        mBinding.chart.clear();
        mBinding.chart.invalidate();
    }
}



