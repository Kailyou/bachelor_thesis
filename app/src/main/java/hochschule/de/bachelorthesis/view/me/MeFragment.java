package hochschule.de.bachelorthesis.view.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.Converter;
import hochschule.de.bachelorthesis.utility.MySnackBar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.viewmodels.MeViewModel;
import hochschule.de.bachelorthesis.databinding.FragmentMeBinding;

/**
 * View class to display the user data.
 * <p>
 * The data will be loaded from data base.
 */
public class MeFragment extends Fragment {

    private MeViewModel mViewModel;

    private FragmentMeBinding mBinding;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change title
        Objects.requireNonNull(getActivity()).setTitle("Edit user Data");

        // Enable menu
        setHasOptionsMenu(true);

        // View model
        mViewModel = ViewModelProviders.of(getActivity()).get(MeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Init data binding
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_me, container, false);
        mBinding.setLifecycleOwner(this);

        mBinding.buttonEditMe.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_meFragment_to_meEditFragment));

        final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
        ldu.observe(this, new Observer<UserHistory>() {
            @Override
            public void onChanged(UserHistory uh) {
                if (uh == null) {
                    return;
                }

                /* Update text views */

                // Personal data
                mBinding.age.setText(String.valueOf(uh.getAge()));
                mBinding.height.setText(String.valueOf(uh.getHeight()));
                mBinding.weight.setText(String.valueOf(uh.getWeight()));
                mBinding.sex.setText(uh.getSex());

                // Lifestyle
                mBinding.fitnessLevel.setText(uh.getFitness_level());
                mBinding.medication.setText(Converter.convertBoolean(getContext(), uh.getMedication()));
                mBinding.allergies.setText(Converter.convertBoolean(getContext(), uh.getAllergies()));
                mBinding.smoking.setText(Converter.convertBoolean(getContext(), uh.getSmoking()));
                mBinding.diabetes.setText(Converter.convertBoolean(getContext(), uh.getDiabetes()));
            }
        });

        return mBinding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.me_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_tmp_user) {
            addTemplateUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Adding a temp userHistory object to the database.
     */
    private void addTemplateUser() {
        final UserHistory newUh = new UserHistory(29, 173, 87, "male", "low", false, false, false,
                false);

        final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
        ldu.observe(getViewLifecycleOwner(), new Observer<UserHistory>() {
            @Override
            public void onChanged(UserHistory userHistory) {
                ldu.removeObserver(this);

                //Insert
                if (newUh.equals(userHistory)) {
                    snackBar("You cannot update user data if nothing changed. Change data and try again.");
                } else {
                    mViewModel.insertUserHistory(newUh);
                    snackBar("Template user data added successfully!");
                }
            }
        });
    }

    /**
     * Helper function for faster SnackBar creation
     *
     * @param msg The message to display in the SnackBar
     */
    private void snackBar(String msg) {
        MySnackBar.createSnackBar(Objects.requireNonNull(getContext()), Objects.requireNonNull(getView()), msg);
    }
}
