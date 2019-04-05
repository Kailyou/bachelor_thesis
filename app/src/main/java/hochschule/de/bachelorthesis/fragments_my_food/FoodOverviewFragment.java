package hochschule.de.bachelorthesis.fragments_my_food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodObserver;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodOverviewObserver;

public class FoodOverviewFragment extends Fragment {

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(new FragmentFoodOverviewObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_overview,container, false);
    }
}
