package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LiveData;

import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.loadFromDb.FoodObject;
import hochschule.de.bachelorthesis.utility.Samples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodBinding;
import hochschule.de.bachelorthesis.adapter.AdapterFood;
import hochschule.de.bachelorthesis.room.tables.Food;
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
public class FoodsFragment extends Fragment {

    private FragmentFoodBinding mBinding;

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false);
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
        inflater.inflate(R.menu.food_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                sort();
                return true;

            case R.id.add_apple:
                mViewModel.insertFood(Samples.getApple());
                return true;

            case R.id.add_coke:
                mViewModel.insertFood(Samples.getCoke());
                return true;

            case R.id.add_pizza:
                mViewModel.insertFood(Samples.getPizza());
                return true;

            case R.id.delete_foods:
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

        ArrayList<FoodObject> foodObects = new ArrayList<>();

        // LOAD ALL FOODS
        mViewModel.getAllFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(final List<Food> foods) {

                boolean refFoodFound = false;

                // Check if a REF food is existing
                for (Food f : foods) {
                    if (f.getBrandName().equals("Reference Food")) {
                        refFoodFound = true;
                    }
                }

                // LOAD ALL MEASUREMENTS FOR

                // If a ref food exists, GI calculation will be done before everything can
                // be passed to the adapter.
                if (refFoodFound) {

                    final ArrayList<FoodObject> allFoodObjects = new ArrayList<>();

                    // LOAD ALL MEASUREMENTS FROM ALL FOODS FOR GI CALCULATION
                    for (final Food f : foods) {
                        final LiveData<List<Measurement>> ldm = mViewModel.getAllMeasurementsById(f.id);
                        ldm.observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
                            @Override
                            public void onChanged(List<Measurement> measurements) {
                                ldm.removeObserver(this);

                                /*
                                FoodObject tmp = new FoodObject(f.getFoodName(), f.getBrandName(),
                                        measurements, 0);

                                allFoodObjects.add(tmp);
                                */
                            }
                        });
                    }

                    // If the amount of elements in the food object list is equal to the
                    // amount of foods loaded, every object has been saved
                    if (allFoodObjects.size() == foods.size()) {

                    }

                } else {
                    // sort the list and
                    sortFoodList("alphanumeric", foods);
                    // Add a header element and set it to the start on the list,
                    // so the adapter can use index 0 and build a header line with.
                    Food header = Samples.getEmptyFood();
                    foods.add(0, header);
                    // Pass only the food list to the adapter. The adapter class will need
                    // to check if the others are null.
                    mAdapter.setFoods(foods);
                }
            }
        });
    }

    /**
     * Will sort the food list alphanumeric
     */
    private void sort() {
        final LiveData<List<Food>> ldf = mViewModel.getAllFoods();
        ldf.observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                ldf.removeObserver(this);
                sortFoodList("alphanumeric", foods);
            }
        });
    }

    /**
     * Create a comparator depending on how to sort the list, sort the list and pass the list to the
     * adapter, which will cause the list to be visible.
     *
     * @param sortType How to sort the list
     * @param foods    The food list
     */
    private void sortFoodList(String sortType, List<Food> foods) {
        Comparator<Food> comparator = null;

        // Create a comparator depending of the given parameter
        if (sortType.equals("alphanumeric")) {
            comparator = new Comparator<Food>() {
                @Override
                public int compare(Food food1, Food food2) {
                    return String.CASE_INSENSITIVE_ORDER.compare(food1.getFoodName(), food2.getFoodName());
                }
            };
        }

        if (comparator != null) {
            Collections.sort(foods, comparator);
        }
    }
}

