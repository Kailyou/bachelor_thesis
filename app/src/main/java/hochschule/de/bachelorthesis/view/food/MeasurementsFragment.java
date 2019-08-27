package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.utility.Samples;

import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementsBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.adapter.AdapterMeasurements;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

public class MeasurementsFragment extends Fragment {

    private AdapterMeasurements mAdapter;

    private FoodViewModel mViewModel;

    private int mFoodId;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View model
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(FoodViewModel.class);

        setHasOptionsMenu(true);

        // get passed food id
        assert getArguments() != null;
        mFoodId = getArguments().getInt("food_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Init data binding
        FragmentMeasurementsBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_measurements, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(mViewModel);

        // RecyclerView
        final RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);


        // loading the measurement entries
        mViewModel.getAllMeasurementsById(mFoodId).observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
            @Override
            public void onChanged(List<Measurement> measurements) {

                // Should never happen
                if (measurements == null) {
                    return;
                }

                NavController navController = Navigation
                        .findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment_host);

                mAdapter = new AdapterMeasurements(getContext(), navController);
                recyclerView.setAdapter(mAdapter);

                // Add a header element and set it to the start on the list,
                // so the adapter can use index 0 and build a header line with.
                Measurement header = Samples.getEmptyMesurement();
                measurements.add(0, header);
                mAdapter.setMeasurements(measurements);
            }
        });


        // Load the reference food to calculate the GI, so it can be passed to the controller and
        // displayed in the list
        mViewModel.getFoodByFoodNameAndBrandName("Glucose", "Reference Product").observe(getViewLifecycleOwner(), new Observer<Food>() {
            @Override
            public void onChanged(Food refFood) {

                // Leave if there is no REF food
                if (refFood == null) {
                    return;
                }

                // Get all measurements for the reference food
                mViewModel.getAllMeasurementsById(refFood.id).observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
                    @Override
                    public void onChanged(List<Measurement> refMeasurements) {

                        // Leave if there are no REF food measurements.
                        if (refMeasurements == null) {
                            return;
                        }

                        mAdapter.setRefMeasurements(refMeasurements);
                    }
                });
            }
        });

        // fab
        Bundle bundle = new Bundle();
        bundle.putInt("food_id", mFoodId);
        binding.add.setOnClickListener(Navigation
                .createNavigateOnClickListener(R.id.action_foodInfoFragment_to_addMeasurement, bundle));

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.measurements_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_tmp_measurement_unfinished:
                createTemplateMeasurement(false);
                return true;
            case R.id.add_tmp_measurement_finished:
                createTemplateMeasurement(true);
                return true;
            case R.id.delete_measurements:
                deleteMeasurements();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates a random measurement, either a unfinished or an unfinished one, depending on the user's
     * selection.
     *
     * @param finished - Measurement unfinished or not
     */
    private void createTemplateMeasurement(final boolean finished) {

        // Load all measurements to check later if all has been finished
        final LiveData<List<Measurement>> ldMeasurements = mViewModel.getAllMeasurements();
        ldMeasurements.observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
            @Override
            public void onChanged(List<Measurement> allMeasurements) {
                ldMeasurements.removeObserver(this);

                // Only insert if no other measurement is active right now
                for (Measurement m : allMeasurements) {
                    if (m.isActive()) {
                        snackBar("Cannot add measurement because another one is still active!");

                        return;
                    }
                }

                // Load user data
                final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
                ldu.observe(getViewLifecycleOwner(), new Observer<UserHistory>() {
                    @Override
                    public void onChanged(final UserHistory userHistory) {
                        ldu.removeObserver(this);

                        // Leave if no user data has been set yet
                        if (userHistory == null) {
                            snackBar("Enter user data first!");
                            return;
                        }

                        // Load the food
                        final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);
                        ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
                            @Override
                            public void onChanged(Food food) {
                                ldf.removeObserver(this);

                                Measurement templateMeasurement;

                                // Create either an unfinished or a finished measurement
                                // If food is ref product use another sample to get higher values
                                if (finished) {
                                    if (food.getFoodName().equals("Glucose")) {
                                        templateMeasurement = Samples.getRandomGlucoseMeasurement(Objects.requireNonNull(getContext()), mFoodId, userHistory.id);
                                    } else {
                                        templateMeasurement = Samples.getRandomMeasurement(
                                                Objects.requireNonNull(getContext()), mFoodId, userHistory.id);
                                    }
                                } else {
                                    templateMeasurement = Samples
                                            .getRandomMeasurementUnfinished(Objects.requireNonNull(getContext()), mFoodId,
                                                    userHistory.id);
                                }

                                mViewModel.insertMeasurement(templateMeasurement);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Deletes all current measurements for the food.
     */
    private void deleteMeasurements() {
        final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);
        ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                ldf.removeObserver(this);

                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("Delete Confirmation")
                        .setMessage(
                                "You are about to delete this measurement.\n\nIt cannot be restored at a later time!\n\nContinue?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes, new OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (whichButton == -1) {
                                    mViewModel.deleteAllMeasurementFromFoodWithId(mFoodId);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
    }

    /**
     * Helper function for faster SnackBar creation
     *
     * @param msg The message to display in the SnackBar
     */
    private void snackBar(String msg) {
        MySnackBar.createSnackBar(Objects.requireNonNull(getContext()), Objects.requireNonNull(getView()), msg);
    }
}

