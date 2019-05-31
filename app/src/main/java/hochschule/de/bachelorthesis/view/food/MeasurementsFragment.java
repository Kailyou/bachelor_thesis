package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementsBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.AdapterMeasurements;
import hochschule.de.bachelorthesis.viewmodels.FoodInfoViewModel;

public class MeasurementsFragment extends Fragment {

    private static final String TAG = MeasurementsFragment.class.getName();

    private FoodInfoViewModel mViewModel;

    private int mFoodId;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        FragmentMeasurementsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_measurements, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // View model
        mViewModel = ViewModelProviders.of(getActivity()).get(FoodInfoViewModel.class);

        // RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);

        // Adapter
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment_host);
        final AdapterMeasurements adapter = new AdapterMeasurements(getContext(), navController, mFoodId);
        recyclerView.setAdapter(adapter);

        // get passed food id
        assert getArguments() != null;
        mFoodId = getArguments().getInt("food_id");

        // loading the measurement entries
        mViewModel.getAllMeasurementsById(mFoodId).observe(this, new Observer<List<Measurement>>() {
            @Override
            public void onChanged(List<Measurement> measurements) {
                Log.d(TAG, "onChanged: ist drin " + measurements.toString());
                adapter.setMeasurements(measurements);
            }
        });

        Log.d("yolo", "onCreateView: " + mFoodId);

        // fab
        Bundle bundle = new Bundle();
        bundle.putInt("food_id", mFoodId);
        binding.add.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_foodInfoFragment_to_addMeasurement, bundle));

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
            case R.id.add_tmp_measurement:
                mViewModel.addTemplateMeasurement(this, mFoodId);
                return true;
            case R.id.delete_measurements:
                mViewModel.deleteAllMeasurementFromFoodWithId(mFoodId);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
