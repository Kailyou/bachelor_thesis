package hochschule.de.bachelorthesis.view.fragments.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodBinding;
import hochschule.de.bachelorthesis.utility.AdapterFood;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.view_model.fragments.FoodViewModel;
import hochschule.de.bachelorthesis.widget.BetterFloatingActionButton;

public class FoodFragment extends Fragment {

    private BetterFloatingActionButton fab;
    private FragmentFoodBinding mBinding;
    private RecyclerView mRecyclverView;
    private AdapterFood adapter;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable menu
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        //mBinding.setViewModel(mViewModel);

        fab = mBinding.buttonAddNote;

        // RecyclerView
        mRecyclverView = mBinding.recyclerView;
        mRecyclverView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclverView.setHasFixedSize(true);

        // Adapter
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment);
        final AdapterFood adapter = new AdapterFood(getContext(), navController);
        mRecyclverView.setAdapter(adapter);

        // View model
        FoodViewModel foodViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        foodViewModel.getAllFood().observe(this, new Observer<List<Food>>() {
                    @Override
                    public void onChanged(List<Food> foods) {
                        adapter.setFoods(foods);
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e("YOLO", "outer nav: " + Navigation.findNavController(view));
        fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_main_activity_food_fragment_to_foodAddFragment));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.food_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

