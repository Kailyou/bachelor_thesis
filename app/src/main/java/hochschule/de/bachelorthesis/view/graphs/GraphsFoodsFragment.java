package hochschule.de.bachelorthesis.view.graphs;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentGraphsFoodsBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.BarChartValueFormatter;
import hochschule.de.bachelorthesis.utility.MyMath;
import hochschule.de.bachelorthesis.viewmodels.GraphsViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class GraphsFoodsFragment extends Fragment {

  private FragmentGraphsFoodsBinding mBinding;

  private ArrayList<FoodData> mFoodData = new ArrayList<>();

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
      case R.id.graphs_all_glucose_max:
        mViewModel.getGraphAllModel().setChartType(0);
        loadFoodDataAndBuildChart();
        return true;

      case R.id.graphs_all_glucose_avg:
        mViewModel.getGraphAllModel().setChartType(1);
        loadFoodDataAndBuildChart();
        return true;

      case R.id.graphs_all_integral:
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
                  FoodData foodData = new FoodData(
                      food.getFoodName(),
                      Measurement.getGlucoseMaxFromList(measurements),
                      Measurement.getGlucoseAverageFromList(measurements),
                      Measurement.getAverageIntegralFromList(measurements),
                      0f);

                  mFoodData.add(foodData);

                  // If all values has been collected, build the graph
                  if (mFoodData.size() == foods.size()) {
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
   * (mFoodData.length == foods.size).
   *
   * Depending on the selected chart type to display, call the correct function and build the
   * chart.
   */
  private void buildChart() {
    if (mFoodData.size() == 0) {
      return;
    }

    // Remove all unfinished measurements
    Iterator<FoodData> iterator = mFoodData.iterator();

    while (iterator.hasNext()) {
      FoodData current = iterator.next();
      if (current.getGlucoseMax() == 0
          || current.getGlucoseAverage() < 0.1f) {
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
        createChartGlucoseMax();
        break;
      case 1:
        createChartGlucoseAverage();
        break;
      case 2:
        createChartGlucoseIntegral();
        break;
      case 3:
        break;
      default:
        throw new IllegalStateException("Unexpected switch case!");
    }
  }

  /**
   * First sort the data in glucose max order. (From low to max)
   *
   * then get the data for the chart and call the function, which will
   *
   * set up the data and show the graph.
   */
  private void createChartGlucoseMax() {
    // Sort
    Collections.sort(mFoodData, new Comparator<FoodData>() {
      @Override
      public int compare(FoodData o1, FoodData o2) {
        return o1.getGlucoseMax().compareTo(o2.getGlucoseMax());
      }
    });

    Collections.reverse(mFoodData);

    ArrayList<Integer> glucoseMaxValues = new ArrayList<>();

    for (int i = 0; i < mFoodData.size(); i++) {
      glucoseMaxValues.add(mFoodData.get(i).getGlucoseMax());
    }

    // Set max
    mBinding.chart.getAxisLeft()
        .setAxisMaximum(MyMath.calculateMaxFromIntList(glucoseMaxValues) + 20);

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
   *
   * then get the data for the chart and call the function, which will
   *
   * set up the data and show the graph.
   */
  private void createChartGlucoseAverage() {
    // Sort
    Collections.sort(mFoodData, new Comparator<FoodData>() {
      @Override
      public int compare(FoodData o1, FoodData o2) {
        return o1.getGlucoseAverage().compareTo(o2.getGlucoseAverage());
      }
    });

    ArrayList<Float> glucoseAverageValues = new ArrayList<>();

    for (int i = 0; i < mFoodData.size(); i++) {
      glucoseAverageValues.add(mFoodData.get(i).getGlucoseAverage());
    }

    // Set max
    mBinding.chart.getAxisLeft()
        .setAxisMaximum(MyMath.calculateMaxFromFloatList(glucoseAverageValues) + 20);

    // Entries
    ArrayList<BarEntry> dataValues = new ArrayList<>();
    for (int i = 0; i < glucoseAverageValues.size(); ++i) {
      dataValues.add(new BarEntry(i, glucoseAverageValues.get(i)));
    }

    // Set
    finishGraph(new BarDataSet(dataValues, "Average"));
  }

  /**
   * First sort the data in glucose integral order. (From low to max)
   *
   * then get the data for the chart and call the function, which will
   *
   * set up the data and show the graph.
   */
  private void createChartGlucoseIntegral() {
    // Sort
    Collections.sort(mFoodData, new Comparator<FoodData>() {
      @Override
      public int compare(FoodData o1, FoodData o2) {
        return o1.getIntegral().compareTo(o2.getIntegral());
      }
    });

    ArrayList<Float> glucoseIntegralValues = new ArrayList<>();

    for (int i = 0; i < mFoodData.size(); i++) {
      glucoseIntegralValues.add(mFoodData.get(i).getIntegral());
    }

    // Set max
    mBinding.chart.getAxisLeft()
        .setAxisMaximum(MyMath.calculateMaxFromFloatList(glucoseIntegralValues) + 20);

    // Entries
    ArrayList<BarEntry> dataValues = new ArrayList<>();
    for (int i = 0; i < glucoseIntegralValues.size(); ++i) {
      dataValues.add(new BarEntry(i, glucoseIntegralValues.get(i)));
    }

    // Set
    finishGraph(new BarDataSet(dataValues, "Integral"));
  }

  /**
   * Finish the graph by setting the label, the data, doing some final formatting and notify the
   * changes to the chart itself to display them.
   */
  private void finishGraph(BarDataSet set) {
    // Set label
    String[] labels = new String[mFoodData.size()];
    XAxis xAxis = mBinding.chart.getXAxis();
    xAxis.setLabelCount(labels.length, true);
    xAxis.setValueFormatter(new BarChartValueFormatter(labels));

    for (int i = 0; i < mFoodData.size(); i++) {
      labels[i] = mFoodData.get(i).getFoodName();
    }

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
    mFoodData.clear();
    mDataSets.clear();
    mBinding.chart.notifyDataSetChanged();
    mBinding.chart.clear();
    mBinding.chart.invalidate();
  }

  /**
   * Wrapper object for the food data.
   *
   * This is mainly needed because in the function where the data are loaded, to save the data in
   * and have a possibility for a condition.
   *
   * If the length of the list of objects with this wrapper object is equal to the amount of food
   * objects, all data have been saved.
   */
  private class FoodData {

    private String mFoodName;
    private Integer mMax;
    private Float mAverage;
    private Float mIntegral;
    private Float mStdev;

    private FoodData(String foodName, int max, float average, float integral, float stdev) {
      mFoodName = foodName;
      mMax = max;
      mAverage = average;
      mIntegral = integral;
      mStdev = stdev;
    }

    private String getFoodName() {
      return mFoodName;
    }

    private Integer getGlucoseMax() {
      return mMax;
    }

    private Float getGlucoseAverage() {
      return mAverage;
    }

    public Float getIntegral() {
      return mIntegral;
    }

    public Float getStdev() {
      return mStdev;
    }
  }
}



