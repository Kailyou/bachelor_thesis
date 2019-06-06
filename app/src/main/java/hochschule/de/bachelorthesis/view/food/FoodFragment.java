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

import androidx.lifecycle.LiveData;
import hochschule.de.bachelorthesis.utility.Samples;
import java.util.Collections;
import java.util.Comparator;
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

  private FoodViewModel mViewModel;

  private BetterFloatingActionButton mFab;

  private AdapterFood mAdapter;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);

    // Enable menu
    setHasOptionsMenu(true);
  }

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Init data binding
    FragmentFoodBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_food, container, false);
    binding.setLifecycleOwner(getViewLifecycleOwner());

    mFab = binding.buttonAddNote;

    // Adapter
    NavController mNavcontroller = Navigation
        .findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment_host);

    mAdapter = new AdapterFood(mNavcontroller);

    // RecyclerView
    RecyclerView recyclerView = binding.recyclerView;
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(mAdapter);

    mViewModel.getAllFoods().observe(this, new Observer<List<Food>>() {
      @Override
      public void onChanged(List<Food> foods) {
        sortFoodList("alphanumeric", mAdapter, foods);
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
    switch (item.getItemId()) {
      case R.id.sort:
        sort();
        return true;

      case R.id.add_apple:
        mViewModel.insertFood(Samples.getApple());
        return true;

      case R.id.add_coke:
        mViewModel.insertFood(Samples.getCoke());
        return true;

      case R.id.add_pizza:
        mViewModel.insertFood(Samples.getPizza());
        return true;

      case R.id.delete_foods:
        mViewModel.deleteAllFoods();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Will sort the list alphanumeric
   */
  private void sort() {
    final LiveData<List<Food>> ldf = mViewModel.getAllFoods();
    ldf.observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
      @Override
      public void onChanged(List<Food> foods) {
        ldf.removeObserver(this);
        sortFoodList("alphanumeric", mAdapter, foods);
      }
    });
  }

  private void sortFoodList(String sortType, AdapterFood adapter, List<Food> list) {

    Comparator<Food> comparator = null;

    switch (sortType) {
      case "alphanumeric":
        comparator = new Comparator<Food>() {
          @Override
          public int compare(Food food1, Food food2) {
            return String.CASE_INSENSITIVE_ORDER.compare(food1.getFoodName(), food2.getFoodName());
          }
        };
        break;
    }

    if (comparator != null) {
      Collections.sort(list, comparator);
      mAdapter.setFoods(list);
    }
  }
}

