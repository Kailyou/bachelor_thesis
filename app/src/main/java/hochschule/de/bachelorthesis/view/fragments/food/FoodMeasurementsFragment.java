package hochschule.de.bachelorthesis.view.fragments.food;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodMeasurementsBinding;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodMeasurmentsObserver;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.view_model.activities.FoodInfoViewModel;

public class FoodMeasurementsFragment extends Fragment {

    private static final String TAG = FoodMeasurementsFragment.class.getName();

    private FragmentFoodMeasurementsBinding mBinding;

    private FoodInfoViewModel mViewModel;

    private int mFoodId;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // life cycle component
        getLifecycle().addObserver(new FragmentFoodMeasurmentsObserver());

        // view model
        mViewModel = ViewModelProviders.of(this).get(FoodInfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get passed food id
        mFoodId = getArguments().getInt("food_id");

        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_measurements, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        // fab
        FloatingActionButton fab = mBinding.getRoot().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Add button pressed");
            }
        });

        return mBinding.getRoot();
    }
}
