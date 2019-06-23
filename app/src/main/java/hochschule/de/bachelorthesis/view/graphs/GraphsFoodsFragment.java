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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentGraphsFoodsBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.viewmodels.GraphsViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class GraphsFoodsFragment extends Fragment {

  private FragmentGraphsFoodsBinding mBinding;

  private ArrayList<IBarDataSet> mDataSets = new ArrayList<>();
  private ArrayList<IBarDataSet> mDataSets2 = new ArrayList<>();

  private GraphsViewModel mViewModel;

  private ArrayList<BarCharSaveObject> mFoodData = new ArrayList<>();


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

    //createTestBarChart();
    loadDataFromFoods();

    return mBinding.getRoot();
  }

  /**
   * This function loads all foods, then loads all measurements for each food, calculates the max
   * and average glucose and saves all into one wrapper object. Finally the function to draw the
   * graphs will be called after all data has been collected.
   */
  private void loadDataFromFoods() {
    // First load all food objects
    mViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
      @Override
      public void onChanged(final List<Food> foods) {

        for (final Food food : foods) {

          Log.d("yolo", "onChanged: foodid1 = " + food.id);

          mViewModel.getAllMeasurementsByFoodId(food.id).observe(getViewLifecycleOwner(),
              new Observer<List<Measurement>>() {
                @Override
                public void onChanged(List<Measurement> measurements) {
                  int glucoseMax = 0;
                  int glucoseAverage = 0;

                  for (Measurement m : measurements) {
                    if (glucoseMax < m.getGlucoseMax()) {
                      glucoseMax = m.getGlucoseMax();
                    }

                    if (glucoseAverage < m.getGlucoseAverage()) {
                      glucoseAverage = m.getGlucoseAverage();
                    }
                  }

                  mFoodData
                      .add(new BarCharSaveObject(food.getFoodName(), glucoseMax, glucoseAverage));

                  if (mFoodData.size() == foods.size()) {
                    createGlucoseMaxChartFromAllFoods();
                    createGlucoseAverageChartFromAllFoods();
                  }
                }
              });
        }
      }
    });
  }

  private void createGlucoseMaxChartFromAllFoods() {
    // First sort the list
    Comparator<BarCharSaveObject> compareByGlucoseMax = new Comparator<BarCharSaveObject>() {
      @Override
      public int compare(BarCharSaveObject o1, BarCharSaveObject o2) {
        return o1.getGlucoseMax().compareTo(o2.getGlucoseMax());
      }
    };

    // Remove all empty
    Iterator<BarCharSaveObject> iterator = mFoodData.iterator();
    while (iterator.hasNext()) {
      BarCharSaveObject barCharSaveObject = iterator.next();
      if (barCharSaveObject.getGlucoseMax() == 0) {
        iterator.remove();
      }
    }

    Collections.sort(mFoodData, compareByGlucoseMax);

    String[] labels = new String[mFoodData.size()];

    // Diverse settings for the bar chart
    mBinding.barChartGlucoseMax.getDescription().setText("Max Glucose");
    mBinding.barChartGlucoseMax.setTouchEnabled(false);
    mBinding.barChartGlucoseMax.getLegend().setEnabled(false);
    mBinding.barChartGlucoseMax.animateY(2000);

    // X Axis (left)
    XAxis xAxis = mBinding.barChartGlucoseMax.getXAxis();
    xAxis.setDrawGridLines(false);
    xAxis.setPosition(XAxisPosition.BOTTOM); // Shown left instead of right
    xAxis.setLabelCount(mFoodData.size());
    xAxis.setValueFormatter(new MyValueFormatter(labels));

    // Y Axis left (top)
    YAxis topAxis = mBinding.barChartGlucoseMax.getAxisLeft();
    topAxis.setAxisMinimum(0);
    topAxis.setAxisMaximum(250f);

    // Y Axis right (bottom)
    YAxis bottomAxis = mBinding.barChartGlucoseMax.getAxisRight();
    bottomAxis.setDrawGridLines(false);
    bottomAxis.setDrawLabels(false);

    // Labels and Entries
    ArrayList<BarEntry> dataValues = new ArrayList<>();
    for (int i = 0; i < mFoodData.size(); ++i) {
      labels[i] = mFoodData.get(i).getFoodName();
      dataValues.add(new BarEntry(i, mFoodData.get(i).mGlucoseMax));
    }

    // Set
    BarDataSet set = new BarDataSet(dataValues, "Test");
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

  private void createGlucoseAverageChartFromAllFoods() {
    // First sort the list
    Comparator<BarCharSaveObject> compareByGlucoseAverage = new Comparator<BarCharSaveObject>() {
      @Override
      public int compare(BarCharSaveObject o1, BarCharSaveObject o2) {
        return o1.getGlucoseMax().compareTo(o2.getGlucoseAverage());
      }
    };

    // Remove all empty
    Iterator<BarCharSaveObject> iterator = mFoodData.iterator();
    while (iterator.hasNext()) {
      BarCharSaveObject barCharSaveObject = iterator.next();
      if (barCharSaveObject.getGlucoseMax() == 0) {
        iterator.remove();
      }
    }

    Collections.sort(mFoodData, compareByGlucoseAverage);

    String[] labels = new String[mFoodData.size()];

    // Diverse settings for the bar chart
    mBinding.barChartGlucoseAverage.getDescription().setText("Average Glucose");
    mBinding.barChartGlucoseAverage.setTouchEnabled(false);
    mBinding.barChartGlucoseAverage.getLegend().setEnabled(false);
    mBinding.barChartGlucoseAverage.animateY(2000);

    // X Axis (left)
    XAxis xAxis = mBinding.barChartGlucoseAverage.getXAxis();
    xAxis.setDrawGridLines(false);
    xAxis.setPosition(XAxisPosition.BOTTOM); // Shown left instead of right
    xAxis.setLabelCount(mFoodData.size());
    xAxis.setValueFormatter(new MyValueFormatter(labels));

    // Y Axis left (top)
    YAxis topAxis = mBinding.barChartGlucoseAverage.getAxisLeft();
    topAxis.setAxisMinimum(0);
    topAxis.setAxisMaximum(250f);

    // Y Axis right (bottom)
    YAxis bottomAxis = mBinding.barChartGlucoseAverage.getAxisRight();
    bottomAxis.setDrawGridLines(false);
    bottomAxis.setDrawLabels(false);

    // Labels and Entries
    ArrayList<BarEntry> dataValues = new ArrayList<>();
    for (int i = 0; i < mFoodData.size(); ++i) {
      labels[i] = mFoodData.get(i).getFoodName();
      dataValues.add(new BarEntry(i, mFoodData.get(i).mGlucoseAverage));
    }

    // Set
    BarDataSet set = new BarDataSet(dataValues, "Test");
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextSize(10f);
    set.setValueTextColor(getResources().getColor(R.color.colorPrimary));
    mDataSets2.add(set);

    // Add data
    BarData data = new BarData(mDataSets2);
    mBinding.barChartGlucoseAverage.setData(data);

    // Notify changes
    mBinding.barChartGlucoseAverage.notifyDataSetChanged();
    mBinding.barChartGlucoseAverage.invalidate();
  }

  private void createTestBarChart() {

    // Diverse settings for the bar chart
    mBinding.barChartGlucoseMax.getDescription().setText("Max Glucose");
    mBinding.barChartGlucoseMax.setTouchEnabled(false);
    mBinding.barChartGlucoseMax.getLegend().setEnabled(false);
    //mBinding.barChart.animateY(2000);

    // X Axis (left)
    XAxis xAxis = mBinding.barChartGlucoseMax.getXAxis();
    xAxis.setDrawGridLines(false);
    xAxis.setPosition(XAxisPosition.BOTTOM); // Shown left instead of right
    xAxis.setLabelCount(5);
    String[] values = {"Food 1", "Food 2", "Food 3", "Food 4", "Food 5"};
    xAxis.setValueFormatter(new MyValueFormatter(values));

    // Y Axis left (top)
    YAxis topAxis = mBinding.barChartGlucoseMax.getAxisLeft();
    topAxis.setAxisMinimum(0);
    topAxis.setAxisMaximum(250f);

    // Y Axis right (bottom)
    YAxis bottomAxis = mBinding.barChartGlucoseMax.getAxisRight();
    bottomAxis.setDrawGridLines(false);
    bottomAxis.setDrawLabels(false);

    // Entries
    ArrayList<BarEntry> dataValues = new ArrayList<>();
    dataValues.add(new BarEntry(0, 170));
    dataValues.add(new BarEntry(1, 200));
    dataValues.add(new BarEntry(2, 170));
    dataValues.add(new BarEntry(3, 150));
    dataValues.add(new BarEntry(4, 126));

    // Set
    BarDataSet set = new BarDataSet(dataValues, "Test");
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

  private class MyValueFormatter extends ValueFormatter {

    private String[] mLabels;

    private MyValueFormatter(String[] labels) {
      mLabels = labels;
    }

    @Override
    public String getFormattedValue(float value) {
      return mLabels[(int) value];
    }
  }

  private class BarCharSaveObject {

    private String mFoodName;
    private Integer mGlucoseMax;
    private Integer mGlucoseAverage;

    public BarCharSaveObject(String foodName, int maxGlucose, int averageGlucose) {
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

    public Integer getGlucoseAverage() {
      return mGlucoseAverage;
    }
  }
}

