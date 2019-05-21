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

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodOverviewBinding;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodOverviewObserver;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.view_model.activities.FoodInfoViewModel;

public class FoodOverviewFragment extends Fragment {

    private static final String TAG = FoodOverviewFragment.class.getName();

    private FoodInfoViewModel mViewModel;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // life cycle component
        getLifecycle().addObserver(new FragmentFoodOverviewObserver());

        // view model
        mViewModel = ViewModelProviders.of(this).get(FoodInfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        FragmentFoodOverviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_overview, container, false);
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
                updateViewModel(food);
                Objects.requireNonNull(getActivity()).setTitle(food.getFoodName());
            }
        });

        return binding.getRoot();
    }

    private void updateViewModel(Food food) {
        // general
        mViewModel.setFoodName(food.getFoodName());
        mViewModel.setBrandName(food.getBrandName());
        mViewModel.setType(food.getFoodType());
        mViewModel.setEnergyKcal(food.getKiloCalories());

        // measurements
        mViewModel.setMeasurementsAmount(food.getMeasurementsDone());
        mViewModel.setMaxGlucose(food.getMaxGlucose());
        mViewModel.setAverageGlucose(food.getAverageGlucose());

        // Analyses
        mViewModel.setRating(food.getRating());
        mViewModel.setPersonalIndex(food.getPersonalIndex());
    }
}
