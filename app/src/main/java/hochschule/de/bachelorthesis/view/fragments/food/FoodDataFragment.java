package hochschule.de.bachelorthesis.view.fragments.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodDataObserver;
import hochschule.de.bachelorthesis.utility.MyToast;

public class FoodDataFragment extends Fragment {

    private int mFoodId;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(new FragmentFoodDataObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mFoodId = getArguments().getInt("food_id");
        MyToast.createToast(getContext(), "" + mFoodId);

        return inflater.inflate(R.layout.fragment_food_data,container, false);
    }
}
