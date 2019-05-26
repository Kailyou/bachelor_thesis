package hochschule.de.bachelorthesis.view.fragments.food;

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
import androidx.navigation.Navigation;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodMeasurementsBinding;
import hochschule.de.bachelorthesis.view_model.fragments.FoodInfoViewModel;

public class FoodMeasurementsFragment extends Fragment {

    private static final String TAG = FoodMeasurementsFragment.class.getName();

    private FragmentFoodMeasurementsBinding mBinding;

    private FoodInfoViewModel mViewModel;

    private int mFoodId;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // view model
        mViewModel = ViewModelProviders.of(this).get(FoodInfoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_measurements, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);


        // fab
        mBinding.buttonAddNote.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_foodInfoFragment_to_foodAddMeasurementFragment2));

        // get passed food id
        assert getArguments() != null;
        //mFoodId = getArguments().getInt("food_id");

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e("YOLO", "inner nav: " + Navigation.findNavController(mBinding.buttonAddNote));
    }
}
