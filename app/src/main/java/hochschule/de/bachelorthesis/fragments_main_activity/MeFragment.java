package hochschule.de.bachelorthesis.fragments_main_activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.lifecycle.FragmentFoodObserver;
import hochschule.de.bachelorthesis.lifecycle.FragmentMeObserver;

public class MeFragment extends Fragment {

    public void onCreate(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Me");
        super.onCreate(savedInstanceState);

        getLifecycle().addObserver(new FragmentMeObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }
}
