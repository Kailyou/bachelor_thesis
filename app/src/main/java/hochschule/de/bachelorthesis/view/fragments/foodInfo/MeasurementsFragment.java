package hochschule.de.bachelorthesis.view.fragments.foodInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeasurementsBinding;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import hochschule.de.bachelorthesis.utility.AdapterMeasurements;
import hochschule.de.bachelorthesis.view_model.viewModels.MeasurementsViewModel;
import hochschule.de.bachelorthesis.widget.BetterFloatingActionButton;

public class MeasurementsFragment extends Fragment {

    private static final String TAG = MeasurementsFragment.class.getName();

    private BetterFloatingActionButton mFab;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        FragmentMeasurementsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_measurements, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // View model
        MeasurementsViewModel viewModel = ViewModelProviders.of(this).get(MeasurementsViewModel.class);

        mFab = binding.add;

        // RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);

        // Adapter
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment_host);
        final AdapterMeasurements adapter = new AdapterMeasurements(getContext(), navController);
        recyclerView.setAdapter(adapter);

        // TODO
        // change to getAllMeasurementyById
        viewModel.getAllMeasurements().observe(this, new Observer<List<Measurement>>() {
            @Override
            public void onChanged(List<Measurement> measurements) {
                adapter.setMeasurements(measurements);
            }
        });

        // get passed food id
        assert getArguments() != null;
        int foodId = getArguments().getInt("food_id");

        // fab
        Bundle bundle = new Bundle();
        bundle.putInt("food_id", foodId);
        binding.add.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_foodInfoFragment_to_addMeasurement, bundle));

        return binding.getRoot();
    }
}
