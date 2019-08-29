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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.adapter.AdapterMeasurements;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementListBinding;
import hochschule.de.bachelorthesis.enums.SortType;
import hochschule.de.bachelorthesis.loadFromDb.MeasurementObject;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.enums.MeasurementType;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.utility.Samples;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

public class MeasurementListFragment extends Fragment {

    private AdapterMeasurements mAdapter;

    private FoodViewModel mViewModel;

    private int mFoodId;

    private List<MeasurementObject> mAllMeasurementObjects = new ArrayList<>();


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
        FragmentMeasurementListBinding mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_measurement_list, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        // RecyclerView
        final RecyclerView recyclerView = mBinding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);

        NavController navController = Navigation
                .findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment_host);

        mAdapter = new AdapterMeasurements(getContext(), navController);
        recyclerView.setAdapter(mAdapter);

        handleListeners();

        // fab
        Bundle bundle = new Bundle();
        bundle.putInt("food_id", mFoodId);
        mBinding.add.setOnClickListener(Navigation
                .createNavigateOnClickListener(R.id.action_foodInfoFragment_to_addMeasurement, bundle));

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.measurement_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_gi:
                mViewModel.updateFoodListModel(SortType.GI);
                sortMeasurementObjectsAndPassResultToAdapter(mViewModel.getFoodListModel().getSortType(), mAllMeasurementObjects);
                snackBar("List sorted for GI successfully!");
                return true;

            case R.id.sort_glucose_max:
                mViewModel.updateFoodListModel(SortType.GLUCOSE_MAX);
                sortMeasurementObjectsAndPassResultToAdapter(mViewModel.getFoodListModel().getSortType(), mAllMeasurementObjects);
                snackBar("List sorted for glucose max successfully!");
                return true;

            case R.id.add_unfinished_gi_measurement:
                createTemplateMeasurement(false, null);
                snackBar("Added unfinished measurement successfully!");
                return true;

            case R.id.add_low_gi_measurement:
                createTemplateMeasurement(true, MeasurementType.LOW);
                snackBar("Added low gi measurement successfully!");
                return true;

            case R.id.add_mid_gi_measurement:
                createTemplateMeasurement(true, MeasurementType.MID);
                snackBar("Added mid gi measurement successfully!");
                return true;

            case R.id.add_high_gi_measurement:
                createTemplateMeasurement(true, MeasurementType.HIGH);
                snackBar("Added high gi measurement successfully!");
                return true;

            case R.id.add_ref_gi_measurement:
                createTemplateMeasurement(true, MeasurementType.REF);
                snackBar("Added ref gi measurement successfully!");
                return true;

            case R.id.delete_measurements:
                deleteMeasurements();
                snackBar("Deleted all measurements successfully!");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleListeners() {
        // LOAD ALL MEASUREMENTS
        mViewModel.getAllMeasurementsById(mFoodId).observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
            @Override
            public void onChanged(final List<Measurement> measurements) {

                mAllMeasurementObjects.clear();

                // Add a header element and set it to the start on the list,
                // so the adapter can use index 0 and build a header line with.
                Measurement header = Samples.getEmptyMeasurement();
                measurements.add(0, header);

                // LOAD REF PRODUCT
                final LiveData<Food> ldf = mViewModel.getFoodByFoodNameAndBrandName("Glucose", "Reference Product");
                ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
                    @Override
                    public void onChanged(Food refFood) {
                        ldf.removeObserver(this);

                        if (refFood == null) {
                            for (Measurement m : measurements) {
                                mAllMeasurementObjects.add(new MeasurementObject(m, null));
                                sortMeasurementObjectsAndPassResultToAdapter(mViewModel.getMeasurementListModel().getSortType(), mAllMeasurementObjects);
                            }

                            return;
                        }

                        // LOAD REF MEASUREMENTS
                        final LiveData<List<Measurement>> ldm = mViewModel.getAllMeasurementsById(refFood.id);
                        ldm.observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
                            @Override
                            public void onChanged(List<Measurement> refMeasurements) {
                                ldm.removeObserver(this);

                                Measurement.removeNonGiMeasurements(refMeasurements);

                                if (refMeasurements.size() == 0) {
                                    for (Measurement m : measurements) {
                                        mAllMeasurementObjects.add(new MeasurementObject(m, null));
                                        sortMeasurementObjectsAndPassResultToAdapter(mViewModel.getMeasurementListModel().getSortType(), mAllMeasurementObjects);
                                    }

                                    return;
                                }

                                for (Measurement m : measurements) {
                                    mAllMeasurementObjects.add(new MeasurementObject(m, refMeasurements));
                                    sortMeasurementObjectsAndPassResultToAdapter(mViewModel.getMeasurementListModel().getSortType(), mAllMeasurementObjects);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Creates a random GI measurement
     *
     * @param finished - Measurement unfinished or not
     */
    private void createTemplateMeasurement(final boolean finished, final MeasurementType type) {

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
                                    templateMeasurement = Samples.getRandomMeasurement(
                                            Objects.requireNonNull(getContext()), mFoodId, userHistory.id, true, type);
                                } else {
                                    templateMeasurement = Samples
                                            .getRandomMeasurementUnfinished(Objects.requireNonNull(getContext()), mFoodId,
                                                    userHistory.id, true);
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
     * Create a comparator depending on how to sort the list, sort the list and pass the list to the
     * adapter, which will cause the list to be visible.
     *
     * @param sortType           How to sort the list
     * @param measurementObjects Measurement object list
     */
    private void sortMeasurementObjectsAndPassResultToAdapter(SortType sortType, List<MeasurementObject> measurementObjects) {
        Comparator<MeasurementObject> comparator;

        // Create a comparator depending of the given parameter
        switch (sortType) {
            case GLUCOSE_MAX:
                comparator = new Comparator<MeasurementObject>() {
                    @Override
                    public int compare(MeasurementObject mo1, MeasurementObject mo2) {
                        return mo1.getGlucoseMax().compareTo(mo2.getGlucoseMax());
                    }
                };
                break;

            case GI:
                comparator = new Comparator<MeasurementObject>() {
                    @Override
                    public int compare(MeasurementObject mo1, MeasurementObject mo2) {
                        return mo1.getGi().compareTo(mo2.getGi());
                    }
                };
                break;

            default:
                throw new IllegalStateException("Unexpected state at list sorting!");
        }

        if (measurementObjects == null || measurementObjects.size() == 0) {
            return;
        }

        // Remove the first element (header), sort the list and put the header back in
        MeasurementObject tmp = measurementObjects.get(0);
        measurementObjects.remove(0);

        Collections.sort(measurementObjects, comparator);

        measurementObjects.add(0, tmp);

        mAdapter.setMeasurementObjects(measurementObjects);
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

