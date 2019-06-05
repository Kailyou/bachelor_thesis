package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import hochschule.de.bachelorthesis.widget.BetterFloatingActionButton;

public class FoodFragment extends Fragment {

  private static final String TAG = FoodFragment.class.getName();

  private BetterFloatingActionButton mFab;

  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable menu
    setHasOptionsMenu(true);
  }

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Init data binding
    FragmentFoodBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_food, container, false);
    binding.setLifecycleOwner(getViewLifecycleOwner());

    // View model
    FoodViewModel viewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

    mFab = binding.buttonAddNote;

    // RecyclerView
    RecyclerView recyclerView = binding.recyclerView;
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setHasFixedSize(true);

    // Adapter
    NavController navController = Navigation
        .findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment_host);
    final AdapterFood adapter = new AdapterFood(navController);
    recyclerView.setAdapter(adapter);

    viewModel.getAllFoods().observe(this, new Observer<List<Food>>() {
      @Override
      public void onChanged(List<Food> foods) {
        adapter.setFoods(foods);
      }
    });

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mFab.setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.action_foodFragment_to_addFood));
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

