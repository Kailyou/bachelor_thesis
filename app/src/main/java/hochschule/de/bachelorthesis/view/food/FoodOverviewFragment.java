package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodOverviewBinding;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;


public class FoodOverviewFragment extends Fragment {
    private static final String TAG = FoodOverviewFragment.class.getName();

    private FragmentFoodOverviewBinding mBinding;

    private FoodViewModel mViewModel;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // view model
        mViewModel = ViewModelProviders.of(getActivity()).get(FoodViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_overview, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setVm(mViewModel);

        // get passed food id
        assert getArguments() != null;
        int foodId = getArguments().getInt("food_id");

        Log.d("yolo", "food name " + mBinding.foodName.getText().toString());

        return mBinding.getRoot();
    }
}
