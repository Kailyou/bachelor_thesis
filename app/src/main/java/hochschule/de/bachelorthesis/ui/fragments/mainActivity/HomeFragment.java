package hochschule.de.bachelorthesis.ui.fragments.mainActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodObserver;
import hochschule.de.bachelorthesis.lifecycle.FragmentHomeObserver;

public class HomeFragment extends Fragment {

    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Home");
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(new FragmentHomeObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}

