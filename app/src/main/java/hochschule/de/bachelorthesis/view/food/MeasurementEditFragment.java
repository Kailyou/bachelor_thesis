package hochschule.de.bachelorthesis.view.food;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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

import hochschule.de.bachelorthesis.databinding.FragmentMeasurementEditBinding;
import hochschule.de.bachelorthesis.utility.MyMath;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

public class MeasurementEditFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

  private static final String TAG = "FoodAddFragment";

  private FragmentMeasurementEditBinding mBinding;

  private FoodViewModel mViewModel;

  private int mFoodId;
  private int mMeasurementId;

  // Time
  private int mYear;
  private int mMonth;
  private int mDayOfMonth;
  private int mHourOfDay;
  private int mMinute;


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
    mMeasurementId = getArguments().getInt("measurement_id");
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_measurement_edit, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

    // Spinner
    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(Objects.requireNonNull(getContext()),
            R.array.stress, android.R.layout.simple_spinner_item);

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mBinding.stress.setAdapter(adapter);

    adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
        R.array.tired, android.R.layout.simple_spinner_item);
    mBinding.tired.setAdapter(adapter);

    // Date picker button
    mBinding.dateButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        chooseDateDialog();
      }
    });

    // Time picker button
    mBinding.timeButton.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        chooseTimeDialog();
      }
    });

    // Load
    mViewModel.getMeasurementById(mMeasurementId).observe(getViewLifecycleOwner(),
        new Observer<Measurement>() {
          @Override
          public void onChanged(Measurement measurement) {
            mViewModel.loadMeasurementFragment(measurement);
          }
        });

    // Update drop downs
    mViewModel.getMeasurementModel().getStressed().observe(getViewLifecycleOwner(),
        new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.stress.setText(s, false);
          }
        });

    mViewModel.getMeasurementModel().getTired().observe(getViewLifecycleOwner(),
        new Observer<String>() {
          @Override
          public void onChanged(String s) {
            mBinding.tired.setText(s, false);
          }
        });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.add_measurement_menu, menu);
  }

  //TODO add delete function
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.save) {
      save();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Opens a dialog to chose the date.
   */
  private void chooseDateDialog() {
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DATE);

    DatePickerDialog datePickerDialog =
        new DatePickerDialog(Objects.requireNonNull(getContext()), this, year, month, day);
    datePickerDialog.show();
  }

  /**
   * Opens a dialog to choose the time.
   */
  private void chooseTimeDialog() {
    TimePickerDialog timePickerDialog =
        new TimePickerDialog(getContext(), this, 0, 0, false);
    timePickerDialog.show();
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    // Takes the existing time values and add leading zeros to strings smaller than 10.
    // So the result view will look e.g. 06.04.2019 rather than 6.4.2019
    String monthFormatted = String.valueOf(month);
    String dayFormatted = String.valueOf(dayOfMonth);

    if (month < 10) {
      monthFormatted = "0" + month;
    }

    if (dayOfMonth < 10) {
      dayFormatted = "0" + dayOfMonth;
    }

    final String date = "" + dayFormatted + "." + monthFormatted + "." + year;
    mBinding.date.setText(date);

    mYear = year;
    mMonth = month;
    mDayOfMonth = dayOfMonth;
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    // Takes the existing time values and add leading zeros to strings smaller than 10.
    // So the result view will look e.g. 06.04.2019 rather than 6.4.2019
    String hoursFormatted = String.valueOf(hourOfDay);
    String minutesFormatted = String.valueOf(minute);

    if (hourOfDay < 10) {
      hoursFormatted = "0" + hourOfDay;
    }

    if (minute < 10) {
      minutesFormatted = "0" + minute;
    }

    // Adds either am or pm at the end of the result string
    String am_pm;

    if (hourOfDay < 12) {
      am_pm = "am";
    } else {
      am_pm = "pm";
    }

    final String time = "" + hoursFormatted + ":" + minutesFormatted + am_pm;
    mBinding.time.setText(time);

    mHourOfDay = hourOfDay;
    mMinute = minute;
  }

  /**
   * Updates the current measurement database entry.
   * <p>
   * If the measurement is done get the viewModel updated - The amount of measurements - The max
   * glucose value - The average glucose value - The rating - The personal index
   */
  private void save() {
    // Get the current measurement
    final LiveData<Measurement> ldm = mViewModel.getMeasurementById(mFoodId);
    ldm.observe(getViewLifecycleOwner(), new Observer<Measurement>() {
      @Override
      public void onChanged(final Measurement measurement) {
        ldm.removeObserver(this);

        // Get the food object
        final LiveData<Food> f = mViewModel.getFoodById(mFoodId);
        f.observe(getViewLifecycleOwner(), new Observer<Food>() {
          @Override
          public void onChanged(Food food) {
            f.removeObserver(this);
            updateMeasurement(food, measurement);
          }
        });
      }
    });
  }


  private void updateMeasurement(Food food, final Measurement measurement) {
    // Build timestamp
    Calendar calender = Calendar.getInstance();
    calender.set(mYear, mMonth, mDayOfMonth, mHourOfDay, mMinute, 0);
    Date date = calender.getTime();

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());

    // New values
    String timeStamp = sdf.format(date);
    int amount = Integer.parseInt(Objects.requireNonNull(mBinding.amount.getText()).toString());
    String stress = mBinding.stress.getText().toString();
    String tired = mBinding.tired.getText().toString();

    final ArrayList<Integer> glucoseValues = new ArrayList<>();
    glucoseValues.add(Integer.parseInt(Objects.requireNonNull(mBinding.mv0.getText()).toString()));
    glucoseValues.add(Integer.parseInt(Objects.requireNonNull(mBinding.mv15.getText()).toString()));
    glucoseValues.add(Integer.parseInt(Objects.requireNonNull(mBinding.mv30.getText()).toString()));
    glucoseValues.add(Integer.parseInt(Objects.requireNonNull(mBinding.mv45.getText()).toString()));
    glucoseValues.add(Integer.parseInt(Objects.requireNonNull(mBinding.mv60.getText()).toString()));
    glucoseValues.add(Integer.parseInt(Objects.requireNonNull(mBinding.mv75.getText()).toString()));
    glucoseValues.add(Integer.parseInt(Objects.requireNonNull(mBinding.mv90.getText()).toString()));
    glucoseValues
        .add(Integer.parseInt(Objects.requireNonNull(mBinding.mv105.getText()).toString()));
    glucoseValues
        .add(Integer.parseInt(Objects.requireNonNull(mBinding.mv120.getText()).toString()));

    // check if all measurements have been entered
    boolean isDone = checkIsDone(glucoseValues);

    if (isDone) {
      mViewModel.updateMeasurement(measurement);
    }
  }

  /**
   * Checks if the measurement is done, by checking the elements in the measurement array. If only
   * one value is zero, the measurement cannot be completed yet.
   *
   * @param measurements - An array with the current measurement values.
   * @return - Returns true if the measurement has been completed, false otherwise.
   */
  private boolean checkIsDone(ArrayList<Integer> measurements) {
    for (int i : measurements) {
      if (i == 0) {
        return false;
      }
    }

    return true;
  }

  /**
   * Calculates the new glucose max value, which is the current max peak of all measurements for
   * that food.
   * <p>
   * A given array with the current measurement values will be compared with the current max value.
   * <p>
   * The biggest will be returned.
   *
   * @param measurements - Array with current measurement values.
   * @param currentMaxPeak - The current glucose max value.
   * @return - The new glucose max value.
   */
  private int calculateGlucoseMax(int[] measurements, int currentMaxPeak) {
    int newGlucoseMax = currentMaxPeak;

    for (int i = 1; i < measurements.length; i++) {
      if (measurements[i] > newGlucoseMax) {
        newGlucoseMax = measurements[i];
      }
    }

    return newGlucoseMax;
  }


  /*
  private int CalculateGlucoseAverageAll(int[] measurements) {

  }
  */

  /**
   * Checks if the user input has been valid.
   *
   * @return returns true if the input was okay. returns false otherwise.
   */
  private boolean inPutOkay() {
    // checks the text fields
    if (mBinding.date.getText() == null || mBinding.date.getText().toString().equals("")) {
      toast("Please enter the food's name.");
      return false;
    }

    if (mBinding.time.getText() == null || mBinding.time.getText().toString().equals("")) {
      toast("Please enter the food's brand name.");
      return false;
    }

    if (mBinding.amount.getText() == null || mBinding.amount.getText().toString().equals("")) {
      toast("Please enter the food's type.");
      return false;
    }

    // checks the drop down menus
    if (mBinding.stress.getText() == null || mBinding.stress.getText().toString().equals("")) {
      toast("Please enter the kilo calories.");
      return false;
    }

    if (mBinding.tired.getText() == null || mBinding.tired.getText().toString().equals("")) {
      toast("Please enter the kilo joules.");
      return false;
    }

    return true;
  }

  private void toast(String msg) {
    MyToast.createToast(getContext(), msg);
  }
}
