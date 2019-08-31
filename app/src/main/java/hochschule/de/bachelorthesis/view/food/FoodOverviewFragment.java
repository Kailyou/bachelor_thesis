package hochschule.de.bachelorthesis.view.food;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.navigation.Navigation;

import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodOverviewBinding;
import hochschule.de.bachelorthesis.loadFromDb.FoodObject;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;


/**
 * @author Maik Thielen
 * <p>
 * Displays the overview about the loaded food object.
 * <p>
 * This fragment will appear after the user selected a food in the foods fragment.
 * <p>
 * The food object behind the selected food will be loaded from the database and the text views will
 * be updated to show the information.
 * <p>
 * TODO: Rating and Personal index
 */
public class FoodOverviewFragment extends Fragment {

    private FragmentFoodOverviewBinding mBinding;

    private FoodViewModel mViewModel;

    private int mFoodId;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // view model
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
                .get(FoodViewModel.class);

        assert getArguments() != null;
        mFoodId = getArguments().getInt("food_id");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Init data binding
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_food_overview, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        // get passed food id
        assert getArguments() != null;
        final int foodId = getArguments().getInt("food_id");

        // LOAD FOOD
        mViewModel.getFoodById(foodId).observe(getViewLifecycleOwner(), new Observer<Food>() {
            @Override
            public void onChanged(final Food food) {
                // Leave immediately if there is no food. Usually this should never happen.
                if (food == null) {
                    return;
                }

                // LOAD MEASUREMENTS FOR FOOD
                mViewModel.getAllMeasurementsById(food.id).observe(getViewLifecycleOwner(),
                        new Observer<List<Measurement>>() {
                            @Override
                            public void onChanged(final List<Measurement> measurements) {

                                // Remove unfinished measurements
                                Measurement.removeNotFinishedMeasurements(measurements);

                                if (measurements == null || measurements.size() == 0) {
                                    FoodObject fo = new FoodObject(food, null);
                                    updateTextViews(fo);
                                    return;
                                }


                                // LOAD REF FOOD
                                mViewModel.getFoodByFoodNameAndBrandName("Glucose", "Reference Product").observe(getViewLifecycleOwner(), new Observer<Food>() {
                                    @Override
                                    public void onChanged(final Food refFood) {

                                        // Leave if there is no ref food because there cannot be
                                        // a GI calculated without ref food.
                                        if (refFood == null) {
                                            FoodObject fo = new FoodObject(food, measurements);
                                            updateTextViews(fo);
                                            return;
                                        }

                                        // LOAD MEASUREMENTS FOR REF FOOD
                                        mViewModel.getAllMeasurementsById(refFood.id).observe(getViewLifecycleOwner(), new Observer<List<Measurement>>() {
                                            @Override
                                            public void onChanged(final List<Measurement> refMeasurements) {
                                                // Remove unfinished measurements
                                                Measurement.removeNotFinishedMeasurements(refMeasurements);

                                                if (refMeasurements == null || refMeasurements.size() == 0) {
                                                    FoodObject fo = new FoodObject(food, null);
                                                    updateTextViews(fo);
                                                    return;
                                                }

                                                FoodObject fo = new FoodObject(food, measurements);
                                                fo.setRefAllMeasurements(refMeasurements);
                                                updateTextViews(fo);
                                            }
                                        });
                                    }
                                });
                            }
                        });
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.food_overview_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {

            new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                    .setTitle("Delete Confirmation")
                    .setMessage(
                            "You are about to delete this food.\n\nIt cannot be restored at a later time!\n\nContinue?")
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(android.R.string.yes, new OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (whichButton == -1) {
                                delete();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the text views
     *
     * @param fo the food object, which contains all data for the food.
     */
    private void updateTextViews(FoodObject fo) {
        if (fo == null || fo.getFood() == null) {
            return;
        }

        // General
        mBinding.foodName.setText(fo.getFood().getFoodName());
        mBinding.brandName.setText(fo.getFood().getBrandName());
        mBinding.type.setText(fo.getFood().getFoodType());
        mBinding.kiloCalories.setText(Converter.convertFloat(fo.getFood().getKiloCalories()));

        // Measurements
        mBinding.amount.setText(String.valueOf(fo.getAmountMeasurement()));

        mBinding.glucoseMax.setText(String.valueOf(fo.getGlucoseMax()));

        mBinding.glucoseAverage.setText(String.valueOf(fo.getGlucoseAvg()));

        mBinding.glucoseIncreaseMax.setText(String.valueOf(fo.getGlucoseIncreaseMax()));

        mBinding.stdev.setText(String.valueOf(fo.getStandardDeviation()));

        mBinding.gi.setText(String.valueOf(fo.getGi()));

        // Set text color depending of the GI result
        if (fo.getGi() == 0)
            mBinding.gi.setTextColor(Color.BLACK);
        if (fo.getGi() < 55)
            mBinding.gi.setTextColor(getResources().getColor(R.color.gi_low));
        else if (fo.getGi() < 71)
            mBinding.gi.setTextColor(getResources().getColor(R.color.gi_mid));
        else
            mBinding.gi.setTextColor(getResources().getColor(R.color.gi_high));

        mBinding.gi.setTypeface(null, Typeface.BOLD);
    }

    /**
     * Loads and deletes the selected food from the database and navigate back.
     */
    private void delete() {
        // Load the food object which should be deleted
        final LiveData<Food> ldf = mViewModel.getFoodById(mFoodId);
        ldf.observe(getViewLifecycleOwner(), new Observer<Food>() {
            @Override
            public void onChanged(Food food) {
                ldf.removeObserver(this);
                mViewModel.deleteFood(food);

                // Navigate back to food fragment
                Navigation.findNavController(Objects.requireNonNull(getView()))
                        .navigate(R.id.action_foodInfoFragment_to_foodFragment);
            }
        });
    }
}
