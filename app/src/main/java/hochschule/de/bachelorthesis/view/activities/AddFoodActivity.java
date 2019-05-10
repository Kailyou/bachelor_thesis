package hochschule.de.bachelorthesis.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.ActivityAddFoodBinding;
import hochschule.de.bachelorthesis.lifecycle.ActivityAddFoodObserver;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.view_model.activities.AddFoodViewModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.Objects;

public class AddFoodActivity extends AppCompatActivity {

    private static final String TAG = "AddFoodActivity";

    ActivityAddFoodBinding mBinding;

    private AddFoodViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_food);

        getLifecycle().addObserver(new ActivityAddFoodObserver());

        // Modify action bar
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        setTitle("Add Food");

        // view model
        viewModel = ViewModelProviders.of(this).get(AddFoodViewModel.class);

        // Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_add_food_spinner_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mBinding.activityAddFoodSpinnerType.setAdapter(adapter);
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
        // check for empty strings in food name and brand name
        // by removing whitespaces from the begin and the and
        // if the result string is empty, the input was just filled by whitespaces
        if (mBinding.editFoodName.getText().toString().trim().isEmpty()
                || mBinding.editBrandName.getText().toString().trim().isEmpty()
                || mBinding.editEnergyKcal.getText().toString().trim().isEmpty()
                || mBinding.editEnergyKj.getText().toString().trim().isEmpty()
                || mBinding.editFat.getText().toString().trim().isEmpty()
                || mBinding.editSaturates.getText().toString().trim().isEmpty()
                || mBinding.editProtein.getText().toString().isEmpty()
                || mBinding.editCarbohydrates.getText().toString().trim().isEmpty()
                || mBinding.editSugar.getText().toString().trim().isEmpty()
                || mBinding.editSalt.getText().toString().trim().isEmpty()) {
            MyToast.createToast(this, "Please enter all values.");
            return;
        }

        String foodName = mBinding.editFoodName.getText().toString();
        String brandName = mBinding.editBrandName.getText().toString();

        // save data to database
        //Food newFood = new Food(foodName, brandName,"test");
        //viewModel.insert(newFood);
        MyToast.createToast(this, foodName + "added to the list..");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_food:
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
        menuInflater.inflate(R.menu.add_food_menu, menu);
        return true;    // displays the menu
    }
}
