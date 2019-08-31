package hochschule.de.bachelorthesis.view.graphs;

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
import hochschule.de.bachelorthesis.adapter.GraphsAdapter;
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
        GraphsAdapter mSectionsPagerAdapter = new GraphsAdapter(
                getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = binding.container;
        viewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = binding.tabs;

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        return binding.getRoot();
    }
}