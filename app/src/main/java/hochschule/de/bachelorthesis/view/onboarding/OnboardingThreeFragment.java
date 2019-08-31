package hochschule.de.bachelorthesis.view.onboarding;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentOnboardingThreeBinding;

/**
 * @author Maik Thielen
 * <p>
 * View class for the third onboarding fragment.
 */
public class OnboardingThreeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentOnboardingThreeBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_onboarding_three, container, false);

        final ViewPager viewpager = Objects.requireNonNull(getActivity()).findViewById(R.id.onboarding_view_pager);

        // Back button
        binding.onboardingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(1);
            }
        });

        // Next button
        binding.onboardingNext.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_onboardingHostFragment_to_homeFragment));

        return binding.getRoot();
    }
}
