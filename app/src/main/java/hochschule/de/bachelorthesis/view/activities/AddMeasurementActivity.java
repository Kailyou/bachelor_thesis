package hochschule.de.bachelorthesis.view.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.ActivityAddMeasurementBinding;
import hochschule.de.bachelorthesis.view_model.activities.AddMeasurementViewModel;

public class AddMeasurementActivity extends AppCompatActivity {

    private ActivityAddMeasurementBinding mBinding;

    private AddMeasurementViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_measurement);

        // Lifecycle component
        //getLifecycle().addObserver(new ActivityAddFoodObserver());

        // Modify action bar
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        setTitle("Add Measurement");

        // view model
        viewModel = ViewModelProviders.of(this).get(AddMeasurementViewModel.class);
    }

    /**
     * Takes the input from the EditTexts and saves them
     * if the input is valid.
     *
     * New food will be added to the database and the UI will be updated.
     *
     * Finally, a toast will give the user feedback if it worked or not.
     */
    private void saveFood() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_food) {
                saveFood();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Override this function to specify the options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.food_add_menu, menu);
        return true;    // displays the menu
    }
}
