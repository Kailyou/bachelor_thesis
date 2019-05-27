package hochschule.de.bachelorthesis.utility;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.room.tables.Food;

public class AdapterFood extends RecyclerView.Adapter<AdapterFood.FoodHolder> {

    private Context mContext;
    private NavController mNavController;

    private List<Food> foods = new ArrayList<>();

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

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, final int position) {
        final Food currentFood = foods.get(position);
        holder.textViewFoodName.setText(currentFood.getFoodName());
        holder.textViewBrandName.setText(currentFood.getBrandName());
        holder.textViewMetaText.setText(currentFood.getFoodType());

        // Click event for the card views, which will start a new activity (FoodInfoActivity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Navigate to food info fragment and pass the food's id
                Bundle bundle = new Bundle();
                bundle.putInt("food_id", currentFood.id);
                mNavController.navigate(R.id.action_foodFragment_to_foodInfoFragment, bundle);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    class FoodHolder extends RecyclerView.ViewHolder {
        private TextView textViewFoodName;
        private TextView textViewBrandName;
        private TextView textViewMetaText;

        private FoodHolder(@NonNull View itemView) {
            super(itemView);
            textViewFoodName = itemView.findViewById(R.id.food_item_food_name);
            textViewBrandName = itemView.findViewById(R.id.food_item_brand_name);
            textViewMetaText = itemView.findViewById(R.id.food_item_meta_text);
        }
    }
}
