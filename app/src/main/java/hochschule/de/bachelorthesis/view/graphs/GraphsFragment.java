package hochschule.de.bachelorthesis.view.graphs;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import hochschule.de.bachelorthesis.databinding.FragmentGraphsBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import hochschule.de.bachelorthesis.viewmodels.GraphsViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;

public class GraphsFragment extends Fragment {

  private LineChart mChart;

  private ArrayList<ArrayList<Entry>> mAllEntries;
  private ArrayList<ILineDataSet> mDataSets = new ArrayList<>();

  private FragmentGraphsBinding mBinding;

  private GraphsViewModel mViewModel;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    Objects.requireNonNull(getActivity()).setTitle("Plan");
    super.onCreate(savedInstanceState);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(GraphsViewModel.class);

    // Enable menu
    setHasOptionsMenu(true);

    mAllEntries = new ArrayList<>();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_graphs, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setViewModel(mViewModel);

    mBinding.lineChart.getAxisLeft().setDrawGridLines(false);
    mBinding.lineChart.getXAxis().setDrawGridLines(false);

    getGlucoseDataFromAllMeasurementsByFood();

    /*
    LimitLine upper_limit = new LimitLine(200f, "Danger");
    upper_limit.setLineWidth(4f);
    upper_limit.enableDashedLine(10f, 10f, 0f);
    upper_limit.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
    upper_limit.setTextSize(15f);

    LimitLine lower_limit = new LimitLine(120f, "Too Low");
    lower_limit.setLineWidth(4f);
    lower_limit.enableDashedLine(10f, 10f, 0f);
    lower_limit.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
    lower_limit.setTextSize(15f);


    YAxis leftAxis = mChart.getAxisLeft();
    leftAxis.removeAllLimitLines();
    //leftAxis.addLimitLine(upper_limit);
    //leftAxis.addLimitLine(lower_limit);
    //leftAxis.setAxisMinimum(50f);
    //leftAxis.setAxisMaximum(200f);
    leftAxis.enableGridDashedLine(10f, 10f, 0);
    leftAxis.setDrawLimitLinesBehindData(true);

    XAxis xAxis = mChart.getXAxis();
    xAxis.setAxisMinimum(0f);
    xAxis.setAxisMaximum(120f);
    xAxis.setPosition(XAxisPosition.BOTTOM);


    //mChart.setDragEnabled(true);
    //mChart.setScaleEnabled(false);

    ArrayList<Entry> yValues = new ArrayList<>();
    yValues.add(new Entry(0, 100f));
    yValues.add(new Entry(15, 110f));
    yValues.add(new Entry(30, 125f));
    yValues.add(new Entry(45, 130f));
    yValues.add(new Entry(60, 150f));
    yValues.add(new Entry(75, 170f));
    yValues.add(new Entry(90, 160f));
    yValues.add(new Entry(105, 135));
    yValues.add(new Entry(120, 110));

    LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
    set1.setFillAlpha(110);
    set1.setColor(Color.BLUE);
    set1.setLineWidth(3f);  // how fat is the line
    set1.setValueTextSize(10f);
    set1.setValueTextColor(Color.BLUE);

    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    dataSets.add(set1);

    LineData data = new LineData(dataSets);

    mChart.setData(data);
    */

    return mBinding.getRoot();
  }

  private void getGlucoseDataFromAllMeasurementsByFood() {

    Log.d("yolo", "onChanged: ist noch nicht drin");

    mViewModel.getAllMeasurementsById(1)
        .observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
          @Override
          public void onChanged(List<Measurement> measurements) {

            Log.d("yolo", "onChanged: ist drin");

            for (Measurement m : measurements) {
              ArrayList<Entry> values = new ArrayList<>();
              values.add(new Entry(0, m.getGlucoseStart()));
              values.add(new Entry(15, m.getGlucose15()));
              values.add(new Entry(30, m.getGlucose30()));
              values.add(new Entry(45, m.getGlucose45()));
              values.add(new Entry(60, m.getGlucose60()));
              values.add(new Entry(75, m.getGlucose75()));
              values.add(new Entry(90, m.getGlucose90()));
              values.add(new Entry(105, m.getGlucose105()));
              values.add(new Entry(120, m.getGlucose120()));

              mAllEntries.add(values);
            }

            for (ArrayList<Entry> entries : mAllEntries) {
              LineDataSet set = new LineDataSet(entries, "Test");
              set.setFillAlpha(110);
              //set.setColor(Color.BLUE);
              set.setLineWidth(3f);  // how fat is the line
              set.setValueTextSize(10f);
              //set.setValueTextColor(Color.BLUE);

              mDataSets.add(set);
            }

            LineData data = new LineData(mDataSets);
            mBinding.lineChart.setData(data);

            mBinding.lineChart.notifyDataSetChanged();
            mBinding.lineChart.invalidate();
          }
        });
  }
}

