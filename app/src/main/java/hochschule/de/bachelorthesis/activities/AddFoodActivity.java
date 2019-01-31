package hochschule.de.bachelorthesis.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.room.Food;
import hochschule.de.bachelorthesis.view_model.AddFoodViewModel;
import hochschule.de.bachelorthesis.view_model.FoodViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddFoodActivity extends AppCompatActivity {
    private AddFoodViewModel viewModel;

    private EditText editTextFoodName;
    private EditText editTextBrandName;
    private EditText editTextEnergyKcal;
    private EditText editTextEnergyKJ;
    private EditText editTextFat;
    private EditText editTextSaturates;
    private EditText editTextProtein;
    private EditText editTextCarbohydrates;
    private EditText editTextSugar;
    private EditText editTextSalt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_activity);

        editTextFoodName = findViewById(R.id.edit_food_name);
        editTextBrandName = findViewById(R.id.edit_brand_name);
        editTextEnergyKcal = findViewById(R.id.edit_energy_kcal);
        editTextEnergyKJ = findViewById(R.id.edit_energy_kj);
        editTextFat = findViewById(R.id.edit_fat);
        editTextSaturates = findViewById(R.id.edit_saturates);
        editTextProtein = findViewById(R.id.edit_protein);
        editTextCarbohydrates = findViewById(R.id.edit_carbohydrates);
        editTextSugar = findViewById(R.id.edit_sugar);
        editTextSalt = findViewById(R.id.edit_salt);

        // Modify action bar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        setTitle("Add Food");

        // view model
        viewModel = ViewModelProviders.of(this).get(AddFoodViewModel.class);
    }

    private void saveFood() {
        String foodName = editTextFoodName.getText().toString();
        String brandName = editTextBrandName.getText().toString();
        String stringEnergyKcal = editTextEnergyKcal.getText().toString();
        String stringEnergyKJ = editTextEnergyKJ.getText().toString();
        String stringFat = editTextFat.getText().toString();
        String stringSaturates = editTextSaturates.getText().toString();
        String stringProtein = editTextProtein.getText().toString();
        String stringCarbohydrates = editTextCarbohydrates.getText().toString();
        String stringSugar = editTextSugar.getText().toString();
        String stringSalt = editTextSalt.getText().toString();

        // check for empty strings in food name and brand name
        // by removing whitespaces from the begin and the and
        // if the result string is empty, the input was just filled by whitespaces
        if (foodName.trim().isEmpty()
                || brandName.trim().isEmpty()
                || stringEnergyKcal.trim().isEmpty()
                || stringEnergyKJ.trim().isEmpty()
                || stringFat.trim().isEmpty()
                || stringSaturates.trim().isEmpty()
                || stringProtein.trim().isEmpty()
                || stringCarbohydrates.trim().isEmpty()
                || stringSugar.trim().isEmpty()
                || stringSalt.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a food and a brand name.", Toast.LENGTH_LONG).show();
            return;
        }

        // save data to databse
        Food newFood = new Food(foodName, brandName, "test");
        viewModel.insert(newFood);
        Toast.makeText(this, foodName + " added to the list.", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_food_menu, menu);
        return true;    // displays the menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_food:
                saveFood();
                return true;
            // get intent of parent activity and bring it to front if it exists
            // see https://stackoverflow.com/questions/30059474/android-navigation-up-from-activity-to-fragment/30059708#30059708
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            super.onBackPressed(); //replaced
        }
    }
}