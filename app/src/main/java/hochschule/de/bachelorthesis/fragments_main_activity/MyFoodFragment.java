package hochschule.de.bachelorthesis.fragments_main_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.activities.AddFoodActivity;
import hochschule.de.bachelorthesis.adapter.FoodAdapter;
import hochschule.de.bachelorthesis.room.Food;
import hochschule.de.bachelorthesis.view_model.FoodViewModel;

public class MyFoodFragment extends Fragment implements View.OnClickListener {

    private FoodViewModel foodViewModel;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_food, container, false);

        // Add food button
        FloatingActionButton buttonAddFood = rootView.findViewById(R.id.button_add_note);
        buttonAddFood.setOnClickListener(this);

        // RecyclerView
        RecyclerView rv = rootView.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);

        // Adapter
        final FoodAdapter adapter = new FoodAdapter(getContext());
        rv.setAdapter(adapter);

        foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        foodViewModel.getAllFood().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                adapter.setFoods(foods);
            }
        });

       return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), AddFoodActivity.class);
        startActivity(intent);
    }
}

