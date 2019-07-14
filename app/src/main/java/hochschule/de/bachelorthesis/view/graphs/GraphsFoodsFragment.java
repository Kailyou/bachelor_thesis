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

    loadFoodDataAndBuildGraph();

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.graphs_all_menu, menu);
  }

  // TODO, remove those ids and get new one for animating graph maybe.
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.graphs_all_glucose_max:
        mViewModel.getGraphAllModel().setChartType(0);
        loadFoodDataAndBuildGraph();
        return true;

      case R.id.graphs_all_glucose_avg:
        mViewModel.getGraphAllModel().setChartType(1);
        loadFoodDataAndBuildGraph();
        return true;

      case R.id.graphs_all_integral:
        mViewModel.getGraphAllModel().setChartType(2);
        loadFoodDataAndBuildGraph();
        return true;

      case R.id.graphs_all_stdev:
        mViewModel.getGraphAllModel().setChartType(3);
        loadFoodDataAndBuildGraph();
        return true;

      case R.id.graphs_all_animate:
        mBinding.chart.animateY(2000);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * This function loads all foods, then loads all measurements for each food, calculates the max
   * and average glucose and saves all into one wrapper object. Finally the function to draw the
   * graphs will be called after all data has been collected.
   */
  private void loadFoodDataAndBuildGraph() {
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
                      Measurement.getGlucoseAverageFromList(measurements));

                  mFoodData.add(foodData);

                  // If all values has been collected, build the graph
                  if (mFoodData.size() == foods.size()) {
                    buildBarChart();
                  }
                }
              });
        }
      }
    });
  }

  private void buildBarChart() {
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
    mBinding.chart.animateY(2000);

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
        createBarChartGlucoseMax();
        break;
      case 1:
        createBarChartGlucoseAverage();
        break;
      case 2:
        break;
      case 3:
        break;
      default:
        throw new IllegalStateException("Unexpected switch case!");
    }
  }

  private void createBarChartGlucoseMax() {
    // Sort
    Collections.sort(mFoodData, new Comparator<FoodData>() {
      @Override
      public int compare(FoodData o1, FoodData o2) {
        return o1.getGlucoseMax().compareTo(o2.getGlucoseMax());
      }
    });

    ArrayList<Integer> glucoseMaxValues = new ArrayList<>();

    for (int i = 0; i < mFoodData.size(); i++) {
      glucoseMaxValues.add(mFoodData.get(i).getGlucoseMax());
    }

    // Set max
    mBinding.chart.getAxisLeft()
        .setAxisMaximum(MyMath.getMaxFromIntegerArrayList(glucoseMaxValues) + 20);

    // Entries
    ArrayList<BarEntry> dataValues = new ArrayList<>();
    for (int i = 0; i < glucoseMaxValues.size(); ++i) {
      dataValues.add(new BarEntry(i, glucoseMaxValues.get(i)));
    }

    // Set
    finishGraph(new BarDataSet(dataValues, "Glucose Max"));
  }

  private void createBarChartGlucoseAverage() {
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
        .setAxisMaximum(MyMath.getMaxFromFloatArrayList(glucoseAverageValues) + 20);

    // Entries
    ArrayList<BarEntry> dataValues = new ArrayList<>();
    for (int i = 0; i < glucoseAverageValues.size(); ++i) {
      dataValues.add(new BarEntry(i, glucoseAverageValues.get(i)));
    }

    // Set
    finishGraph(new BarDataSet(dataValues, "Glucose Average"));
  }

  private void finishGraph(BarDataSet set) {
    // Set label
    String[] labels = new String[mFoodData.size()];
    XAxis xAxis = mBinding.chart.getXAxis();
    xAxis.setLabelCount(labels.length);
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
    mBinding.chart.setData(data);

    // Notify changes
    mBinding.chart.notifyDataSetChanged();
    mBinding.chart.invalidate();
  }

  private void clearData() {
    mFoodData.clear();
    mDataSets.clear();
    mBinding.chart.notifyDataSetChanged();
    mBinding.chart.clear();
    mBinding.chart.invalidate();
  }

  private class FoodData {

    private String mFoodName;
    private Integer mGlucoseMax;
    private Float mGlucoseAverage;

    private FoodData(String foodName, int maxGlucose, float averageGlucose) {
      mFoodName = foodName;
      mGlucoseMax = maxGlucose;
      mGlucoseAverage = averageGlucose;
    }

    private String getFoodName() {
      return mFoodName;
    }

    private Integer getGlucoseMax() {
      return mGlucoseMax;
    }

    private Float getGlucoseAverage() {
      return mGlucoseAverage;
    }
  }
}



