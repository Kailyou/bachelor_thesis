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
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.utility.Samples;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.util.Objects;

/**
 * @author thielenm
 *
 * This class simply displays the data of the food.
 *
 * Once the class is created, there will be the before by the user chosen food object loaded and
 * with that food object, the texts of the textViews will be set.
 */
public class FoodDataFragment extends Fragment {

  private FoodViewModel mViewModel;

  private FragmentFoodDataBinding mBinding;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // view model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_food_data, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());

    // Get passed food id
    assert getArguments() != null;
    int foodId = getArguments().getInt("food_id");

    // Fab
    Bundle bundle = new Bundle();
    bundle.putInt("food_id", foodId);

    mBinding.fab.setOnClickListener(
        Navigation
            .createNavigateOnClickListener(R.id.action_foodInfoFragment_to_editFoodDataFragment2,
                bundle));

    // Load The food object
    mViewModel.getFoodById(foodId).observe(getViewLifecycleOwner(), new Observer<Food>() {
      @Override
      public void onChanged(Food food) {
        /* Update text views */

        // General
        mBinding.foodName.setText(food.getFoodName());
        mBinding.brandName.setText(food.getBrandName());
        mBinding.type.setText(food.getFoodType());

        // Nutritional information
        mBinding.kiloCalories.setText(Converter.convertFloat(food.getKiloCalories()));
        mBinding.kiloJoules.setText(Converter.convertFloat(food.getKiloJoules()));
        mBinding.fat.setText(Converter.convertFloat(food.getFat()));
        mBinding.saturates.setText(Converter.convertFloat(food.getSaturates()));
        mBinding.protein.setText(Converter.convertFloat(food.getProtein()));
        mBinding.carbohydrates.setText(Converter.convertFloat(food.getCarbohydrate()));
        mBinding.sugar.setText(Converter.convertFloat(food.getSugars()));
        mBinding.salt.setText(Converter.convertFloat(food.getSalt()));
      }
    });

    return mBinding.getRoot();
  }
}
