package hochschule.de.bachelorthesis.view.graphs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentGraphsHostBinding;
import hochschule.de.bachelorthesis.viewmodels.GraphsViewModel;

public class GraphsHostFragment extends Fragment {

  private GraphsViewModel mViewModel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(GraphsViewModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Init data binding
    FragmentGraphsHostBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_graphs_host, container, false);
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

      switch (i) {
        case 0:
          return new GraphsFoodSingleFragment();

        case 1:
          return new GraphsFoodsFragment();

        default:
          throw new IllegalArgumentException("Unexpected index @FoodInfoActivity");
      }
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}
