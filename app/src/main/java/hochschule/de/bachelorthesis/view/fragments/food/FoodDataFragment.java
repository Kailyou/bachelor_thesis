package hochschule.de.bachelorthesis.view.fragments.food;

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
import hochschule.de.bachelorthesis.databinding.FragmentFoodDataBinding;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodDataObserver;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.view_model.activities.FoodInfoViewModel;

public class FoodDataFragment extends Fragment {

    private static final String TAG = FoodDataFragment.class.getName();

    private FragmentFoodDataBinding mBinding;

    private FoodInfoViewModel mViewModel;

    private int mFoodId;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // life cycle component
        getLifecycle().addObserver(new FragmentFoodDataObserver());

        // view model
        mViewModel = ViewModelProviders.of(this).get(FoodInfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get passed food id
        mFoodId = getArguments().getInt("food_id");

        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_data, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        return mBinding.getRoot();
    }
}
