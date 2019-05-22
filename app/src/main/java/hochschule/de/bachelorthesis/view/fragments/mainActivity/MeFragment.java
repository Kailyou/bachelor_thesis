package hochschule.de.bachelorthesis.view.fragments.mainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.model.User;
import hochschule.de.bachelorthesis.utility.UserSample;
import hochschule.de.bachelorthesis.view_model.fragments.MeViewModel;
import hochschule.de.bachelorthesis.databinding.FragmentMeBinding;

public class MeFragment extends Fragment {
    private MeViewModel mViewModel;
    private FragmentMeBinding mBinding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change title
        Objects.requireNonNull(getActivity()).setTitle("Edit user Data");

        // View model
        mViewModel = ViewModelProviders.of(this).get(MeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        mBinding.buttonEditMe.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_main_activity_me_fragment_to_meEditFragment));

        loadUser();

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.me_menu, menu);
    }

    /**
     * This function is supposed to load the current user history data from the database.
     * For test reasons, it will now just set the data.
     * TODO implement database function
     */
    private void loadUser() {
        User user = UserSample.getTestUser1();
        mViewModel.setUserAge(String.valueOf(user.getAge()));
        mViewModel.setHeight(String.valueOf(user.getHeight()));
        mViewModel.setWeight(String.valueOf(user.getWeight()));
        mViewModel.setSex(user.getSex());
        mViewModel.setFitnessLevel(user.getFitnessLevel());
        mViewModel.setMedication(user.getMedication());
        mViewModel.setAllergies(user.getAllergies());
        mViewModel.setSmoking(user.getSmoking());
    }
}
