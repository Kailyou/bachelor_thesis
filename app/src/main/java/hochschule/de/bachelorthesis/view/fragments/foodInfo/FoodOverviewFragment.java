package hochschule.de.bachelorthesis.view.fragments.foodInfo;

import android.os.Bundle;
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
import hochschule.de.bachelorthesis.view_model.fragments.FoodInfoViewModel;


public class FoodOverviewFragment extends Fragment {
    private static final String TAG = FoodOverviewFragment.class.getName();

    private FragmentFoodOverviewBinding mBinding;

    private FoodInfoViewModel mViewModel;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // view model
        mViewModel = ViewModelProviders.of(getActivity()).get(FoodInfoViewModel.class);
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

        return mBinding.getRoot();
    }
}
