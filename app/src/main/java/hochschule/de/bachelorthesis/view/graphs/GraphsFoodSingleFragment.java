package hochschule.de.bachelorthesis.view.graphs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentGraphsSingleFoodBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.BarChartValueFormatter;
import hochschule.de.bachelorthesis.utility.MyMath;
import hochschule.de.bachelorthesis.viewmodels.GraphsViewModel;

public class GraphsFoodSingleFragment extends Fragment {

  private FragmentGraphsSingleFoodBinding mBinding;

  private GraphsViewModel mViewModel;

  private final ArrayList<String> mLabelsFoodListDropDown = new ArrayList<>();

  // Line Chart
  private ArrayList<ILineDataSet> mDataSetsLineChart = new ArrayList<>();

  // Bar Chart
  private ArrayList<IBarDataSet> mDataSetsBarChart = new ArrayList<>();


  public void onCreate(@Nullable Bundle savedInstanceState) {
    Objects.requireNonNull(getActivity()).setTitle("Plan");
    super.onCreate(savedInstanceState);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(GraphsViewModel.class);

    setHasOptionsMenu(true);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    // Init data binding
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_graphs_single_food, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

    buildDropdown();

    handleListeners();

    // Default show the line graph
    loadMeasurementsAndBuildGraph();

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.graphs_single_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {

      // chart type 0 displays a line chart to display glucose over time
      case R.id.graphs_single_line_graph:
        mViewModel.getGraphSingleModel().setChartType(0);
        loadMeasurementsAndBuildGraph();
        return true;

      // chart type 1 displays a line chart to display glucose increase over time
      case R.id.graphs_single_line_graph_increase:
        mViewModel.getGraphSingleModel().setChartType(1);
        loadMeasurementsAndBuildGraph();
        return true;

      // chart type 2 displays a bar chart
      case R.id.graphs_single_bar_graph:
        mViewModel.getGraphSingleModel().setChartType(2);
        loadMeasurementsAndBuildGraph();
        return true;

      case R.id.graphs_single_animate:
        animateGraph();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onStop() {
    super.onStop();

    // Save the selected food
    mViewModel.getGraphSingleModel()
        .setSelectedFood(mBinding.dropdownFood.getText().toString());
  }

  /**
   * Builds a dropdown food list, where the user can select with which food he wants to visualize
   * the data with.
   *
   * Loads all existing foods and builds a string with the pattern "food name (brand name)"
   *
   * Save the selected food to the view model, so it can be restored by entering the view again.
   */
  private void buildDropdown() {
    // Update the food list if a new food has been added
    mViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
      @Override
      public void onChanged(List<Food> foods) {
        mLabelsFoodListDropDown.clear();

        for (Food f : foods) {
          // Build string in this form: Food name, brand name
          String s = f.getFoodName() + " (" + f.getBrandName() + ")";
          mLabelsFoodListDropDown.add(s);
        }

        // Sort the list alphabetical
        Collections.sort(mLabelsFoodListDropDown);

        // Creating adapter for dropdown list (spinner)
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
            Objects.requireNonNull(getContext()),
            android.R.layout.simple_spinner_item, mLabelsFoodListDropDown);

        dataAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mBinding.dropdownFood.setAdapter(dataAdapter);
      }
    });
  }

  /**
   * Helper function for all the listeners
   */
  private void handleListeners() {
    // Reacting to a new food selection. This will triggered after the user left this view
    // and when onStop is called.
    mViewModel.getGraphSingleModel().getSelectedFood().observe(getViewLifecycleOwner(),
        new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.dropdownFood.setText(s, false);
            loadMeasurementsAndBuildGraph();
          }
        });

    mBinding.dropdownFood.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadMeasurementsAndBuildGraph();
      }
    });

    // Draw new graph if one radio button has been selected
    mBinding.radioGroupLineStyle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        loadMeasurementsAndBuildGraph();
      }
    });
  }


  private void loadMeasurementsAndBuildGraph() {
    String selectedFood = mBinding.dropdownFood.getText().toString();

    // Leave if no food has been selected yet
    if (selectedFood.equals("")) {
      return;
    }

    // Build foodName and brandName String, String s should be: food name (brand name)
    String[] parts = selectedFood.split(" [(]");

    // Get the food object by the selected food by searching for food name and branding name
    mViewModel
        .getFoodByFoodNameAndBrandName(parts[0], parts[1].substring(0, parts[1].length() - 1))
        .observe(
            getViewLifecycleOwner(), new Observer<Food>() {
              @Override
              public void onChanged(Food food) {
                // If food object got deleted food will be null, leave function
                if (food == null) {
                  // Save the selected food
                  mViewModel.getGraphSingleModel()
                      .setSelectedFood("");
                  return;
                }

                // Get all measurements for the selected food
                mViewModel.getAllMeasurementsByFoodId(food.id)
                    .observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
                      @Override
                      public void onChanged(List<Measurement> measurements) {
                        if (measurements == null) {
                          return;
                        }

                        // Check which graph to build
                        switch (mViewModel.getGraphSingleModel().getChartType()) {
                          // chart type 0 displays a line chart to display glucose over time
                          case 0:
                            buildLineChart(measurements);
                            break;
                          // chart type 1 displays a line chart to display glucose increase over time
                          case 1:
                            buildLineChart(measurements);
                            break;
                          case 2:
                            // chart type 2 displays a bar chart
                            buildBarChart(measurements);
                            break;
                          default:
                            throw new IllegalStateException("Unexpected switch case!");
                        }
                      }
                    });
              }
            });
  }

  /**
   * Building the line chart
   *
   * @param measurements List of measurement values
   */
  private void buildLineChart(List<Measurement> measurements) {
    // Hide bar chart view elements
    mBinding.barChart.setVisibility(View.GONE);

    // display bar chart view elements
    mBinding.radioGroupLineStyle.setVisibility(View.VISIBLE);
    mBinding.lineChart.setVisibility(View.VISIBLE);

    resetLineChart();

    // Diverse Settings
    mBinding.lineChart.setTouchEnabled(false);
    animateGraph();

    // Change description
    if (mViewModel.getGraphSingleModel().getChartType() == 0) {
      mBinding.lineChart.getDescription().setText("Glucose over time");
    } else if (mViewModel.getGraphSingleModel().getChartType() == 1) {
      mBinding.lineChart.getDescription().setText("Glucose increase over time");
    }

    // Left-Axis
    YAxis leftAxis = mBinding.lineChart.getAxisLeft();
    leftAxis.setDrawGridLines(false);

    // Right-Axis
    YAxis rightAxis = mBinding.lineChart.getAxisRight();
    rightAxis.setDrawLabels(false);
    rightAxis.setDrawGridLines(false);

    // X Axis
    XAxis xAxis = mBinding.lineChart.getXAxis();
    xAxis.setDrawGridLines(false);
    xAxis.setAxisMinimum(0f);
    xAxis.setAxisMaximum(120f);
    xAxis.setPosition(XAxisPosition.BOTTOM);

    createPercentileLine(measurements, 0.75f, Color.YELLOW);
    createPercentileLine(measurements, 0.25f, Color.WHITE);

    // Draw either an average line or a median line
    if (mBinding.lineStyleAverage.isChecked()) {
      createAverageLine(measurements);
    } else if (mBinding.lineStyleMedian.isChecked()) {
      createMedianLine(measurements);
    }
  }

  /**
   * Creates data for the percentile line
   *
   * @param measurements List of measurements.
   * @param p percentage for the percentile
   * @param fillColor Fill color
   */
  private void createPercentileLine(List<Measurement> measurements, float p, int fillColor) {

    // check the amount of values
    if (measurements.size() <= 1) {
      return;
    }

    HashMap<String, ArrayList<Integer>> allGlucoseValues = getGlucoseValuesOverTime(
        measurements);

    // Should never happen but if the chart type is false somehow the list will be null,
    // leave in this case
    if (allGlucoseValues == null) {
      return;
    }

    float percentileTileStart = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_start")), p);
    float percentile15 = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_15")), p);
    float percentile30 = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_30")), p);
    float percentile45 = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_45")), p);
    float percentile60 = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_60")), p);
    float percentile75 = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_75")), p);
    float percentile90 = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_90")), p);
    float percentile105 = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_105")), p);
    float percentile120 = MyMath.getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_120")), p);

    // Create Entry ArrayList
    ArrayList<Entry> percentileValues = new ArrayList<>();
    percentileValues.add(new Entry(0, percentileTileStart));
    percentileValues.add(new Entry(15, percentile15));
    percentileValues.add(new Entry(30, percentile30));
    percentileValues.add(new Entry(45, percentile45));
    percentileValues.add(new Entry(60, percentile60));
    percentileValues.add(new Entry(75, percentile75));
    percentileValues.add(new Entry(90, percentile90));
    percentileValues.add(new Entry(105, percentile105));
    percentileValues.add(new Entry(120, percentile120));

    // Create set
    LineDataSet set = new LineDataSet(percentileValues, "Deviation");
    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

    if (fillColor == Color.WHITE) {
      set.setLabel("");
    }

    set.setDrawCircles(false);
    set.setFillAlpha(110);
    set.setLineWidth(1f);
    set.setValueTextSize(10f);
    set.setColor(fillColor);
    set.setValueTextColor(Color.TRANSPARENT);
    set.setFillColor(fillColor);
    set.setFillAlpha(255);
    set.setDrawFilled(true);

    // Create
    mDataSetsLineChart.add(set);

    LineData data = new LineData(mDataSetsLineChart);
    mBinding.lineChart.setData(data);
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.invalidate();
  }

  /**
   * Creates data for the average line
   *
   * @param measurements List of measurements.
   */
  private void createAverageLine(List<Measurement> measurements) {
    if (measurements == null || measurements.size() == 0) {
      return;
    }

    HashMap<String, ArrayList<Integer>> allGlucoseValues = getGlucoseValuesOverTime(
        measurements);

    // Should never happen but if the chart type is false somehow the list will be null,
    // leave in this case
    if (allGlucoseValues == null) {
      return;
    }

    float averageStart = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_start")));
    float average15 = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_15")));
    float average30 = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_30")));
    float average45 = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_45")));
    float average60 = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_60")));
    float average75 = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_75")));
    float average90 = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_90")));
    float average105 = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_105")));
    float average120 = MyMath.calculateMeanFromIntegers(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_120")));

    // Create Entry ArrayList
    ArrayList<Entry> avg_values = new ArrayList<>();
    avg_values.add(new Entry(0, averageStart));
    avg_values.add(new Entry(15, average15));
    avg_values.add(new Entry(30, average30));
    avg_values.add(new Entry(45, average45));
    avg_values.add(new Entry(60, average60));
    avg_values.add(new Entry(75, average75));
    avg_values.add(new Entry(90, average90));
    avg_values.add(new Entry(105, average105));
    avg_values.add(new Entry(120, average120));

    // Create set
    LineDataSet set = new LineDataSet(avg_values, "");

    if (mViewModel.getGraphSingleModel().getChartType() == 0) {
      set.setLabel("Glucose average");
    } else if (mViewModel.getGraphSingleModel().getChartType() == 1) {
      set.setLabel("Glucose increase average");
    }

    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    set.setFillAlpha(110);
    set.setLineWidth(2f);  // how fat is the line
    set.setValueTextSize(12f);
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextColor(getResources().getColor(R.color.colorSecondary));

    // Create
    mDataSetsLineChart.add(set);

    LineData data = new LineData(mDataSetsLineChart);
    mBinding.lineChart.setData(data);
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.invalidate();
  }

  /**
   * Creates data for the median line
   *
   * @param measurements List of measurements.
   */
  private void createMedianLine(List<Measurement> measurements) {

    if (measurements == null || measurements.size() == 0) {
      return;
    }

    HashMap<String, ArrayList<Integer>> allGlucoseValues = getGlucoseValuesOverTime(measurements);

    // Should never happen but if the chart type is false somehow the list will be null,
    // leave in this case
    if (allGlucoseValues == null) {
      return;
    }

    float medianStart = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_start")));
    float median15 = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_15")));
    float median30 = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_30")));
    float median45 = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_45")));
    float median60 = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_60")));
    float median75 = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_75")));
    float median90 = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_90")));
    float median105 = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_105")));
    float median120 = MyMath.getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_120")));

    // Create Entry ArrayList
    ArrayList<Entry> avg_values = new ArrayList<>();
    avg_values.add(new Entry(0, medianStart));
    avg_values.add(new Entry(15, median15));
    avg_values.add(new Entry(30, median30));
    avg_values.add(new Entry(45, median45));
    avg_values.add(new Entry(60, median60));
    avg_values.add(new Entry(75, median75));
    avg_values.add(new Entry(90, median90));
    avg_values.add(new Entry(105, median105));
    avg_values.add(new Entry(120, median120));

    // Set
    LineDataSet set = new LineDataSet(avg_values, "");

    if (mViewModel.getGraphSingleModel().getChartType() == 0) {
      set.setLabel("Glucose median");
    } else if (mViewModel.getGraphSingleModel().getChartType() == 1) {
      set.setLabel("Glucose increase median");
    }

    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    set.setFillAlpha(110);
    set.setLineWidth(2f);
    set.setValueTextSize(12f);
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextColor(getResources().getColor(R.color.colorSecondary));
    mDataSetsLineChart.add(set);

    // Add data
    LineData data = new LineData(mDataSetsLineChart);
    mBinding.lineChart.setData(data);

    // Notify changes
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.invalidate();
  }

  /**
   *
   */
  private void buildBarChart(List<Measurement> measurements) {
    // hide line chart view elements
    mBinding.textViewLineStyle.setVisibility(View.GONE);
    mBinding.radioGroupLineStyle.setVisibility(View.GONE);
    mBinding.lineChart.setVisibility(View.GONE);

    // display bar chart view elements
    mBinding.barChart.setVisibility(View.VISIBLE);

    resetBarChart();

    // Leave function if there is no measurement
    if (measurements == null || measurements.size() == 0) {
      return;
    }

    // Diverse settings for the bar chart
    mBinding.barChart.getDescription().setText("");
    mBinding.barChart.setTouchEnabled(false);
    mBinding.barChart.getLegend().setEnabled(false);
    animateGraph();

    // X Axis (bottom)
    XAxis xAxis = mBinding.barChart.getXAxis();
    xAxis.setDrawGridLines(false);
    xAxis.setPosition(XAxisPosition.BOTTOM); // Shown left instead of right
    xAxis.setLabelCount(5);
    xAxis.setLabelRotationAngle(45f); // To prevent overlapping labels
    String[] values = {"STDEV", "Avg. Increase", "Max. Increase", "Avg. Glucose",
        "Max. Glucose"};
    xAxis.setValueFormatter(new BarChartValueFormatter(values));

    // Y Axis left (left)
    YAxis topAxis = mBinding.barChart.getAxisLeft();
    topAxis.setAxisMinimum(0);
    topAxis.setAxisMaximum(Measurement.getGlucoseMaxFromList(measurements) + 50);

    // Y Axis right (bottom)
    YAxis bottomAxis = mBinding.barChart.getAxisRight();
    bottomAxis.setDrawGridLines(false);
    bottomAxis.setDrawLabels(false);

    ArrayList<BarEntry> dataValues = new ArrayList<>();

    // Entries
    dataValues.add(new BarEntry(0, Measurement.getStandardDeviationFromList(measurements)));
    dataValues.add(new BarEntry(1, Measurement.getGlucoseIncreaseAverageFromList(measurements)));
    dataValues.add(new BarEntry(2, Measurement.getGlucoseIncreaseMaxFromList(measurements)));
    dataValues.add(new BarEntry(3, Measurement.getGlucoseAverageFromList(measurements)));
    dataValues.add(new BarEntry(4, Measurement.getGlucoseMaxFromList(measurements)));

    // Set
    BarDataSet set = new BarDataSet(dataValues, "");
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextSize(10f);
    mDataSetsBarChart.add(set);

    // Add data
    BarData data = new BarData(mDataSetsBarChart);
    data.setBarWidth(0.5f);
    mBinding.barChart.setData(data);

    // Notify changes
    mBinding.barChart.notifyDataSetChanged();
    mBinding.barChart.invalidate();
  }

  /**
   * Helper function which will call the correct buildGlucoseHashMapForAllTimes variant.
   *
   * @param measurements A list of glucose measurements
   * @return A HashMap with the glucose values saved in ArrayLists.
   */
  private HashMap<String, ArrayList<Integer>> getGlucoseValuesOverTime(
      List<Measurement> measurements) {
    if (mViewModel.getGraphSingleModel().getChartType() == 0) {
      return buildGlucoseHashMapForAllTimes(measurements, false);
    } else if (mViewModel.getGraphSingleModel().getChartType() == 1) {
      return buildGlucoseHashMapForAllTimes(measurements, true);
    }
    // Should never happen but leave if chart type is wrong somehow
    else {
      return null;
    }
  }

  /**
   * This function will take all measurements and first remove all unfinished ones. Then there will
   * be created ArrayLists for each time stamp (start value, 15 minute value, ...) Finally, those
   * will be saved in a HashMap and this will be returned.
   *
   * @param measurements A list of glucose measurements.
   * @param isIncrease If isIncrease is true the data will contain the increase rather than the
   * glucose values. Therefore the start value will be subtracted.
   * @return A HashMap with the glucose values saved in ArrayLists
   */
  private HashMap<String, ArrayList<Integer>> buildGlucoseHashMapForAllTimes(
      List<Measurement> measurements,
      boolean isIncrease) {

    if (measurements == null || measurements.size() == 0) {
      return null;
    }

    // Remove unfinished measurements
    Measurement.removeNotFinishedMeasurements(measurements);

    if (measurements.size() == 0) {
      return null;
    }

    // Initializes the objects
    ArrayList<Integer> allStartValues = new ArrayList<>();
    ArrayList<Integer> all15Values = new ArrayList<>();
    ArrayList<Integer> all30Values = new ArrayList<>();
    ArrayList<Integer> all45Values = new ArrayList<>();
    ArrayList<Integer> all60Values = new ArrayList<>();
    ArrayList<Integer> all75Values = new ArrayList<>();
    ArrayList<Integer> all90Values = new ArrayList<>();
    ArrayList<Integer> all105Values = new ArrayList<>();
    ArrayList<Integer> all120Values = new ArrayList<>();

    // Save each glucose values by time in arrays
    if (isIncrease) {
      for (Measurement m : measurements) {
        allStartValues.add(0);
        all15Values.add(m.getGlucose15() - m.getGlucoseStart());
        all30Values.add(m.getGlucose30() - m.getGlucoseStart());
        all45Values.add(m.getGlucose45() - m.getGlucoseStart());
        all60Values.add(m.getGlucose60() - m.getGlucoseStart());
        all75Values.add(m.getGlucose75() - m.getGlucoseStart());
        all90Values.add(m.getGlucose90() - m.getGlucoseStart());
        all105Values.add(m.getGlucose105() - m.getGlucoseStart());
        all120Values.add(m.getGlucose120() - m.getGlucoseStart());
      }
    } else {
      for (Measurement m : measurements) {
        allStartValues.add(m.getGlucoseStart());
        all15Values.add(m.getGlucose15());
        all30Values.add(m.getGlucose30());
        all45Values.add(m.getGlucose45());
        all60Values.add(m.getGlucose60());
        all75Values.add(m.getGlucose75());
        all90Values.add(m.getGlucose90());
        all105Values.add(m.getGlucose105());
        all120Values.add(m.getGlucose120());
      }
    }

    // Fill HashMap with key value pairs
    HashMap<String, ArrayList<Integer>> allGlucoseValues = new HashMap<>();
    allGlucoseValues.put("glucose_values_start", allStartValues);
    allGlucoseValues.put("glucose_values_15", all15Values);
    allGlucoseValues.put("glucose_values_30", all30Values);
    allGlucoseValues.put("glucose_values_45", all45Values);
    allGlucoseValues.put("glucose_values_60", all60Values);
    allGlucoseValues.put("glucose_values_75", all75Values);
    allGlucoseValues.put("glucose_values_90", all90Values);
    allGlucoseValues.put("glucose_values_105", all105Values);
    allGlucoseValues.put("glucose_values_120", all120Values);

    return allGlucoseValues;
  }

  /**
   * Animates either the line chart in x direction or the bar chart in y direction, depending of
   * which chart is currently visible to the user.
   */
  private void animateGraph() {
    if (mBinding.lineChart.getVisibility() == View.VISIBLE) {
      mBinding.lineChart.animateX(1000);
    } else if (mBinding.barChart.getVisibility() == View.VISIBLE) {
      mBinding.barChart.animateY(1000);
    }
  }

  /**
   * Resets the data for the line chart
   */
  private void resetLineChart() {
    mDataSetsLineChart.clear();
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.clear();
    mBinding.lineChart.invalidate();
  }

  /**
   * Resets the data for the bar chart
   */
  private void resetBarChart() {
    mDataSetsBarChart.clear();
    mBinding.barChart.notifyDataSetChanged();
    mBinding.barChart.clear();
    mBinding.barChart.invalidate();
  }
}