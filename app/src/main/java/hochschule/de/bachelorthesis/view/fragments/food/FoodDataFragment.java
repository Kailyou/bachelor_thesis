package hochschule.de.bachelorthesis.view.fragments.food;

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

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodDataBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.view_model.activities.FoodInfoViewModel;

public class FoodDataFragment extends Fragment {

    private static final String TAG = FoodDataFragment.class.getName();

    private FoodInfoViewModel mViewModel;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // view model
        mViewModel = ViewModelProviders.of(this).get(FoodInfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        FragmentFoodDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_data, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(mViewModel);

        // get passed food id
        assert getArguments() != null;
        int foodId = getArguments().getInt("food_id");

        // Observe
        final LiveData<Food> food = mViewModel.getFoodById(foodId);
        food.observe(this, new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                //updateViewModel(food);
            }
        });

        return binding.getRoot();
    }

    private void updateViewModel(Food food) {
        mViewModel.setEnergyKcal(food.getKiloCalories());
        mViewModel.setEnergyKJ(food.getKiloJoules());
        mViewModel.setFat(food.getFat());
        mViewModel.setSaturates(food.getSaturates());
        mViewModel.setProtein(food.getProtein());
        mViewModel.setCarbohydrates(food.getCarbohydrates());
        mViewModel.setSugar(food.getSugar());
        mViewModel.setsSalt(food.getSalt());
    }
}
