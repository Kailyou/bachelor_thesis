package hochschule.de.bachelorthesis.utility;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import java.util.ArrayList;
import java.util.List;

public class AdapterMeasurements extends
    RecyclerView.Adapter<AdapterMeasurements.MeasurementHolder> {

  private NavController mNavController;

  private List<Measurement> mMeasurements = new ArrayList<>();


  public AdapterMeasurements(NavController navController) {
    mNavController = navController;
  }

  @NonNull
  @Override
  public MeasurementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_measurement, parent, false);

    return new MeasurementHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MeasurementHolder holder, final int position) {
    final Measurement currentMeasurement = mMeasurements.get(position);

    // Build date
    String ts = currentMeasurement.getTimeStamp();
    String date = String.copyValueOf(ts.toCharArray(), 0, 10);

    holder.amount.setText(String.valueOf(currentMeasurement.getAmount()));
    holder.date.setText(date);
    holder.mp.setText(String.valueOf(currentMeasurement.getGlucoseMax()));
    holder.avg.setText(String.valueOf(currentMeasurement.getGlucoseAverage()));
    holder.rating.setText("unrated");

    // Click event for the card views, which will start a new activity (FoodInfoActivity)
    final Bundle bundle = new Bundle();
    bundle.putInt("measurement_id", currentMeasurement.id);

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mNavController.navigate(R.id.action_foodInfoFragment_to_measurementFragment, bundle);
      }
    });
  }

  /**
   * Returns the total number of items in the data set held by the adapter.
   */
  @Override
  public int getItemCount() {
    return mMeasurements.size();
  }

  public void setMeasurements(List<Measurement> measurements) {
    this.mMeasurements = measurements;
    notifyDataSetChanged();
  }

  class MeasurementHolder extends RecyclerView.ViewHolder {

    private TextView amount;
    private TextView date;
    private TextView mp;
    private TextView avg;
    private TextView rating;

    private MeasurementHolder(@NonNull View itemView) {
      super(itemView);
      amount = itemView.findViewById(R.id.amount);
      date = itemView.findViewById(R.id.date);
      mp = itemView.findViewById(R.id.max_glucose);
      avg = itemView.findViewById(R.id.avg_glucose);
      rating = itemView.findViewById(R.id.rating);
    }
  }
}
