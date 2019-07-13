package hochschule.de.bachelorthesis.view.graphs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Arrays;
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

    loadDataFromFoods();

    return mBinding.getRoot();
  }

  /**
   * This function loads all foods, then loads all measurements for each food, calculates the max
   * and average glucose and saves all into one wrapper object. Finally the function to draw the
   * graphs will be called after all data has been collected.
   */
  private void loadDataFromFoods() {
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
      if(current.getGlucoseMax() == 0
      || current.getGlucoseAverage() < 0.1f) {
        iterator.remove();
      }
    }

    // Diverse settings for the bar chart
    mBinding.barChartGlucoseMax.getDescription().setEnabled(false);
    mBinding.barChartGlucoseMax.setTouchEnabled(false);
    mBinding.barChartGlucoseMax.getLegend().setEnabled(false);
    mBinding.barChartGlucoseMax.animateY(2000);

    // X Axis (left)
    XAxis xAxis = mBinding.barChartGlucoseMax.getXAxis();
    xAxis.setDrawGridLines(false);
    xAxis.setPosition(XAxisPosition.BOTTOM); // Shown left instead of right

    // Y Axis left (top)
    YAxis topAxis = mBinding.barChartGlucoseMax.getAxisLeft();
    topAxis.setAxisMinimum(0);

    // Y Axis right (bottom)
    YAxis bottomAxis = mBinding.barChartGlucoseMax.getAxisRight();
    bottomAxis.setDrawGridLines(false);
    bottomAxis.setDrawLabels(false);

    // TODO, check which bar graph should be build
    createBarChartGlucoseMax();
  }

  private void createBarChartGlucoseMax() {
    // Sort
    Collections.sort(mFoodData, new Comparator<FoodData>() {
      @Override
      public int compare(FoodData o1, FoodData o2) {
        return o1.getGlucoseMax().compareTo(o2.getGlucoseMax());
      }
    });

    // Labels
    String[] labels = new String[mFoodData.size()];
    ArrayList<Integer> glucoseMaxValues = new ArrayList<>();
    ArrayList<Float> glucoseAverageValues = new ArrayList<>();

    for (int i = 0; i < mFoodData.size(); i++) {
      labels[i] = mFoodData.get(i).getFoodName();
      glucoseMaxValues.add(mFoodData.get(i).getGlucoseMax());
      glucoseAverageValues.add(mFoodData.get(i).getGlucoseAverage());
    }

    // X Axis (left)
    XAxis xAxis = mBinding.barChartGlucoseMax.getXAxis();
    xAxis.setLabelCount(labels.length);
    xAxis.setValueFormatter(new BarChartValueFormatter(labels));

    // Y Axis left (top)
    mBinding.barChartGlucoseMax.getAxisLeft()
        .setAxisMaximum(MyMath.getMaxFromArrayList(glucoseMaxValues) + 20);

    // Entries
    ArrayList<BarEntry> dataValues = new ArrayList<>();
    for (int i = 0; i < glucoseMaxValues.size(); ++i) {
      dataValues.add(new BarEntry(i, glucoseMaxValues.get(i)));
    }

    // Set
    BarDataSet set = new BarDataSet(dataValues, "Glucose Max");
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextSize(10f);
    set.setValueTextColor(getResources().getColor(R.color.colorPrimary));
    mDataSets.add(set);

    // Add data
    BarData data = new BarData(mDataSets);
    mBinding.barChartGlucoseMax.setData(data);

    // Notify changes
    mBinding.barChartGlucoseMax.notifyDataSetChanged();
    mBinding.barChartGlucoseMax.invalidate();
  }

  private void clearData() {
    mFoodData.clear();
    mDataSets.clear();
    mBinding.barChartGlucoseMax.notifyDataSetChanged();
    mBinding.barChartGlucoseMax.clear();
    mBinding.barChartGlucoseMax.invalidate();
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

    public String getFoodName() {
      return mFoodName;
    }

    public Integer getGlucoseMax() {
      return mGlucoseMax;
    }

    public Float getGlucoseAverage() {
      return mGlucoseAverage;
    }
  }
}



