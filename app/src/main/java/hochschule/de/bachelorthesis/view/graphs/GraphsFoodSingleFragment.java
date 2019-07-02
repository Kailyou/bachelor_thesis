package hochschule.de.bachelorthesis.view.graphs;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentGraphsSingleFoodBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.MyMath;
import hochschule.de.bachelorthesis.viewmodels.GraphsViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class GraphsFoodSingleFragment extends Fragment {

  private ArrayList<ILineDataSet> mDataSets = new ArrayList<>();

  private FragmentGraphsSingleFoodBinding mBinding;

  private GraphsViewModel mViewModel;

  private Food mChoosenFood;


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
        .inflate(inflater, R.layout.fragment_graphs_single_food, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setViewModel(mViewModel);

    // Dropdown
    final ArrayList<String> labels = new ArrayList<>();

    mViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
      @Override
      public void onChanged(List<Food> foods) {
        for (Food f : foods) {
          // Build string in this form: Food name, brand name
          String s = f.getFoodName() + " (" + f.getBrandName() + ")";
          labels.add(s);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
            Objects.requireNonNull(getContext()),
            android.R.layout.simple_spinner_item, labels);

        dataAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mBinding.dropdownFood.setAdapter(dataAdapter);

        mBinding.dropdownFood.setOnItemClickListener(new OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mViewModel.getGraphModel().setSelectedFood(mBinding.dropdownFood.getText().toString());
          }
        });
      }
    });

    // gets the current value and updates the view
    mViewModel.getGraphModel().getSelectedFood().observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        mBinding.dropdownFood.setText(s, false);

        // Build foodName and brandName String, String s should be: foodname (brandname)
        String[] parts = s.split(" [(]");

        mViewModel
            .getFoodByFoodNameAndBrandName(parts[0], parts[1].substring(0, parts[1].length() - 1))
            .observe(
                getViewLifecycleOwner(), new Observer<Food>() {
                  @Override
                  public void onChanged(Food food) {
                    createLine(food.id);
                  }
                });
      }
    });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.graphs_single_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.graphs_single_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void createLine(int foodId) {
    resetChart();

    // Overall settings
    mBinding.lineChart.setTouchEnabled(false);

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

    mViewModel.getAllMeasurementsByFoodId(foodId)
        .observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
          @Override
          public void onChanged(List<Measurement> measurements) {
            createPercentileLine(measurements, 0.75f,
                getResources().getColor(R.color.colorPrimary));
            createPercentileLine(measurements, 0.25f, Color.WHITE);
            //createMedianLine(measurements);
            createAverageLine(measurements);
          }
        });
  }

  private void createAverageLine(List<Measurement> measurements) {

    if (measurements.size() <= 1) {
      return;
    }

    HashMap<String, ArrayList<Integer>> allGlucoseValues = getAllGlucoseByTime(measurements);

    // Get average values
    int glucose_avg_start = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_start")));
    int glucose_avg_15 = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_15")));
    int glucose_avg_30 = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_30")));
    int glucose_avg_45 = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_45")));
    int glucose_avg_60 = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_60")));
    int glucose_avg_75 = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_75")));
    int glucose_avg_90 = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_90")));
    int glucose_avg_105 = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_105")));
    int glucose_avg_120 = MyMath.getAverageFromArrayList(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_120")));

    // Create Entry ArrayList
    ArrayList<Entry> avg_values = new ArrayList<>();
    avg_values.add(new Entry(0, glucose_avg_start));
    avg_values.add(new Entry(15, glucose_avg_15));
    avg_values.add(new Entry(30, glucose_avg_30));
    avg_values.add(new Entry(45, glucose_avg_45));
    avg_values.add(new Entry(60, glucose_avg_60));
    avg_values.add(new Entry(75, glucose_avg_75));
    avg_values.add(new Entry(90, glucose_avg_90));
    avg_values.add(new Entry(105, glucose_avg_105));
    avg_values.add(new Entry(120, glucose_avg_120));

    // Create set
    LineDataSet set = new LineDataSet(avg_values, "Average Glucose");
    set.setFillAlpha(110);
    set.setLineWidth(1f);  // how fat is the line
    set.setValueTextSize(10f);
    set.setColor(Color.YELLOW);
    set.setValueTextColor(Color.BLACK);

    // Create
    mDataSets.add(set);

    LineData data = new LineData(mDataSets);
    mBinding.lineChart.setData(data);
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.invalidate();
  }

  private void createMedianLine(List<Measurement> measurements) {
    HashMap<String, ArrayList<Integer>> allGlucoseValues = getAllGlucoseByTime(measurements);

    // check the amount of values
    if (measurements.size() == 1) {
      return;
    }

    // Sort the arrays
    for (Map.Entry<String, ArrayList<Integer>> entry : allGlucoseValues.entrySet()) {
      ArrayList<Integer> list = entry.getValue();
      Collections.sort(list);
    }

    float medianStart = getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_start")));
    float median15 = getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_15")));
    float median30 = getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_30")));
    float median45 = getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_45")));
    float median60 = getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_60")));
    float median75 = getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_75")));
    float median90 = getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_90")));
    float median105 = getMedianValue(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_105")));
    float median120 = getMedianValue(
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
    LineDataSet set = new LineDataSet(avg_values, "Median");
    set.setFillAlpha(110);
    set.setLineWidth(2f);
    set.setValueTextSize(12f);
    set.setColor(Color.YELLOW);
    set.setValueTextColor(Color.BLACK);
    mDataSets.add(set);

    // Add data
    LineData data = new LineData(mDataSets);
    mBinding.lineChart.setData(data);

    // Notify changes
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.invalidate();
  }

  private void createPercentileLine(List<Measurement> measurements, float p, int fillColor) {
    HashMap<String, ArrayList<Integer>> allGlucoseValues = getAllGlucoseByTime(measurements);

    // check the amount of values
    if (measurements.size() <= 1) {
      return;
    }

    // Sort the arrays
    for (Map.Entry<String, ArrayList<Integer>> entry : allGlucoseValues.entrySet()) {
      ArrayList<Integer> list = entry.getValue();

      Collections.sort(list);
    }

    float percentileTileStart = getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_start")), p);
    float percentile15 = getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_15")), p);
    float percentile30 = getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_30")), p);
    float percentile45 = getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_45")), p);
    float percentile60 = getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_60")), p);
    float percentile75 = getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_75")), p);
    float percentile90 = getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_90")), p);
    float percentile105 = getPercentile(
        Objects.requireNonNull(allGlucoseValues.get("glucose_values_105")), p);
    float percentile120 = getPercentile(
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
    LineDataSet set = new LineDataSet(percentileValues, "Percentile");
    set.setDrawCircles(false);
    set.setFillAlpha(110);
    set.setLineWidth(1f);
    set.setValueTextSize(10f);
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextColor(Color.TRANSPARENT);
    set.setFillColor(fillColor);
    set.setFillAlpha(255);
    set.setDrawFilled(true);

    // Create
    mDataSets.add(set);

    LineData data = new LineData(mDataSets);
    mBinding.lineChart.setData(data);
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.invalidate();
  }


  /**
   * This function will take all measurements and first remove all unfinished ones. Then there will
   * be created ArrayLists for each time stamp (start value, 15 minute value, ...) Finally, those
   * will be saved in a HashMap and this will be returned.
   *
   * @param measurements A list of glucose measurements.
   * @return A HashMap with the glucose values saved in ArrayLists
   */
  private HashMap<String, ArrayList<Integer>> getAllGlucoseByTime(List<Measurement> measurements) {

    if (measurements == null || measurements.size() == 0) {
      return null;
    }

    Log.d("yolo2", ": measurements all" + measurements.size());

    // Remove unfinished measurements
    measurements.get(0).removeNotFinishedMeasurements(measurements);

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
   * This function will return the median of a list of values.
   *
   * @param values - ArrayList with values inside.
   * @return Returns the median.
   */
  private float getMedianValue(ArrayList<Integer> values) {
    if (values.size() == 0 || values.size() == 1) {
      return 0;
    }

    int amount = values.size();

    // Last bit = 0 even, else odd.
    if ((amount & 1) == 0) {
      // -1 because arrays start at 1
      return (float) 0.5 * (values.get((amount / 2) - 1) + values.get((amount / 2)));
    } else {
      // +1 is missing here because of the -1 because of array index
      return values.get((amount) / 2);
    }
  }

  private float getPercentile(ArrayList<Integer> values, float p) {
    if (values.size() == 0 || values.size() == 1) {
      return 0;
    }

    double k = values.size() * p;

    // Even or odd
    if (k == (int) k) {
      // -1 because arrays start at 1
      float v1 = values.get((int) k - 1);
      float v2 = values.get((int) k);

      return (float) 0.5 * (v1 + v2);
    } else {
      return values.get(((int) Math.ceil(k)) - 1);
    }
  }

  private void resetChart() {
    mDataSets.clear();
    mBinding.lineChart.notifyDataSetChanged();
    mBinding.lineChart.clear();
    mBinding.lineChart.invalidate();
  }
}

