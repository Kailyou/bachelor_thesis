package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodInfoBinding;
import hochschule.de.bachelorthesis.viewmodels.FoodInfoViewModel;

public class FoodInfoFragment extends Fragment {
    private static final String TAG = FoodInfoFragment.class.getName();

    private FoodInfoViewModel mViewModel;
    private FoodInfoFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private int mFoodId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFoodId = getArguments().getInt("food_id");

        mViewModel = ViewModelProviders.of(getActivity()).get(FoodInfoViewModel.class);
        mViewModel.load(mFoodId, this);
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Init data binding
        FragmentFoodInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_info, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setVm(mViewModel);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new FoodInfoFragment.SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = binding.container;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = binding.tabs;

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        return binding.getRoot();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        @NonNull
        public Fragment getItem(int i) {

            Bundle bundle = new Bundle();
            bundle.putInt("food_id", mFoodId);

            switch (i) {
                case 0:
                    FoodOverviewFragment overviewFragment = new FoodOverviewFragment();
                    overviewFragment.setArguments(bundle);
                    return overviewFragment;

                case 1:
                    MeasurementsFragment measurementsFragment = new MeasurementsFragment();
                    measurementsFragment.setArguments(bundle);
                    return measurementsFragment;

                case 2:
                    FoodDataFragment dataFragment = new FoodDataFragment();
                    dataFragment.setArguments(bundle);
                    return dataFragment;

                default:
                    throw new IllegalArgumentException("Unexpected index @FoodInfoActivity");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
