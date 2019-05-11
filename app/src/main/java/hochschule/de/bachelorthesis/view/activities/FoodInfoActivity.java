package hochschule.de.bachelorthesis.view.activities;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.view.fragments.food.FoodDataFragment;
import hochschule.de.bachelorthesis.view.fragments.food.FoodMeasurementsFragment;
import hochschule.de.bachelorthesis.view.fragments.food.FoodOverviewFragment;
import hochschule.de.bachelorthesis.lifecycle.ActivityFoodObserver;
import hochschule.de.bachelorthesis.view_model.activities.FoodInfoViewModel;

public class FoodInfoActivity extends AppCompatActivity {

    private static final String TAG = FoodInfoActivity.class.getName();

    private FoodInfoViewModel viewModel;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private int mFoodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FoodInfoActivityArgs args = FoodInfoActivityArgs.fromBundle(Objects.requireNonNull(getIntent().getExtras()));
        mFoodId = args.getFoodId();
        Log.e("TAG", "" + mFoodId);

        setContentView(R.layout.activity_food);

        // life cycle
        getLifecycle().addObserver(new ActivityFoodObserver());

        // view model
        viewModel = ViewModelProviders.of(this).get(FoodInfoViewModel.class);

        // Enable back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                 return true;
            case R.id.menu_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
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
                    FoodMeasurementsFragment measurementsFragment = new FoodMeasurementsFragment();
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