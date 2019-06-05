package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import androidx.navigation.Navigation;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodDataBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.utility.Samples;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.Objects;

public class FoodDataFragment extends Fragment {

  private static final String TAG = FoodDataFragment.class.getName();

  private FoodViewModel mViewModel;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // view model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(FoodViewModel.class);

    mViewModel.loadDataFragment(Samples.getEmptyFood());
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    FragmentFoodDataBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_food_data, container, false);
    binding.setLifecycleOwner(getViewLifecycleOwner());
    binding.setVm(mViewModel);

    // Get passed food id
    assert getArguments() != null;
    int foodId = getArguments().getInt("food_id");

    // Fab
    Bundle bundle = new Bundle();
    bundle.putInt("food_id", foodId);

    binding.fab.setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.action_foodInfoFragment_to_editFoodDataFragment2,
            bundle));

    mViewModel.getFoodById(foodId).observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(Food food) {
        mViewModel.loadDataFragment(food);
      }
    });

    return binding.getRoot();
  }
}
