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
import androidx.recyclerview.widget.RecyclerView;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.activities.FoodActivity;
import hochschule.de.bachelorthesis.room.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodHolder> {

    private Context context;

    private List<Food> foods = new ArrayList<>();

    public FoodAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_item_cardview, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, FoodActivity.class);
               context.startActivity(intent);
            }
        });

        return new FoodHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        Food currentFood = foods.get(position);
        holder.mainText.setText(currentFood.getMainText());
        holder.subText.setText(currentFood.getSubText());
        holder.metaText.setText(currentFood.getMetaText());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
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
        private TextView mainText;
        private TextView subText;
        private TextView metaText;

        private FoodHolder(@NonNull View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.food_item_maintext);
            subText = itemView.findViewById(R.id.food_item_subtext);
            metaText = itemView.findViewById(R.id.food_item_metatext);
        }
    }
}
