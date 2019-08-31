package hochschule.de.bachelorthesis.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import hochschule.de.bachelorthesis.view.food.FoodDataFragment;
import hochschule.de.bachelorthesis.view.food.FoodOverviewFragment;
import hochschule.de.bachelorthesis.view.food.MeasurementListFragment;

/**
 * @author Maik Thielen
 * <p>
 * Adapter class for the food views.
 * <p>
 * Returns the needed Fragment depending on the tab item.
 */
public class FoodAdapter extends FragmentPagerAdapter {

    private int mFoodId;

    public FoodAdapter(FragmentManager fm, int foodId) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFoodId = foodId;
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
                MeasurementListFragment measurementListFragment = new MeasurementListFragment();
                measurementListFragment.setArguments(bundle);
                return measurementListFragment;

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

