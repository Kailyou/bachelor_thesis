package hochschule.de.bachelorthesis.view.onboarding;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OnboardingAdapter extends FragmentPagerAdapter {

    public OnboardingAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OnboardingOneFragment();
            case 1:
                return new OnboardingTwoFragment();
            case 2:
                return new OnboardingThreeFragment();
            default:
                throw new IllegalStateException("Unexpected state at OnboardingAdapter");
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
