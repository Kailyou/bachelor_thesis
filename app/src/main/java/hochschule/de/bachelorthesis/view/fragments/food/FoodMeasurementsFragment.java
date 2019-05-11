package hochschule.de.bachelorthesis.view.fragments.food;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodMeasurmentsObserver;
import hochschule.de.bachelorthesis.utility.MyToast;

public class FoodMeasurementsFragment extends Fragment {

    private static final String TAG = FoodMeasurementsFragment.class.getName();

    private int mFoodId;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(new FragmentFoodMeasurmentsObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_measurements,container, false);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Add button pressed");
            }
        });

        mFoodId = getArguments().getInt("food_id");
        MyToast.createToast(getContext(), "" + mFoodId);

        return rootView;
    }
}
