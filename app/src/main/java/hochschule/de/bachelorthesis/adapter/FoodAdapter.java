package hochschule.de.bachelorthesis.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.activities.FoodActivity;
import hochschule.de.bachelorthesis.room.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder> {

    private Context context;
    private NavController navController;

    private List<Food> foods = new ArrayList<>();

    public FoodAdapter(Context context, NavController navController) {
        this.context = context;
        this.navController = navController;
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
        Food currentFood = foods.get(position);
        holder.textViewFoodName.setText(currentFood.getFoodName());
        holder.textViewBrandName.setText(currentFood.getBrandName());
        holder.textViewMetaText.setText(currentFood.getMetaText());

        // Click event for the card views, which will start a new activity (FoodActivity)
        // navController.navigate(R.id.action_main_activity_food_fragment_to_FoodActivity);

        // Trying to pass the primaryKey
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodActivity.class);
                intent.putExtra("primary_key", foods.get(position).getId());
                intent.putExtra("food_name", foods.get(position).getFoodName());
                context.startActivity(intent);
            }
        });
        */

        holder.itemView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_main_activity_food_fragment_to_FoodActivity));
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
