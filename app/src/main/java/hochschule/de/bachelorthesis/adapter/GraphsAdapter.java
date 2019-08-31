package hochschule.de.bachelorthesis.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import hochschule.de.bachelorthesis.view.graphs.GraphsFoodSingleFragment;
import hochschule.de.bachelorthesis.view.graphs.GraphsFoodsFragment;

/**
 * @author Maik Thielen
 * <p>
 * Adapter class for the graphs views.
 * <p>
 * Returns the needed Fragment depending on the tab item.
 */
public class GraphsAdapter extends FragmentPagerAdapter {

    public GraphsAdapter(FragmentManager fm) {
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
