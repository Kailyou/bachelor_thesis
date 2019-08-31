package hochschule.de.bachelorthesis.view.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentUpdateBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.viewmodels.HomeViewModel;

import java.util.Objects;

/**
 * @author Maik Thielen
 * <p>
 * View for the measurement update fragment from the home view.
 * <p>
 * A simple form to enter or change measurement values.
 * <p>
 * The user can save by clicking on the save button in the action bar.
 */
public class UpdateFragment extends Fragment {

    private HomeViewModel mViewModel;

    private FragmentUpdateBinding mBinding;

    private int mMeasurementId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change title
        Objects.requireNonNull(getActivity()).setTitle("Update current Measurement");

        // Enable menu
        setHasOptionsMenu(true);

        // View model
        mViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Init data binding
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_update, container, false);
        mBinding.setLifecycleOwner(this);

        // Get passed food id
        assert getArguments() != null;
        mMeasurementId = getArguments().getInt("measurement_id");

        // Load measurement and set the text views
        final LiveData<Measurement> ldm = mViewModel.getMeasurementById(mMeasurementId);
        ldm.observe(getViewLifecycleOwner(), new Observer<Measurement>() {
            @Override
            public void onChanged(Measurement measurement) {
                ldm.removeObserver(this);

                if (measurement == null) {
                    return;
                }

                mBinding.mv0.setText(Converter.convertIntegerMeasurement(measurement.getGlucoseStart()));
                mBinding.mv15.setText(Converter.convertIntegerMeasurement(measurement.getGlucose15()));
                mBinding.mv30.setText(Converter.convertIntegerMeasurement(measurement.getGlucose30()));
                mBinding.mv45.setText(Converter.convertIntegerMeasurement(measurement.getGlucose45()));
                mBinding.mv60.setText(Converter.convertIntegerMeasurement(measurement.getGlucose60()));
                mBinding.mv75.setText(Converter.convertIntegerMeasurement(measurement.getGlucose75()));
                mBinding.mv90.setText(Converter.convertIntegerMeasurement(measurement.getGlucose90()));
                mBinding.mv105.setText(Converter.convertIntegerMeasurement(measurement.getGlucose105()));
                mBinding.mv120.setText(Converter.convertIntegerMeasurement(measurement.getGlucose120()));
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.update_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Takes the new input from the user and updates the measurement object.
     */
    private void save() {
        // Load measurement object from database
        final LiveData<Measurement> ldm = mViewModel.getMeasurementById(mMeasurementId);
        ldm.observe(getViewLifecycleOwner(), new Observer<Measurement>() {
            @Override
            public void onChanged(Measurement measurement) {
                ldm.removeObserver(this);

                if (mBinding.mv0.getText() != null && !mBinding.mv0.getText().toString().equals("")) {
                    measurement.setGlucoseStart(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv0.getText()).toString()));
                }

                if (mBinding.mv15.getText() != null && !mBinding.mv15.getText().toString().equals("")) {
                    measurement.setGlucose15(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv15.getText()).toString()));
                }

                if (mBinding.mv30.getText() != null && !mBinding.mv30.getText().toString().equals("")) {
                    measurement.setGlucose30(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv30.getText()).toString()));
                }

                if (mBinding.mv45.getText() != null && !mBinding.mv45.getText().toString().equals("")) {
                    measurement.setGlucose45(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv45.getText()).toString()));
                }

                if (mBinding.mv60.getText() != null && !mBinding.mv60.getText().toString().equals("")) {
                    measurement.setGlucose60(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv60.getText()).toString()));
                }

                if (mBinding.mv75.getText() != null && !mBinding.mv75.getText().toString().equals("")) {
                    measurement.setGlucose75(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv75.getText()).toString()));
                }

                if (mBinding.mv90.getText() != null && !mBinding.mv90.getText().toString().equals("")) {
                    measurement.setGlucose90(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv90.getText()).toString()));
                }

                if (mBinding.mv105.getText() != null && !mBinding.mv105.getText().toString().equals("")) {
                    measurement.setGlucose105(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv105.getText()).toString()));
                }

                if (mBinding.mv120.getText() != null && !mBinding.mv120.getText().toString().equals("")) {
                    measurement.setGlucose120(
                            Integer.parseInt(Objects.requireNonNull(mBinding.mv120.getText()).toString()));
                }

                mViewModel.updateMeasurement(measurement);
                snackBar();
            }
        });
    }

    /**
     * Helper function for faster SnackBar creation
     */
    private void snackBar() {
        MySnackBar.createSnackBar(Objects.requireNonNull(getContext()), Objects.requireNonNull(getView()), "Measurement updated successfully!");
    }
}
