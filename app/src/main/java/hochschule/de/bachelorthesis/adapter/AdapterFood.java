package hochschule.de.bachelorthesis.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.loadFromDb.FoodObject;

/**
 * @author Maik Thielen
 * <p>
 * Adapter class for the food list.
 * <p>
 * Will take a list of food objects and create the needed text views out of it.
 */
public class AdapterFood extends RecyclerView.Adapter<AdapterFood.FoodHolder> {

    private Context mContext;

    private List<FoodObject> mFoodObjects = new ArrayList<>();

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
        final FoodObject currentFoodObject = mFoodObjects.get(position);

        // Position 0 as header
        if (position == 0) {
            holder.mFoodName.setText(mContext.getString(R.string.food));
            holder.mFoodName.setTextSize(10);
            holder.mFoodName.setTextColor(Color.BLACK);
            holder.mFoodName.setTypeface(null, Typeface.BOLD);

            holder.mBrandName.setVisibility(View.GONE);
            holder.mBrandName.setTextColor(Color.BLACK);
            holder.mBrandName.setTypeface(null, Typeface.BOLD);

            holder.mGi.setText(mContext.getString(R.string.gi));
            holder.mGi.setTextSize(10);
            holder.mGi.setTextColor(Color.BLACK);
            holder.mGi.setTypeface(null, Typeface.BOLD);

            return;
        }

        holder.mFoodName.setText(currentFoodObject.getFood().getFoodName());
        holder.mBrandName.setText(currentFoodObject.getFood().getBrandName());

        int gi = currentFoodObject.getGi();
        holder.mGi.setText(String.valueOf(gi));

        // Set text color depending of the GI result
        if (gi == 0) {
            holder.mGi.setTextColor(Color.BLACK);
        } else if (gi < 55)
            holder.mGi.setTextColor(mContext.getResources().getColor(R.color.gi_low));
        else if (gi < 71)
            holder.mGi.setTextColor(mContext.getResources().getColor(R.color.gi_mid));
        else
            holder.mGi.setTextColor(mContext.getResources().getColor(R.color.gi_high));


        // Click event for the card views, which will start a new activity (FoodInfoActivity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Navigate to food info fragment and pass the food's id
                Bundle bundle = new Bundle();
                bundle.putInt("food_id", currentFoodObject.getFood().id);
                bundle.putString("title", currentFoodObject.getFood().getFoodName());
                mNavController.navigate(R.id.action_foodFragment_to_foodInfoFragment, bundle);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return mFoodObjects.size();
    }

    public void setFoodObjects(List<FoodObject> foodObjects) {
        mFoodObjects = foodObjects;
        notifyDataSetChanged();
    }

    class FoodHolder extends RecyclerView.ViewHolder {

        private TextView mFoodName;
        private TextView mBrandName;
        private TextView mGi;

        private FoodHolder(@NonNull View itemView) {
            super(itemView);
            mFoodName = itemView.findViewById(R.id.food_item_food_name);
            mBrandName = itemView.findViewById(R.id.food_item_brand_name);
            mGi = itemView.findViewById(R.id.food_item_gi);
        }
    }
}
