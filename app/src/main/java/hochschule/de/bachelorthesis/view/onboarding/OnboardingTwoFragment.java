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
import hochschule.de.bachelorthesis.databinding.FragmentOnboardingTwoBinding;

/**
 * @author Maik Thielen
 * <p>
 * View class for the second onboarding fragment.
 */
public class OnboardingTwoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentOnboardingTwoBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_onboarding_two, container, false);

        final ViewPager viewpager = Objects.requireNonNull(getActivity()).findViewById(R.id.onboarding_view_pager);

        // Back button
        binding.onboardingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(0);
            }
        });

        // Next button
        binding.onboardingNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(2);
            }
        });

        return binding.getRoot();
    }
}
