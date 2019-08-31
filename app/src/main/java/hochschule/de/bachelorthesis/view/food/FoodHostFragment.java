package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.adapter.FoodAdapter;
import hochschule.de.bachelorthesis.databinding.FragmentFoodHostBinding;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

/**
 * @author Maik Thielen
 * <p>
 * This class is the wrapper class for the tabs.
 * <p>
 * Returns the correct fragment after the user swiped or clicked on one tab element.
 * <p>
 * The navigation is not optiomal because currently NavigationComponent is not supporting Tabs.
 * <p>
 * In the future, the navigation here maybe could be outsourced to the navigation.xml file.
 */
public class FoodHostFragment extends Fragment {

    private FoodViewModel mViewModel;
    private int mFoodId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        mFoodId = getArguments().getInt("food_id");

        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(FoodViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Init data binding
        FragmentFoodHostBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_food_host, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setVm(mViewModel);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        FoodAdapter mFoodAdapter = new FoodAdapter(
                getChildFragmentManager(), mFoodId);

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = binding.container;
        viewPager.setAdapter(mFoodAdapter);

        TabLayout tabLayout = binding.tabs;

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        return binding.getRoot();
    }
}
