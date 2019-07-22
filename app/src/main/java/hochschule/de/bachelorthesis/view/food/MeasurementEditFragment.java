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
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementEditBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * @author thielenm
 *
 * The View class for editing an existing measurement.
 *
 * First the existing measurement will be loaded from the database and the text fields will be
 * updated.
 *
 * The user can clear the data himself by pressing the clear button in the action bar.
 *
 * Finally, the user can save the measurement by pressing the save icon if every text field has been
 * filled out.
 */
public class MeasurementEditFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

  private FragmentMeasurementEditBinding mBinding;

  private FoodViewModel mViewModel;

  //private int mFoodId;
  private int mMeasurementId;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable menu
    setHasOptionsMenu(true);

    // View model
    mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
        .get(FoodViewModel.class);

    // Get passed food id
    assert getArguments() != null;
    //mFoodId = getArguments().getInt("food_id");
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

    // Load the measurement from database
    mViewModel.getMeasurementById(mMeasurementId).observe(getViewLifecycleOwner(),
        new Observer<Measurement>() {
          @Override
          public void onChanged(Measurement measurement) {
            /* Update text views */
            updateTextViews(measurement.getTimeStamp(),
                measurement.isGi(), measurement.getAmount(),
                measurement.getStress(), measurement.getTired(), measurement.isPhysicallyActivity(),
                measurement.isAlcoholConsumed(), measurement.isIll(), measurement.isMedication(),
                measurement.isPeriod(), measurement.getGlucoseStart(), measurement.getGlucose15(),
                measurement.getGlucose30(), measurement.getGlucose45(), measurement.getGlucose60(),
                measurement.getGlucose75(), measurement.getGlucose90(), measurement.getGlucose105(),
                measurement.getGlucose120()
            );
          }
        });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.measurement_add_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save:
        if (isInputOkay()) {
          save();
        }
        return true;

      // While clearing. Counts will be set to -1, so they
      // can be parsed to an empty String.
      case R.id.clear:
        /* Update text views */
        updateTextViews("",
            false, -1,
            "", "", false,
            false, false, false,
            false, -1, -1, -1, -1, -1, -1, -1, -1, -1
        );
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    // Build a string of a date by using the calender class
    // with the pattern dd.mm.yyyy
    // Example: 11.10.2019
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, dayOfMonth, 0, 0, 0);
    Date date = calendar.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    mBinding.date.setText(sdf.format(date));
  }

  @Override
  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    // Create a calender instance to create a date object with the pattern
    // hh:mm to get a String like that of the given hours and minute.
    // Finally add the formatting at the end and updateFood the view
    // Example: 06:25 AM
    Calendar calendar = Calendar.getInstance();
    calendar.set(0, 0, 0, hourOfDay, minute, 0);
    Date date = calendar.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("hh.mm aa", Locale.getDefault());

    mBinding.time.setText(sdf.format(date));
  }

  /**
   * Opens a dialog to choose the date with predefined date values (the current ones).
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
   * Opens a dialog to choose the time with predefined date values (the current ones).
   */
  private void chooseTimeDialog() {
    Calendar c = Calendar.getInstance();
    int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    TimePickerDialog timePickerDialog =
        new TimePickerDialog(getContext(), this, hourOfDay, minute, false);
    timePickerDialog.show();
  }

  /**
   * Updates the text views.
   *
   * @param timeStamp The timestamp
   * @param isGi Gi calculation or not
   * @param amount Amount food consumed
   * @param stress Stress
   * @param tired Tired
   * @param isPhysicallyActive Physically active
   * @param hasAlcoholConsumed Alcohol consumed
   * @param isIll Ill
   * @param takesMedication Taking medication
   * @param hasPeriod is on Period
   * @param mv0 glucose value start
   * @param mv15 glucose value after 15 minutes
   * @param mv30 glucose value after 30 minutes
   * @param mv45 glucose value after 45 minutes
   * @param mv60 glucose value after 60 minutes
   * @param mv75 glucose value after 75 minutes
   * @param mv90 glucose value after 90 minutes
   * @param mv105 glucose value after 105 minutes
   * @param mv120 glucose value after 120 minutes
   */
  private void updateTextViews(String timeStamp,
      boolean isGi, int amount, String stress, String tired, boolean isPhysicallyActive,
      boolean hasAlcoholConsumed,
      boolean isIll, boolean takesMedication, boolean hasPeriod,
      int mv0, int mv15, int mv30, int mv45, int mv60,
      int mv75, int mv90, int mv105, int mv120) {

    /* Update text views */

    // Time information
    mBinding.date.setText(
        Converter.convertTimeStampToDate(timeStamp));

    mBinding.time
        .setText(Converter.convertTimeStampToTimeStart(timeStamp));

    // Advance information
    mBinding.gi.setChecked(isGi);
    mBinding.amount.setText(Converter.convertInteger(amount));
    mBinding.stress.setText(stress);
    mBinding.tired.setText(tired);
    mBinding.physicallyActive.setChecked(isPhysicallyActive);
    mBinding.alcoholConsumed.setChecked(hasAlcoholConsumed);

    // Events
    mBinding.ill.setChecked(isIll);
    mBinding.medication.setChecked(takesMedication);
    mBinding.period.setChecked(hasPeriod);

    // Glucose Values
    mBinding.mv0.setText(Converter.convertIntegerMeasurement(mv0));
    mBinding.mv15.setText(Converter.convertIntegerMeasurement(mv15));
    mBinding.mv30.setText(Converter.convertIntegerMeasurement(mv30));
    mBinding.mv45.setText(Converter.convertIntegerMeasurement(mv45));
    mBinding.mv60.setText(Converter.convertIntegerMeasurement(mv60));
    mBinding.mv75.setText(Converter.convertIntegerMeasurement(mv75));
    mBinding.mv90.setText(Converter.convertIntegerMeasurement(mv90));
    mBinding.mv105.setText(Converter.convertIntegerMeasurement(mv105));
    mBinding.mv120.setText(Converter.convertIntegerMeasurement(mv120));
  }

  /**
   * Checks if the user input has been valid.
   *
   * The input is okay if at least the minimum data has been entered
   *
   * Required data for valid input: date, time, amount, stress, tired and glucose begin
   *
   * Checkboxes are not required because they are default set to false.
   *
   * @return returns true if the input was okay. returns false otherwise.
   */
  private boolean isInputOkay() {
    // checks the text fields
    if (mBinding.date.getText() == null || mBinding.date.getText().toString().equals("")) {
      toast("Please select the date.");
      return false;
    }

    if (mBinding.time.getText() == null || mBinding.time.getText().toString().equals("")) {
      toast("Please select the time.");
      return false;
    }

    // If it is a GI measurement, the amount field is not important because it will be disabled
    // and the amount to consume will be calculated instead of.
    if (!mBinding.gi.isChecked()) {
      if (mBinding.amount.getText() == null || mBinding.amount.getText().toString().equals("")) {
        toast("Please enter the amount consumed.");
        return false;
      }
    }

    if (mBinding.stress.getText() == null || mBinding.stress.getText().toString().equals("")) {
      toast("Please select the stress level.");
      return false;
    }

    if (mBinding.tired.getText() == null || mBinding.tired.getText().toString().equals("")) {
      toast("Please select the tiredness level.");
      return false;
    }

    if (mBinding.mv0.getText() == null || mBinding.mv0.getText().toString().equals("")) {
      toast("Please select at least the start glucose value.");
      return false;
    }

    return true;
  }

  /**
   * Updates the current measurement database entry.
   *
   * If the measurement is done get the viewModel updated
   */
  private void save() {
    // Get the current measurement
    final LiveData<Measurement> ldm = mViewModel.getMeasurementById(mMeasurementId);
    ldm.observe(getViewLifecycleOwner(), new Observer<Measurement>() {
      @Override
      public void onChanged(final Measurement measurement) {
        ldm.removeObserver(this);
        updateMeasurement(measurement);
      }
    });
  }


  private void updateMeasurement(final Measurement measurement) {
    if (measurement == null) {
      return;
    }

    measurement.setTimeStamp(buildTimestamp());
    measurement.setGi(mBinding.gi.isChecked());

    // if GI calculation, amount field will be deactivated and the amount will be calculated
    if (measurement.isGi()) {
      //TODO calculate the amount, for test reasons set to 1000
      measurement.setAmount(1000);
    } else {
      measurement.setAmount(Integer
          .parseInt(Objects.requireNonNull(mBinding.amount.getText()).toString()));
    }

    measurement.setStress(mBinding.stress.getText().toString());
    measurement.setTired(mBinding.tired.getText().toString());
    measurement.setPhysicallyActivity(mBinding.physicallyActive.isChecked());
    measurement.setAlcoholConsumed(mBinding.alcoholConsumed.isChecked());
    measurement.setIll(mBinding.ill.isChecked());

    // if not ill, medication checkbox will be deactivated and set to false
    if (!measurement.isIll()) {
      measurement.setMedication(mBinding.medication.isChecked());
    } else {
      mBinding.medication.setChecked(false);
      mBinding.medication.setEnabled(false);
      measurement.setMedication(false);
    }

    // TODO check if user is male or female, if female, disable
    measurement.setPeriod(mBinding.period.isChecked());

    if (mBinding.mv0.getText() != null && !mBinding.mv0.getText().toString().equals("")) {
      int mv0 = Integer.parseInt(Objects.requireNonNull(mBinding.mv0.getText()).toString());
      measurement.setGlucoseStart(mv0);
    }

    if (mBinding.mv15.getText() != null && !mBinding.mv15.getText().toString().equals("")) {
      int mv15 = Integer.parseInt(Objects.requireNonNull(mBinding.mv15.getText()).toString());
      measurement.setGlucose15(mv15);
    }

    if (mBinding.mv30.getText() != null && !mBinding.mv30.getText().toString().equals("")) {
      int mv30 = Integer.parseInt(Objects.requireNonNull(mBinding.mv30.getText()).toString());
      measurement.setGlucose30(mv30);
    }

    if (mBinding.mv45.getText() != null && !mBinding.mv45.getText().toString().equals("")) {
      int mv45 = Integer.parseInt(Objects.requireNonNull(mBinding.mv45.getText()).toString());
      measurement.setGlucose45(mv45);
    }

    if (mBinding.mv60.getText() != null && !mBinding.mv60.getText().toString().equals("")) {
      int mv60 = Integer.parseInt(Objects.requireNonNull(mBinding.mv60.getText()).toString());
      measurement.setGlucose60(mv60);
    }

    if (mBinding.mv75.getText() != null && !mBinding.mv75.getText().toString().equals("")) {
      int mv75 = Integer.parseInt(Objects.requireNonNull(mBinding.mv75.getText()).toString());
      measurement.setGlucose75(mv75);
    }

    if (mBinding.mv90.getText() != null && !mBinding.mv90.getText().toString().equals("")) {
      int mv90 = Integer.parseInt(Objects.requireNonNull(mBinding.mv90.getText()).toString());
      measurement.setGlucose90(mv90);
    }

    if (mBinding.mv105.getText() != null && !mBinding.mv105.getText().toString().equals("")) {
      int mv105 = Integer.parseInt(Objects.requireNonNull(mBinding.mv105.getText()).toString());
      measurement.setGlucose105(mv105);
    }

    if (mBinding.mv120.getText() != null && !mBinding.mv120.getText().toString().equals("")) {
      int mv120 = Integer.parseInt(Objects.requireNonNull(mBinding.mv120.getText()).toString());
      measurement.setGlucose120(mv120);
    }

    mViewModel.updateMeasurement(measurement);
  }

  /**
   * @return Returns a timestamp with the pattern "dd/MM/yyyy_KK:mm aa"
   *
   * EXAMPLE: 05/07/2019_06:03 AM
   */
  private String buildTimestamp() {
    if (mBinding.date.getText() == null || mBinding.date.getText().toString().equals("")
        || mBinding.time.getText() == null || mBinding.time.getText().toString().equals("")) {
      return "";
    }

    return mBinding.date.getText().toString() + ":" + mBinding.time.getText().toString();
  }

  private void toast(String msg) {
    MySnackBar.createSnackBar(getContext(), msg);
  }
}
