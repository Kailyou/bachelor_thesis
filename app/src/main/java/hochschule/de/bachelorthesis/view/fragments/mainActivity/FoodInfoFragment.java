package hochschule.de.bachelorthesis.view.fragments.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.view.fragments.food.FoodDataFragment;
import hochschule.de.bachelorthesis.view.fragments.food.FoodMeasurementsFragment;
import hochschule.de.bachelorthesis.view.fragments.food.FoodOverviewFragment;
import hochschule.de.bachelorthesis.view_model.fragments.FoodInfoViewModel;

public class FoodInfoFragment extends Fragment {
    private FoodInfoViewModel viewModel;
    private FoodInfoFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private int mFoodId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("YOLOOOOOO");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_info, container, false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new FoodInfoFragment.SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        return view;
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
                    FoodMeasurementsFragment measurementsFragment = new FoodMeasurementsFragment();
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