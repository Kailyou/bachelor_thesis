package hochschule.de.bachelorthesis.view.graphs;

import android.os.Bundle;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import hochschule.de.bachelorthesis.databinding.FragmentGraphsBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.MyMath;
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

    // X Axis
    XAxis xAxis = mBinding.lineChart.getXAxis();
    xAxis.setAxisMinimum(0f);
    xAxis.setAxisMaximum(120f);
    xAxis.setPosition(XAxisPosition.BOTTOM);

    return mBinding.getRoot();
  }

  private void getGlucoseDataFromAllMeasurementsByFood() {

    Log.d("yolo", "onChanged: ist noch nicht drin");

    mViewModel.getAllMeasurementsById(1)
        .observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
          @Override
          public void onChanged(List<Measurement> measurements) {

            ArrayList<Integer> allStartValues = new ArrayList<>();
            ArrayList<Integer> all15Values = new ArrayList<>();
            ArrayList<Integer> all30Values = new ArrayList<>();
            ArrayList<Integer> all45Values = new ArrayList<>();
            ArrayList<Integer> all60Values = new ArrayList<>();
            ArrayList<Integer> all75Values = new ArrayList<>();
            ArrayList<Integer> all90Values = new ArrayList<>();
            ArrayList<Integer> all105Values = new ArrayList<>();
            ArrayList<Integer> all120Values = new ArrayList<>();

            // save
            for (Measurement m : measurements) {
              allStartValues.add(m.getGlucoseStart());
            }

            for (Measurement m : measurements) {
              allStartValues.add(m.getGlucoseStart());
            }

            for (Measurement m : measurements) {
              all15Values.add(m.getGlucose15());
            }

            for (Measurement m : measurements) {
              all30Values.add(m.getGlucose30());
            }

            for (Measurement m : measurements) {
              all45Values.add(m.getGlucose45());
            }

            for (Measurement m : measurements) {
              all60Values.add(m.getGlucose60());
            }

            for (Measurement m : measurements) {
              all75Values.add(m.getGlucose75());
            }

            for (Measurement m : measurements) {
              all90Values.add(m.getGlucose90());
            }

            for (Measurement m : measurements) {
              all105Values.add(m.getGlucose105());
            }

            for (Measurement m : measurements) {
              all120Values.add(m.getGlucose120());
            }

            // Get average values
            int glucose_avg_start = MyMath.getAverageFromArrayList(allStartValues);
            int glucose_avg_15 = MyMath.getAverageFromArrayList(all15Values);
            int glucose_avg_30 = MyMath.getAverageFromArrayList(all30Values);
            int glucose_avg_45 = MyMath.getAverageFromArrayList(all45Values);
            int glucose_avg_60 = MyMath.getAverageFromArrayList(all60Values);
            int glucose_avg_75 = MyMath.getAverageFromArrayList(all75Values);
            int glucose_avg_90 = MyMath.getAverageFromArrayList(all90Values);
            int glucose_avg_105 = MyMath.getAverageFromArrayList(all105Values);
            int glucose_avg_120 = MyMath.getAverageFromArrayList(all120Values);

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
            set.setColor(getResources().getColor(R.color.colorPrimary));
            set.setLineWidth(2f);  // how fat is the line
            set.setValueTextSize(10f);
            set.setValueTextColor(getResources().getColor(R.color.colorPrimary));

            // Create
            mDataSets.add(set);

            LineData data = new LineData(mDataSets);
            mBinding.lineChart.setData(data);

            mBinding.lineChart.notifyDataSetChanged();
            mBinding.lineChart.invalidate();
          }
        });
  }
}

