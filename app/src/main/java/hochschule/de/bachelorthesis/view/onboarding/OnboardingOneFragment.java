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
import hochschule.de.bachelorthesis.databinding.FragmentOnboardingOneBinding;

/**
 * @author Maik Thielen
 * <p>
 * View class for the first onboarding fragment.
 */
public class OnboardingOneFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentOnboardingOneBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_onboarding_one, container, false);

        final ViewPager viewpager = Objects.requireNonNull(getActivity()).findViewById(R.id.onboarding_view_pager);

        // Next button
        binding.onboardingNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(1);
            }
        });

        return binding.getRoot();
    }
}
