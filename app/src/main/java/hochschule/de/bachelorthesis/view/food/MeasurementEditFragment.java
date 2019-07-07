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
import com.google.android.material.textfield.TextInputEditText;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementEditBinding;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
      if (isPutOkay()) {
        save();
      }
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

  private void chooseTimeDialog() {
    TimePickerDialog timePickerDialog =
        new TimePickerDialog(getContext(), this, 0, 0, false);
    timePickerDialog.show();
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
  private boolean isPutOkay() {
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

    int mv0 = Integer.parseInt(Objects.requireNonNull(mBinding.mv0.getText()).toString());
    int mv15 = Integer.parseInt(Objects.requireNonNull(mBinding.mv15.getText()).toString());
    int mv30 = Integer.parseInt(Objects.requireNonNull(mBinding.mv30.getText()).toString());
    int mv45 = Integer.parseInt(Objects.requireNonNull(mBinding.mv45.getText()).toString());
    int mv60 = Integer.parseInt(Objects.requireNonNull(mBinding.mv60.getText()).toString());
    int mv75 = Integer.parseInt(Objects.requireNonNull(mBinding.mv75.getText()).toString());
    int mv90 = Integer.parseInt(Objects.requireNonNull(mBinding.mv90.getText()).toString());
    int mv105 = Integer.parseInt(Objects.requireNonNull(mBinding.mv105.getText()).toString());
    int mv120 = Integer.parseInt(Objects.requireNonNull(mBinding.mv120.getText()).toString());

    if (mv0 != 0) {
      measurement.setGlucoseStart(mv0);
    }

    if (mv15 != 0) {
      measurement.setGlucose15(mv15);
    }

    if (mv30 != 0) {
      measurement.setGlucose30(mv30);
    }

    if (mv45 != 0) {
      measurement.setGlucose45(mv45);
    }

    if (mv60 != 0) {
      measurement.setGlucose60(mv60);
    }

    if (mv75 != 0) {
      measurement.setGlucose75(mv75);
    }

    if (mv90 != 0) {
      measurement.setGlucose90(mv90);
    }

    if (mv105 != 0) {
      measurement.setGlucose105(mv105);
    }

    if (mv120 != 0) {
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
