package hochschule.de.bachelorthesis.view.food;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementAddBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.utility.Parser;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MeasurementAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

  private FragmentMeasurementAddBinding mBinding;

  private FoodViewModel mViewModel;

  private int mFoodId;

  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable menu
    setHasOptionsMenu(true);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);

    // Get passed food id
    assert getArguments() != null;
    mFoodId = getArguments().getInt("food_id");
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    // Init data binding
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_measurement_add, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

    // Drop downs
    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(Objects.requireNonNull(getContext()),
            R.array.stress, android.R.layout.simple_spinner_item);

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mBinding.stress.setAdapter(adapter);

    adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
        R.array.tired, android.R.layout.simple_spinner_item);
    mBinding.tired.setAdapter(adapter);

    // Build from string
    mViewModel.getMeasurementAddModel().getTimestamp().observe(getViewLifecycleOwner(),
        new Observer<String>() {
          @Override
          public void onChanged(String s) {
          }
        });

    // load the last user input by observing the view model object
    // filter has to be false otherwise auto complete will destroy the dropdown element.
    mViewModel.getMeasurementAddModel().getStressed().observe(getViewLifecycleOwner(),
        new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.stress.setText(s, false);
          }
        });

    mViewModel.getMeasurementAddModel().getTired().observe(getViewLifecycleOwner(),
        new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.tired.setText(s, false);
          }
        });

    // Date and time picker buttons
    mBinding.dateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        chooseDateDialog();
      }
    });

    mBinding.timeButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        chooseTimeDialog();
      }
    });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.add_measurement_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save:
        if (inPutOkay()) {
          save();
        }
        return true;

      case R.id.clear:
        mViewModel.updateMeasurementAddModel(new Measurement(
            -1, -1,
            false,
            "",
            -1,
            "",
            "",
            false,
            false,
            false,
            false,
            false,
            -1));
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Saving the current state to the viewModel.
   *
   * Uses a function to parse a Float value.
   *
   * If the Integer value is empty, the result will be a -1.
   *
   * The view will convert that -1 to an empty String.
   */
  @Override
  public void onStop() {
    super.onStop();

    // Set timestamp for date and time

    mViewModel.getMeasurementAddModel().getTimestamp().setValue(buildTimestamp());

    // The Integer value have to be parsed first

    // If amount is 0, there has been no user input because the amount cannot be zero.
    // In this case set it to -1, so the parser will use an empty string

    mViewModel.getMeasurementAddModel().getAmount().setValue(
        Parser.parseInteger(Objects.requireNonNull(mBinding.amount.getText()).toString()));
    mViewModel.getMeasurementAddModel().getValue0().setValue(
        Parser.parseInteger(Objects.requireNonNull(mBinding.mv0.getText()).toString()));

    // Checkboxes
    mViewModel.getMeasurementAddModel().isGi().setValue(mBinding.gi.isChecked());
    mViewModel.getMeasurementAddModel().getPhysicallyActivity()
        .setValue(mBinding.physicallyActive.isChecked());
    mViewModel.getMeasurementAddModel().getAlcoholConsumed()
        .setValue(mBinding.alcoholConsumed.isChecked());
    mViewModel.getMeasurementAddModel().getIll().setValue(mBinding.ill.isChecked());
    mViewModel.getMeasurementAddModel().getMedication().setValue(mBinding.medication.isChecked());
    mViewModel.getMeasurementAddModel().getPeriod().setValue(mBinding.period.isChecked());

    // Drop downs
    mViewModel.getMeasurementAddModel().getStressed()
        .setValue(mBinding.stress.getText().toString());
    mViewModel.getMeasurementAddModel().getTired().setValue(mBinding.tired.getText().toString());
  }

  private void chooseDateDialog() {
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DATE);

    DatePickerDialog datePickerDialog =
        new DatePickerDialog(Objects.requireNonNull(getContext()), this, year, month, day);
    datePickerDialog.show();
  }

  private void chooseTimeDialog() {
    TimePickerDialog timePickerDialog =
        new TimePickerDialog(getContext(), this, 0, 0, false);
    timePickerDialog.show();
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    // Build a string of a date by using the calender class
    // with the pattern dd.mm.yyyy
    // Example:
    // 11.10.2019
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, dayOfMonth, 0, 0, 0);
    Date date = calendar.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    mBinding.date.setText(sdf.format(date));
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    // Adds either am or pm at the end of the result string
    String am_pm;

    // Get the format AM or PM by checking if the hourOfDay value.
    // If it is
    if (hourOfDay == 0) {
      hourOfDay += 12;
      am_pm = " AM";
    } else if (hourOfDay == 12) {
      am_pm = " PM";
    } else if (hourOfDay > 12) {
      hourOfDay -= 12;
      am_pm = " PM";
    } else {
      am_pm = " AM";
    }

    Calendar calendar = Calendar.getInstance();
    calendar.set(0, 0, 0, hourOfDay, minute, 0);
    Date date = calendar.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("hh.mm", Locale.getDefault());
    String finalText = sdf.format(date) + " " + am_pm;
    mBinding.time.setText(finalText);
  }

  /**
   * Checks if the user input has been valid.
   *
   * The input is okay if valid data has been entered
   *
   * @return returns true if the input was okay. returns false otherwise.
   */
  private boolean inPutOkay() {
    // checks the text fields
    if (mBinding.date.getText() == null || mBinding.date.getText().toString().equals("")) {
      toast("Please select the date.");
      return false;
    }

    if (mBinding.time.getText() == null || mBinding.time.getText().toString().equals("")) {
      toast("Please select a time.");
      return false;
    }

    if (mBinding.amount.getText() == null || mBinding.amount.getText().toString().equals("")) {
      toast("Please enter the amount consumed.");
      return false;
    }

    // checks the drop down menus
    if (mBinding.stress.getText() == null || mBinding.stress.getText().toString().equals("")) {
      toast("Please select the stress level.");
      return false;
    }

    if (mBinding.tired.getText() == null || mBinding.tired.getText().toString().equals("")) {
      toast("Please select the tired level.");
      return false;
    }

    if (mBinding.mv0.getText() == null || mBinding.mv0.getText().toString().equals("")) {
      toast("Please select a start glucose value.");
      return false;
    }

    return true;
  }

  /**
   * Save the food to the database.
   */
  private void save() {
    //TODO check if no other measurement is unfinished

    final LiveData<UserHistory> uh = mViewModel.getUserHistoryLatest();
    uh.observe(this, new Observer<UserHistory>() {
      @Override
      public void onChanged(UserHistory userHistory) {
        uh.removeObserver(this);

        if (userHistory == null) {
          MyToast.createToast(getContext(), "Please enter user data first!");
          return;
        }

        final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);
        ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
          @Override
          public void onChanged(Food food) {
            ldf.removeObserver(this);

            String timeStamp = buildTimestamp();

            int amount = Integer
                .parseInt(Objects.requireNonNull(mBinding.amount.getText()).toString());
            String stress = mBinding.stress.getText().toString();
            String tired = mBinding.tired.getText().toString();
            boolean gi = mBinding.gi.isChecked();
            boolean physicallyActive = mBinding.physicallyActive.isChecked();
            boolean alcoholConsumed = mBinding.alcoholConsumed.isChecked();
            boolean ill = mBinding.ill.isChecked();
            boolean medication = mBinding.medication.isChecked();
            boolean period = mBinding.period.isChecked();

            int glucose_0 = Integer
                .parseInt(Objects.requireNonNull(mBinding.mv0.getText()).toString());

            Measurement newMeasurement = new Measurement(
                mFoodId,
                Objects.requireNonNull(uh.getValue()).id,
                gi,
                timeStamp,
                amount,
                stress, tired,
                physicallyActive, alcoholConsumed,
                ill, medication, period,
                glucose_0
            );

            mViewModel.insertMeasurement(newMeasurement);

            // Navigate back to me fragment
            Bundle bundle = new Bundle();
            bundle.putInt("food_id", mFoodId);

            Navigation.findNavController(Objects.requireNonNull(getView()))
                .navigate(R.id.action_addMeasurement_to_foodInfoFragment, bundle);
          }
        });
      }
    });
  }

  /**
   *
   * EXAMPLE: 05.07.2019_06:03 AM
   *
   * @return Returns a timestamp with the pattern "dd.mm.yyyy_hh:mm FORMAT"
   *
   */
  private String buildTimestamp() {
    if (mBinding.date.getText() == null || mBinding.date.getText().toString().equals("")
        || mBinding.time.getText() == null || mBinding.time.getText().toString().equals("")) {
      return "";
    }

    return mBinding.date.getText().toString() + ":" + mBinding.time.getText().toString();
  }

  private void toast(String msg) {
    MyToast.createToast(getContext(), msg);
  }
}
