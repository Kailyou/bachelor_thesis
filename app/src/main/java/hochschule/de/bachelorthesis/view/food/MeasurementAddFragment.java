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

import hochschule.de.bachelorthesis.databinding.FragmentMeasurementAddBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

public class MeasurementAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

  private FragmentMeasurementAddBinding mBinding;

  private FoodViewModel mViewModel;

  private int mFoodId;

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
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    // Init data binding
    mBinding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_measurement_add, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setViewModel(mViewModel);

    // Spinner
    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(Objects.requireNonNull(getContext()),
            R.array.stress, android.R.layout.simple_spinner_item);

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    mBinding.stress.setAdapter(adapter);

    adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
        R.array.tired, android.R.layout.simple_spinner_item);
    mBinding.tired.setAdapter(adapter);

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

    /*
    // The float values have to be parsed first
    mViewModel.getmMeasurementAddModel().getAmount().setValue(
        Parser.parseFloat(Objects.requireNonNull(mBinding.amount.getText().toString())));

    // The exposed drop down
    // get the needed string out of the string array resource and update the vm.
    mViewModel.getFoodAddDataModel().getType().setValue(mBinding.type.getText().toString());



    mViewModel.getFoodAddDataModel().getKiloJoules().setValue(
        Converter.parseFloat(Objects.requireNonNull(mBinding.kiloJoules.getText()).toString()));

    mViewModel.getFoodAddDataModel().getFat().setValue(
        Converter.parseFloat(Objects.requireNonNull(mBinding.fat.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSaturates().setValue(
        Converter.parseFloat(Objects.requireNonNull(mBinding.saturates.getText()).toString()));

    mViewModel.getFoodAddDataModel().getProtein().setValue(
        Converter.parseFloat(Objects.requireNonNull(mBinding.protein.getText()).toString()));

    mViewModel.getFoodAddDataModel().getCarbohydrates().setValue(
        Converter.parseFloat(Objects.requireNonNull(mBinding.carbohydrate.getText()).toString()));

    mViewModel.getFoodAddDataModel().getKiloCalories().setValue(
        Converter.parseFloat(Objects.requireNonNull(mBinding.kiloCalories.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSugars().setValue(
        Converter.parseFloat(Objects.requireNonNull(mBinding.sugars.getText()).toString()));

    mViewModel.getFoodAddDataModel().getSalt().setValue(
        Converter.parseFloat(Objects.requireNonNull(mBinding.salt.getText()).toString()));
        */
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.add_measurement_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.save) {
      if (inPutOkay()) {
        save();
      }

      return true;
    }

    return super.onOptionsItemSelected(item);
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

            // Build timestamp
            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear, mMonth, mDayOfMonth, mHourOfDay, mMinute, 0);
            Date date = calendar.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());
            String timeStamp = sdf.format(date);

            int amount = Integer
                .parseInt(Objects.requireNonNull(mBinding.amount.getText()).toString());
            String stress = mBinding.stress.getText().toString();
            String tired = mBinding.tired.getText().toString();
            int glucose_0 = Integer
                .parseInt(Objects.requireNonNull(mBinding.mv0.getText()).toString());

            Measurement newMeasurement = new Measurement(
                mFoodId,
                Objects.requireNonNull(uh.getValue()).id,
                timeStamp,
                amount,
                stress, tired,
                glucose_0
            );

            Log.d("yolo", "onChanged: food id = " + mFoodId);

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

  private void toast(String msg) {
    MyToast.createToast(getContext(), msg);
  }
}
