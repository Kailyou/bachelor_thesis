package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.adapter.AdapterFood;
import hochschule.de.bachelorthesis.databinding.FragmentFoodListBinding;
import hochschule.de.bachelorthesis.enums.SortType;
import hochschule.de.bachelorthesis.loadFromDb.FoodObject;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.MySnackBar;
import hochschule.de.bachelorthesis.utility.Samples;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

/**
 * @author thielenm
 * <p>
 * This class contains the logic for the food list.
 * <p>
 * All foods will be loaded from the databased and displayed in a recycler view.
 * <p>
 * The list will be sorted automatically alphanumeric by default.
 * <p>
 * The user also can add a new food by pressing the FAB button and enter the following formular.
 * <p>
 * Also, the user can delete all measurements by clicking the "delte all measurement" setting
 * button.
 * <p>
 * For debug reasons, it is currently possible to add three different foods by setting buttons.
 */
public class FoodListFragment extends Fragment {

    private FragmentFoodListBinding mBinding;

    private ArrayList<FoodObject> mFoodObjects = new ArrayList<>();

    private FoodViewModel mViewModel;

    private AdapterFood mAdapter;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View model
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(FoodViewModel.class);

        // Enable menu
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_list, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        // Adapter
        NavController mNavController = Navigation
                .findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment_host);

        mAdapter = new AdapterFood(getContext(), mNavController);

        // RecyclerView
        RecyclerView recyclerView = mBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        handleListeners();

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.food_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.sort_alphanumeric:
                mViewModel.updateFoodListModel(SortType.ALPHANUMERIC);
                sortFoodObjectsAndPassResultToAdapter(mViewModel.getFoodListModel().getSortType(), mFoodObjects);
                snackBar("List sorted alphanumeric successfully!");
                return true;

            case R.id.sort_gi:
                mViewModel.updateFoodListModel(SortType.GI);
                sortFoodObjectsAndPassResultToAdapter(mViewModel.getFoodListModel().getSortType(), mFoodObjects);
                snackBar("List sorted for GI successfully!");
                return true;

            case R.id.add_apple:
                mViewModel.insertFood(Samples.getApple());
                snackBar("Added Apple successfully!");
                return true;

            case R.id.add_coke:
                mViewModel.insertFood(Samples.getCoke());
                snackBar("Added Coke successfully!");
                return true;

            case R.id.add_pizza:
                mViewModel.insertFood(Samples.getPizza());
                snackBar("Added Pizza successfully!");
                return true;

            case R.id.add_glucose:
                mViewModel.insertFood(Samples.getGlucose());
                snackBar("Added Glucose successfully!");
                return true;

            case R.id.delete_foods:
                deleteFoods();
                snackBar("Deleted all foods successfully!");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleListeners() {
        // floating action button
        mBinding.buttonAddNote.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_foodFragment_to_addFood));

        loadDataForAdapter();
    }

    private void loadDataForAdapter() {

        // LOAD ALL FOODS
        mViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(final List<Food> foods) {

                mFoodObjects.clear();

                if (foods == null || foods.size() == 0) {
                    mAdapter.setFoodObjects(mFoodObjects);
                    return;
                }

                // LOAD ALL MEASUREMENTS FOR FOODS
                for (final Food f : foods) {
                    final LiveData<List<Measurement>> ldf = mViewModel.getAllMeasurementsById(f.id);
                    ldf.observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
                        @Override
                        public void onChanged(List<Measurement> measurements) {
                            ldf.removeObserver(this);
                            {
                                mFoodObjects.add(new FoodObject(f, measurements));

                                if (mFoodObjects.size() == foods.size()) {

                                    // Add a header element and set it to the start on the list,
                                    // so the adapter can use index 0 and build a header line with.
                                    mFoodObjects.add(0, new FoodObject(Samples.getEmptyFood(), null));

                                    // LOAD REF PRODUCT
                                    final LiveData<Food> ldf2 = mViewModel.getFoodByFoodNameAndBrandName("Glucose", "Reference Product");
                                    ldf2.observe(getViewLifecycleOwner(), new Observer<Food>() {
                                        @Override
                                        public void onChanged(Food refFood) {
                                            ldf2.removeObserver(this);

                                            if (refFood == null) {
                                                sortFoodObjectsAndPassResultToAdapter(mViewModel.getFoodListModel().getSortType(), mFoodObjects);
                                                return;
                                            }

                                            // LOAD ALL MEASUREMENTS
                                            final LiveData<List<Measurement>> ldlm = mViewModel.getAllMeasurementsById(refFood.id);
                                            ldlm.observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
                                                @Override
                                                public void onChanged(List<Measurement> refMeasurements) {

                                                    if (refMeasurements.size() == 0) {
                                                        sortFoodObjectsAndPassResultToAdapter(mViewModel.getFoodListModel().getSortType(), mFoodObjects);
                                                        return;
                                                    }

                                                    for (FoodObject fo : mFoodObjects) {
                                                        fo.setRefAllMeasurements(refMeasurements);
                                                    }

                                                    sortFoodObjectsAndPassResultToAdapter(mViewModel.getFoodListModel().getSortType(), mFoodObjects);
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    private void deleteFoods() {
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle("Delete Confirmation")
                .setMessage(
                        "You are about to delete this measurement." +
                                "\n\nIt cannot be restored at a later time!" +
                                "\n\nContinue?")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (whichButton == -1) {
                            mViewModel.deleteAllFoods();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * Create a comparator depending on how to sort the list, sort the list and pass the list to the
     * adapter, which will cause the list to be visible.
     *
     * @param sortType    How to sort the list
     * @param foodObjects Food object list
     */
    private void sortFoodObjectsAndPassResultToAdapter(SortType sortType, List<FoodObject> foodObjects) {
        Comparator<FoodObject> comparator;

        // Create a comparator depending of the given parameter
        switch (sortType) {
            case ALPHANUMERIC:
                comparator = new Comparator<FoodObject>() {
                    @Override
                    public int compare(FoodObject fo1, FoodObject fo2) {
                        return String.CASE_INSENSITIVE_ORDER.compare(fo1.getFood().getFoodName(), fo2.getFood().getFoodName());
                    }
                };
                break;

            case GI:
                comparator = new Comparator<FoodObject>() {
                    @Override
                    public int compare(FoodObject fo1, FoodObject fo2) {
                        return fo1.getGi().compareTo(fo2.getGi());
                    }
                };
                break;

            default:
                throw new IllegalStateException("Unexpected state at list sorting!");
        }

        if (foodObjects == null || foodObjects.size() == 0) {
            return;
        }

        // Remove the first element (header), sort the list and put the header back in
        FoodObject tmp = foodObjects.get(0);
        foodObjects.remove(0);

        Collections.sort(foodObjects, comparator);

        foodObjects.add(0, tmp);

        mAdapter.setFoodObjects(foodObjects);
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

