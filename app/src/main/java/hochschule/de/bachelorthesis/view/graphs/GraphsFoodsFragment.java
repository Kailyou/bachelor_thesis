package hochschule.de.bachelorthesis.view.graphs;

import android.os.Bundle;
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
import hochschule.de.bachelorthesis.utility.FoodAnalyser;
import hochschule.de.bachelorthesis.viewmodels.GraphsViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphsFoodsFragment extends Fragment {

  private FragmentGraphsFoodsBinding mBinding;

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

    // Get all foods
    mViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
      @Override
      public void onChanged(List<Food> foods) {
        createTestBarChart();
      }
    });

    return mBinding.getRoot();
  }

  private void createBarChartWithAllFoods() {
    mViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
      @Override
      public void onChanged(List<Food> foods) {

      }
    });
  }

  private void createTestBarChart() {
    // Diverse settings for the bar chart
    mBinding.barChart.getDescription().setText("Max Glucose");
    mBinding.barChart.setTouchEnabled(false);
    mBinding.barChart.getLegend().setEnabled(false);
    mBinding.barChart.animateY(2000);

    // X Axis (left)
    XAxis xAxis = mBinding.barChart.getXAxis();
    xAxis.setDrawGridLines(false);
    xAxis.setPosition(XAxisPosition.BOTTOM); // Shown left instead of right
    xAxis.setLabelCount(5);
    String[] values = {"Food 1", "Food 2", "Food 3", "Food 4", "Food 5"};
    xAxis.setValueFormatter(new MyValueFormatter(values));

    // Y Axis left (top)
    YAxis topAxis = mBinding.barChart.getAxisLeft();
    topAxis.setAxisMinimum(0);
    topAxis.setAxisMaximum(250f);

    // Y Axis right (bottom)
    YAxis bottomAxis = mBinding.barChart.getAxisRight();
    bottomAxis.setDrawGridLines(false);
    bottomAxis.setDrawLabels(false);

    // Entries
    ArrayList<BarEntry> dataValues1 = new ArrayList<>();
    dataValues1.add(new BarEntry(0, 170));
    dataValues1.add(new BarEntry(1, 200));
    dataValues1.add(new BarEntry(2, 170));
    dataValues1.add(new BarEntry(3, 150));
    dataValues1.add(new BarEntry(4, 126));

    // Set
    BarDataSet set = new BarDataSet(dataValues1, "Test");
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextSize(10f);
    set.setValueTextColor(getResources().getColor(R.color.colorPrimary));
    mDataSets.add(set);

    // Add data
    BarData data = new BarData(mDataSets);
    mBinding.barChart.setData(data);

    // Notify changes
    mBinding.barChart.notifyDataSetChanged();
    mBinding.barChart.invalidate();
  }

  private void createBarForFood(Food food, int xPosi) {

    // Get the highest glucose value for that food
    mViewModel.getAllMeasurementsById(food.id).observe(getViewLifecycleOwner(),
        new Observer<List<Measurement>>() {
          @Override
          public void onChanged(List<Measurement> measurements) {
            int maxGlucose = FoodAnalyser.getMaxGlucoseForFood(measurements);

          }
        });

    ArrayList<BarEntry> dataValues1 = new ArrayList<>();

    // Create set
    BarDataSet set = new BarDataSet(dataValues1, "Test");
    set.setValueTextSize(10f);
    set.setColor(getResources().getColor(R.color.colorPrimary));
    set.setValueTextColor(getResources().getColor(R.color.colorPrimary));

    // Create
    mDataSets.add(set);

    BarData data = new BarData(mDataSets);
    mBinding.barChart.setData(data);
    mBinding.barChart.notifyDataSetChanged();
    mBinding.barChart.invalidate();
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
}

