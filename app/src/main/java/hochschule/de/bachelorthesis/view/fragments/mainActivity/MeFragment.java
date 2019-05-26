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
import androidx.databinding.BaseObservable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.view_model.fragments.MeViewModel;
import hochschule.de.bachelorthesis.databinding.FragmentMeBinding;

public class MeFragment extends Fragment {
    private MeViewModel mViewModel;
    private FragmentMeBinding mBinding;
    BaseObservable x;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change title
        Objects.requireNonNull(getActivity()).setTitle("Edit user Data");

        // View model
        mViewModel = ViewModelProviders.of(getActivity()).get(MeViewModel.class);
        mViewModel.load(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setVm(mViewModel);

        mBinding.buttonEditMe.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_main_activity_me_fragment_to_meEditFragment));


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

    }

    /**
     * Updates the view model.
     */
    private void updateViewModel() {
        /*
        Integer age = Integer.parseInt(Objects.requireNonNull(mBinding.age.getText()).toString());
        Integer height = Integer.parseInt(Objects.requireNonNull(mBinding.height.getText()).toString());
        Integer weight = Integer.parseInt(Objects.requireNonNull(mBinding.weight.getText()).toString());
        String sex = mBinding.dropdownSex.getText().toString();
        String fitnessLevel = mBinding.dropdownFitnessLevel.getText().toString();
        boolean medication = false;
        boolean allergies = false;
        boolean smoking = false;

        // Checkboxes
        if(mBinding.medication.isChecked()) {
            medication = true;
        }

        if(mBinding.allergies.isChecked()) {
            allergies = true;
        }

        if(mBinding.smoking.isChecked()) {
            smoking = true;
        }

        mViewModel.update(age, height, weight, sex, fitnessLevel, medication, allergies, smoking);
        */
    }

    private void toast(String msg) {
        MyToast.createToast(getContext(), msg);
    }
}
