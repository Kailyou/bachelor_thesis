package hochschule.de.bachelorthesis.view.onboarding;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.adapter.OnboardingAdapter;
import hochschule.de.bachelorthesis.databinding.FragmentOnboardingHostBinding;

/**
 * The Host Fragment for the onboarding views.
 */
public class OnboardingHostFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Init data binding
        FragmentOnboardingHostBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_onboarding_host, container, false);

        // view pager
        ViewPager mViewPager = binding.onboardingViewPager;
        OnboardingAdapter adapter = new OnboardingAdapter(getChildFragmentManager(), 2); // ?
        mViewPager.setAdapter(adapter);

        // Hide bottom nav
        Objects.requireNonNull(getActivity()).findViewById(R.id.main_activity_bottom_nav).setVisibility(View.GONE);

        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(getActivity()).findViewById(R.id.main_activity_bottom_nav).setVisibility(View.VISIBLE);
    }
}
