package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import hochschule.de.bachelorthesis.MainActivity;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodHostBinding;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

/**
 * @author thielenm
 *
 * This class is the wrapper class for the tabs.
 *
 * Returns the correct fragment after the user swiped or clicked on one tab element.
 *
 * The navigation is not optiomal because currently NavigationComponent is not supporting Tabs.
 *
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
    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(
        getChildFragmentManager());

    // Set up the ViewPager with the sections adapter.
    ViewPager viewPager = binding.container;
    viewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = binding.tabs;

    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    return binding.getRoot();
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the
   * sections/tabs/pages.
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
