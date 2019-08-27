package hochschule.de.bachelorthesis.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;

import java.util.ArrayList;
import java.util.List;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.FoodHolder> {

    private Context mContext;

    private List<Food> mFoods = new ArrayList<>();

    private List<Measurement> mMeasurements = new ArrayList<>();

    private List<Measurement> mRefMeasurements = new ArrayList<>();

    private NavController mNavController;


    public AdapterFood(Context context, NavController navController) {
        mContext = context;
        mNavController = navController;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);

        return new FoodHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, final int position) {
        final Food currentFood = mFoods.get(position);

        // Position 0 as header
        if (position == 0) {
            holder.textViewFoodName.setText(mContext.getString(R.string.food));
            holder.textViewFoodName.setTextSize(10);
            holder.textViewFoodName.setGravity(Gravity.CENTER);

            holder.textViewBrandName.setVisibility(View.GONE);
            holder.textViewFoodName.setGravity(Gravity.CENTER);

            holder.textViewGi.setText(mContext.getString(R.string.gi));
            holder.textViewGi.setTextSize(10);
            holder.textViewGi.setGravity(Gravity.CENTER);

            return;
        }

        holder.textViewFoodName.setText(currentFood.getFoodName());
        holder.textViewBrandName.setText(currentFood.getBrandName());
        holder.textViewGi.setText("N/A");

        // Click event for the card views, which will start a new activity (FoodInfoActivity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Navigate to food info fragment and pass the food's id
                Bundle bundle = new Bundle();
                bundle.putInt("food_id", currentFood.id);
                bundle.putString("title", currentFood.getFoodName());
                mNavController.navigate(R.id.action_foodFragment_to_foodInfoFragment, bundle);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public void setFoods(List<Food> mFoods) {
        this.mFoods = mFoods;
        notifyDataSetChanged();
    }

    public void setMeasurements(List<Measurement> measurements) {
        mMeasurements = measurements;
        notifyDataSetChanged();
    }

    public void setRefMeasurements(List<Measurement> measurements) {
        mRefMeasurements = measurements;
        notifyDataSetChanged();
    }

    class FoodHolder extends RecyclerView.ViewHolder {

        private TextView textViewFoodName;
        private TextView textViewBrandName;
        private TextView textViewGi;

        private FoodHolder(@NonNull View itemView) {
            super(itemView);
            textViewFoodName = itemView.findViewById(R.id.food_item_food_name);
            textViewBrandName = itemView.findViewById(R.id.food_item_brand_name);
            textViewGi = itemView.findViewById(R.id.food_item_gi);
        }
    }
}
