package hochschule.de.bachelorthesis.view.fragments.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodOverviewBinding;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodOverviewObserver;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.view_model.activities.FoodInfoViewModel;

public class FoodOverviewFragment extends Fragment {

    private static final String TAG = FoodOverviewFragment.class.getName();

    private FragmentFoodOverviewBinding mBinding;

    private FoodInfoViewModel mViewModel;

    private int mFoodId;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // life cycle component
        getLifecycle().addObserver(new FragmentFoodOverviewObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get passed food id
        mFoodId = getArguments().getInt("food_id");

        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_overview, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        return mBinding.getRoot();
    }
}
