package hochschule.de.bachelorthesis.view.fragments.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.utility.AdapterFood;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodObserver;
import hochschule.de.bachelorthesis.room.Food;
import hochschule.de.bachelorthesis.view_model.fragments.FoodViewModel;

public class FoodFragment extends Fragment {

    private FloatingActionButton fab;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("My Food");
        super.onCreate(savedInstanceState);

        // Lifecycle
        getLifecycle().addObserver(new FragmentFoodObserver());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food, container, false);

        fab = rootView.findViewById(R.id.button_add_note);

        // RecyclerView
        RecyclerView rv = rootView.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);

        // Adapter
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment);
        final AdapterFood adapter = new AdapterFood(getContext(), navController);
        rv.setAdapter(adapter);

        // View model
        FoodViewModel foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        foodViewModel.getAllFood().observe(this, new Observer<List<Food>>() {
                    @Override
                    public void onChanged(List<Food> foods) {
                        adapter.setFoods(foods);
            }
        });

       return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_main_activity_food_fragment_to_addFoodActivity));
    }
}

