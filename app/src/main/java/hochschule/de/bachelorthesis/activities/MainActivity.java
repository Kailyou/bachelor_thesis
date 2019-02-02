package hochschule.de.bachelorthesis.activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.fragments_main_activity.HomeFragment;
import hochschule.de.bachelorthesis.fragments_main_activity.MyFoodFragment;
import hochschule.de.bachelorthesis.fragments_main_activity.PlanFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // Show the first TopLevelFragment by default.
        showTopLevelFragment(new HomeFragment());

        Log.d(MainActivity.TAG, "hi from MainActivity");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Helper function to load a new fragment.
     * @param fragment - The fragment to show.
     */
    private void showTopLevelFragment(Fragment fragment) {
        // Use the fragment manager to dynamically change the fragment displayed in the FrameLayout.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Action listener for the bottom navigation bar.
     * Loading different fragments depending on the clicked btm bar element.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f;

        switch (item.getItemId()){
            case R.id.navigation_home:
                f = new HomeFragment();
                break;
            case R.id.navigation_my_food:
                f = new MyFoodFragment();
                break;
            case R.id.navigation_plan:
                f = new PlanFragment();
                break;
            default:
                return false;
        }

        showTopLevelFragment(f);
        return true;
    }

    /**
     * Will be used to change the title when a new fragment is loaded.
     */
    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}