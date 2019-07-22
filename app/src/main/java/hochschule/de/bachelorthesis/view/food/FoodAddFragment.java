package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodAddBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.utility.Parser;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author thielenm
 *
 * The fragment for the View to create a new food.
 *
 * Entered data will be saved to a ViewModel, so as long as the APP does not be closed for any
 * reason, the data will be restored as soon as the View will be loaded again.
 *
 * The user can clear the data himself by pressing the clear button in the action bar.
 *
 * Finally, the user can save the food by pressing the save icon if every text field has been filled
 * out.
 */
public class FoodAddFragment extends Fragment {

  private FragmentFoodAddBinding mBinding;

  private FoodViewModel mViewModel;

  private ArrayList<FoodData> mFoodData = new ArrayList<>();


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable menu
    setHasOptionsMenu(true);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    parseCSV();

    // Init data binding
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_add, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

    // Take the strings out of the food data objects, parsed from the CSV file
    // and put them into a list, this will be passed to the adapter for the
    // Autocomplete text view.
    ArrayList<String> foodNames = new ArrayList<>();

    for (FoodData foodData : mFoodData) {
      foodNames.add(foodData.food);
    }

    ArrayAdapter<String> a = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
        android.R.layout.select_dialog_item, foodNames);
    mBinding.selectFood.setAdapter(a);

    // Dropdown
    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(Objects.requireNonNull(getContext()),
            R.array.type, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    mBinding.type.setAdapter(adapter);

    handleListeners();

    return mBinding.getRoot();
  }

  /**
   * Saving the current state to the viewModel.
   *
   * Uses a function to parse a Float value.
   *
   * If the float value is empty, the result will be a -1.
   *
   * The view will convert that -1 to an empty String.
   */
  @Override
  public void onStop() {
    super.onStop();

    // The normal String values can just be taken

    mViewModel.getFoodAddDataModel().getSelectedFood()
        .setValue(mBinding.selectFood.getText().toString());

    mViewModel.getFoodAddDataModel().getFoodName().setValue(
        Objects.requireNonNull(mBinding.foodName.getText()).toString());

    mViewModel.getFoodAddDataModel().getBrandName()
        .setValue(Objects.requireNonNull(mBinding.brandName.getText()).toString());

    mViewModel.getFoodAddDataModel().getType().setValue(mBinding.type.getText().toString());

    // The exposed drop down
    // get the needed string out of the string array resource and updateFood the vm.
    mViewModel.getFoodAddDataModel().getType().setValue(mBinding.type.getText().toString());

    // The float values have to be parsed first
    mViewModel.getFoodAddDataModel().getKiloCalories().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.kiloCalories.getText()).toString()));

    mViewModel.getFoodAddDataModel().getKiloJoules().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.kiloJoules.getText()).toString()));

    mViewModel.getFoodAddDataModel().getFat().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.fat.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSaturates().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.saturates.getText()).toString()));

    mViewModel.getFoodAddDataModel().getProtein().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.protein.getText()).toString()));

    mViewModel.getFoodAddDataModel().getCarbohydrates().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.carbohydrates.getText()).toString()));

    mViewModel.getFoodAddDataModel().getKiloCalories().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.kiloCalories.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSugars().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.sugars.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSalt().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.salt.getText()).toString()));
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.food_add_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {

      case R.id.save:
        if (isInputOkay()) {
          save();
        }
        return true;

      case R.id.clear:
        mViewModel.getFoodAddDataModel().setSelectedFood("");

        mViewModel.updateFoodAddModeL(new Food("", "", "",
            -1, -1, -1, -1, -1, -1, -1, -1));
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * parses the food database CSV file and creates a FoodData object and put it to the list.
   */
  private void parseCSV() {
    InputStream is = getResources().openRawResource(R.raw.fooddatabase);
    Reader r = new InputStreamReader(is);
    CsvReader csvReader = new CsvReader();
    csvReader.setContainsHeader(true);

    try (CsvParser csvParser = csvReader.parse(r)) {
      CsvRow row;
      while ((row = csvParser.nextRow()) != null) {

        // Build food data object and add it to the list
        String foodName = row.getField("food_name");
        String brandName = row.getField("brand_name");

        // Build food string: foodName (brandName)
        String food = foodName + " (" + brandName + ")";

        String type = row.getField("type");
        String energyKCal = row.getField("energy_kcal");
        String energyKj = row.getField("energy_kj");
        String fat = row.getField("fat");
        String saturates = row.getField("saturates");
        String protein = row.getField("protein");
        String carbohydrates = row.getField("carbohydrates");
        String sugar = row.getField("sugar");
        String salt = row.getField("salt");

        FoodData foodData = new FoodData(food, foodName, brandName, type, energyKCal, energyKj, fat,
            saturates, protein,
            carbohydrates, sugar, salt);

        mFoodData.add(foodData);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Helper function for all the listeners
   */
  private void handleListeners() {
    /* Load last input */

    // Food name dropdown list
    mViewModel.getFoodAddDataModel().getSelectedFood()
        .observe(getViewLifecycleOwner(), new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.selectFood
                .setText(s, false);
          }
        });

    // Type dropdown list
    mViewModel.getFoodAddDataModel().getType()
        .observe(getViewLifecycleOwner(), new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.type
                .setText(s, false);
          }
        });

    // Select food dropdown
    mBinding.selectFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view,
          int position, long id) {

        // Get the selected food by comparing the selected element's string with
        // all from the food data list. There can only be one match because of the
        // unique name. Update text views if match.
        for (int i = 0; i < mFoodData.size(); i++) {
          if (mFoodData.get(i).food
              .equals(parent.getItemAtPosition(position).toString())) {
            FoodData foodData = mFoodData.get(i);
            mBinding.foodName.setText(foodData.foodName);
            mBinding.brandName.setText(foodData.brandName);
            mBinding.type.setText(foodData.type);
            mBinding.kiloCalories.setText(foodData.kiloCalories);
            mBinding.kiloJoules.setText(foodData.kiloJoules);
            mBinding.fat.setText(foodData.fat);
            mBinding.saturates.setText(foodData.saturates);
            mBinding.protein.setText(foodData.protein);
            mBinding.carbohydrates.setText(foodData.carbohydrates);
            mBinding.sugars.setText(foodData.sugars);
            mBinding.salt.setText(foodData.salt);
          }
        }
      }
    });
  }

  /**
   * Checks if the user input has been valid.
   *
   * @return returns true if the input was okay. returns false otherwise.
   */
  private boolean isInputOkay() {
    // checks the text fields
    if (mBinding.foodName.getText() == null || mBinding.foodName.getText().toString()
        .equals("")) {
      toast("Please enter the food's name.");
      return false;
    }

    if (mBinding.brandName.getText() == null || mBinding.brandName.getText().toString()
        .equals("")) {
      toast("Please enter the food's brand name.");
      return false;
    }

    if (mBinding.type.getText() == null || mBinding.type.getText().toString().equals("")) {
      toast("Please enter the food's type.");
      return false;
    }

    // checks the drop down menus
    if (mBinding.kiloCalories.getText() == null || mBinding.kiloCalories.getText().toString()
        .equals("")) {
      toast("Please enter the kilo calories.");
      return false;
    }

    if (mBinding.kiloJoules.getText() == null || mBinding.kiloJoules.getText().toString()
        .equals("")) {
      toast("Please enter the kilo joules.");
      return false;
    }

    if (mBinding.fat.getText() == null || mBinding.fat.getText().toString().equals("")) {
      toast("Please enter the fat.");
      return false;
    }

    if (mBinding.saturates.getText() == null || mBinding.saturates.getText().toString()
        .equals("")) {
      toast("Please enter the saturates.");
      return false;
    }

    if (mBinding.protein.getText() == null || mBinding.protein.getText().toString().equals("")) {
      toast("Please enter the protein.");
      return false;
    }

    if (mBinding.carbohydrates.getText() == null || mBinding.carbohydrates.getText().toString()
        .equals("")) {
      toast("Please enter the carbohydrate.");
      return false;
    }

    if (mBinding.sugars.getText() == null || mBinding.sugars.getText().toString().equals("")) {
      toast("Please enter the sugars.");
      return false;
    }

    if (mBinding.salt.getText() == null || mBinding.salt.getText().toString().equals("")) {
      toast("Please enter the salt.");
      return false;
    }

    return true;
  }

  /**
   * Save the food to the database.
   */
  private void save() {
    boolean isFromDb = false;

    String foodName = Objects.requireNonNull(mBinding.foodName.getText()).toString();
    String brandName = Objects.requireNonNull(mBinding.brandName.getText()).toString();

    // Check if food is one of the data base. Build the foodName (brandName) string
    // and compare them with the entries of the food data list
    String food = foodName + " (" + brandName + ")";

    for (FoodData fd : mFoodData) {
      if (fd.food.equals(food)) {
        isFromDb = true;
      }
    }

    // If food was taken from DB add it, if not first create a confirmation dialog.
    if (isFromDb) {
      buildNewFoodAndUpdateDatabase();
    } else {
      new AlertDialog.Builder(Objects.requireNonNull(getContext()))
          .setTitle("Delete Confirmation")
          .setMessage(
              "It is suggest to choose a food from list, continue?")
          .setIcon(android.R.drawable.ic_delete)
          .setPositiveButton(android.R.string.yes, new OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
              if (whichButton == -1) {
                buildNewFoodAndUpdateDatabase();
              }
            }
          })
          .setNegativeButton(android.R.string.no, null).show();
    }
  }

  /**
   * Builds a food object with the Strings of the text views the user filled out. Then insert this
   * new food object to the database. Finally, a SnackBar will be printed.
   */
  private void buildNewFoodAndUpdateDatabase() {
    String foodName = Objects.requireNonNull(mBinding.foodName.getText()).toString();
    String brandName = Objects.requireNonNull(mBinding.brandName.getText()).toString();
    String type = mBinding.type.getText().toString();
    float kiloCalories = Float
        .parseFloat(Objects.requireNonNull(mBinding.kiloCalories.getText()).toString());
    float kiloJoules = Float
        .parseFloat(Objects.requireNonNull(mBinding.kiloJoules.getText()).toString());
    float fat = Float.parseFloat(Objects.requireNonNull(mBinding.fat.getText()).toString());
    float saturates = Float
        .parseFloat(Objects.requireNonNull(mBinding.saturates.getText()).toString());
    float protein = Float
        .parseFloat(Objects.requireNonNull(mBinding.protein.getText()).toString());
    float carbohydrate = Float
        .parseFloat(Objects.requireNonNull(mBinding.carbohydrates.getText()).toString());
    float sugars = Float.parseFloat(Objects.requireNonNull(mBinding.sugars.getText()).toString());
    float salt = Float.parseFloat(Objects.requireNonNull(mBinding.salt.getText()).toString());

    Food newFood = new Food(
        foodName,
        brandName,
        type,
        kiloCalories,
        kiloJoules,
        fat,
        saturates,
        protein,
        carbohydrate,
        sugars,
        salt);

    mViewModel.insertFood(newFood);

    MySnackBar
        .createSnackBar(getContext(),
            mBinding.foodName.getText().toString() + "added to the list..");
  }

  /**
   * Helper function for faster SnackBar creation
   *
   * @param msg The message to display in the SnackBar
   */
  private void toast(String msg) {
    MySnackBar.createSnackBar(getContext(), msg);
  }

  /**
   * Wrapper object for the food data.
   */
  private class FoodData {

    // General
    private String food;
    private String foodName;
    private String brandName;
    private String type;

    // Nutritional information
    private String kiloCalories;
    private String kiloJoules;
    private String fat;
    private String saturates;
    private String protein;
    private String carbohydrates;
    private String sugars;
    private String salt;

    private FoodData(String food, String foodName, String brandName, String type,
        String kiloCalories,
        String kiloJoules,
        String fat, String saturates, String protein, String carbohydrates, String sugars,
        String salt) {
      this.food = food;
      this.foodName = foodName;
      this.brandName = brandName;
      this.type = type;
      this.kiloCalories = kiloCalories;
      this.kiloJoules = kiloJoules;
      this.fat = fat;
      this.saturates = saturates;
      this.protein = protein;
      this.carbohydrates = carbohydrates;
      this.sugars = sugars;
      this.salt = salt;
    }
  }
}
