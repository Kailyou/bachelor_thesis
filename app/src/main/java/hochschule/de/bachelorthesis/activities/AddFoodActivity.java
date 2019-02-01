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
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class AddFoodActivity extends AppCompatActivity {
    private static final String TAG = "AddFoodActivity";

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
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        setTitle("Add Food");

        // view model
        viewModel = ViewModelProviders.of(this).get(AddFoodViewModel.class);

        // Spinner
        Spinner spinner = (Spinner) findViewById(R.id.activity_add_food_spinner_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_add_food_spinner_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Log.d(TAG,"onCreate: started");
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

        // save data to database
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
                assert parentIntent != null;
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
