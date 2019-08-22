package hochschule.de.bachelorthesis.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;

import java.util.ArrayList;
import java.util.List;

public class AdapterMeasurements extends
        RecyclerView.Adapter<AdapterMeasurements.MeasurementHolder> {

    private Context mContext;

    private List<Measurement> mRefMeasurements;

    private NavController mNavController;

    private List<Measurement> mMeasurements = new ArrayList<>();


    public AdapterMeasurements(Context context, NavController navController, List<Measurement> refMeasurements) {
        mContext = context;
        mRefMeasurements = refMeasurements;
        mNavController = navController;
    }

    @NonNull
    @Override
    public MeasurementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_measurement, parent, false);

        return new MeasurementHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MeasurementHolder holder, final int position) {
        final Measurement currentMeasurement = mMeasurements.get(position);

        // Position 0 as header
        if (position == 0) {
            holder.date.setText("Date");
            holder.date.setTextSize(10);

            holder.isGi.setText("Is GI?");
            holder.isGi.setTextSize(10);

            holder.amount.setText("Amount");
            holder.amount.setTextSize(10);

            holder.mp.setText("Glucose max");
            holder.mp.setTextSize(10);

            holder.gi.setText("GI");
            holder.gi.setTextSize(10);

            return;
        }

        // Build date
        String ts = currentMeasurement.getTimeStamp();
        String date = String.copyValueOf(ts.toCharArray(), 0, 10);

        holder.date.setText(date);
        holder.isGi.setText(Converter.convertBoolean(mContext, currentMeasurement.isGi()));
        holder.amount.setText(String.valueOf(currentMeasurement.getAmount()));
        holder.mp.setText(String.valueOf(currentMeasurement.getGlucoseMax()));

        // Either calculate the GI or set the text as N/A
        if (currentMeasurement.isGi())
            holder.gi.setText(String.valueOf((int) currentMeasurement.getGi(mRefMeasurements)));
        else
            holder.gi.setText("N/A");

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

        private TextView date;
        private TextView isGi;
        private TextView amount;
        private TextView mp;
        private TextView gi;

        private MeasurementHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            isGi = itemView.findViewById(R.id.isGi);
            amount = itemView.findViewById(R.id.amount);
            mp = itemView.findViewById(R.id.max_glucose);
            gi = itemView.findViewById(R.id.gi);
        }
    }
}
