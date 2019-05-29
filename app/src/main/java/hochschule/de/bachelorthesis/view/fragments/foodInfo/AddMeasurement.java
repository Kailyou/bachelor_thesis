package hochschule.de.bachelorthesis.view.fragments.foodInfo;

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

import com.google.android.material.picker.MaterialStyledDatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentAddMeasurementBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.view_model.viewModels.AddMeasurementViewModel;
import hochschule.de.bachelorthesis.view_model.viewModels.FoodInfoViewModel;

public class AddMeasurement extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "AddFood";

    private FragmentAddMeasurementBinding mBinding;

    private AddMeasurementViewModel mViewModel;

    private int mFoodId;
    private int mUserHistoryId;

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
        mViewModel = ViewModelProviders.of(getActivity()).get(AddMeasurementViewModel.class);

        // Get passed food id
        assert getArguments() != null;
        mFoodId = getArguments().getInt("food_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_measurement, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        // Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.stress, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.stress.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.tired, android.R.layout.simple_spinner_item);
        mBinding.tired.setAdapter(adapter);

        // Date and time picker buttons
        mBinding.date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chooseDateDialog();
            }
        });

        mBinding.time.setOnClickListener(new View.OnClickListener() {
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
        if (item.getItemId() == R.id.save) {
            //if(inPutOkay()) {
           //     save();
            //}

            save();

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
                new DatePickerDialog(getContext(), this, year, month, day);
        datePickerDialog.show();
    }

    private void chooseTimeDialog() {
        TimePickerDialog timePickerDialog =
                new TimePickerDialog(getContext(),this, 0, 0, false);
        timePickerDialog.show();
    }

    /**
     * Save the food to the database.
     */
    private void save() {
        mViewModel.getUserHistoryId().observe(this, new Observer<UserHistory>() {
            @Override
            public void onChanged(UserHistory userHistory) {
                if(userHistory == null) {
                    MyToast.createToast(getContext(),"ist null alta");
                    return;
                }
                mUserHistoryId = userHistory.id;
            }
        });

        // Build timestamp
        Calendar calender = Calendar.getInstance();
        calender.set(mYear, mMonth, mDayOfMonth, mHourOfDay, mMinute, 0);
        Date date = calender.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());
        String timeStamp = sdf.format(date);
        Log.d("yolo", "Current Timestamp: " + timeStamp);

        int amount = Integer.parseInt(Objects.requireNonNull(mBinding.amount.getText()).toString());
        String stress = mBinding.stress.getText().toString();
        String tired = mBinding.tired.getText().toString();
        int glucose_0 = Integer.parseInt(Objects.requireNonNull(mBinding.mv0.getText()).toString());
        /*
        int glucose_15 = Integer.parseInt(Objects.requireNonNull(mBinding.mv15.getText()).toString());
        int glucose_30 = Integer.parseInt(Objects.requireNonNull(mBinding.mv30.getText()).toString());
        int glucose_45 = Integer.parseInt(Objects.requireNonNull(mBinding.mv45.getText()).toString());
        int glucose_60 = Integer.parseInt(Objects.requireNonNull(mBinding.mv60.getText()).toString());
        int glucose_75 = Integer.parseInt(Objects.requireNonNull(mBinding.mv75.getText()).toString());
        int glucose_90 = Integer.parseInt(Objects.requireNonNull(mBinding.mv90.getText()).toString());
        int glucose_105 = Integer.parseInt(Objects.requireNonNull(mBinding.mv105.getText()).toString());
        int glucose_120 = Integer.parseInt(Objects.requireNonNull(mBinding.mv120.getText()).toString());

        Measurement newMeasurement = new Measurement(
                mFoodId,
                mUserHistoryId,
                timeStamp,
                amount,
                stress, tired,
                glucose_0,
                glucose_15,
                glucose_30,
                glucose_45,
                glucose_60,
                glucose_75,
                glucose_90,
                glucose_105,
                glucose_120,
                0,
                0,
                "unrated"
                );
                */

        Measurement newMeasurement = new Measurement(
                mFoodId,
                mUserHistoryId,
                timeStamp,
                amount,
                stress, tired,
                glucose_0
        );

        mViewModel.insert(newMeasurement);
    }

    /**
     * Checks if the user input has been valid.
     * @return
     * returns true if the input was okay.
     * returns false otherwise.
     */
    private boolean inPutOkay() {
        // checks the text fields
        if(mBinding.date.getText() == null || mBinding.date.getText().toString().equals("")) {
            toast("Please enter the food's name.");
            return false;
        }

        if(mBinding.time.getText() == null || mBinding.time.getText().toString().equals("")) {
            toast("Please enter the food's brand name.");
            return false;
        }

        if(mBinding.amount.getText() == null || mBinding.amount.getText().toString().equals("")) {
            toast("Please enter the food's type.");
            return false;
        }

        // checks the drop down menus
        if(mBinding.stress.getText() == null || mBinding.stress.getText().toString().equals("")) {
            toast("Please enter the kilo calories.");
            return false;
        }

        if(mBinding.tired.getText() == null || mBinding.tired.getText().toString().equals("")) {
            toast("Please enter the kilo joules.");
            return false;
        }

        return true;
    }

    private void toast(String msg) {
        MyToast.createToast(getContext(), msg);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
       mYear = year;
       mMonth = month;
       mDayOfMonth = dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHourOfDay = hourOfDay;
        mMinute = minute;
    }
}
