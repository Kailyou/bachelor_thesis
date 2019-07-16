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
        /* Update text views */

        // Personal data
        mBinding.age.setText(Converter.convertInteger(uh.getAge()));
        mBinding.height.setText(Converter.convertInteger(uh.getHeight()));
        mBinding.weight.setText(Converter.convertInteger(uh.getWeight()));
        mBinding.sex.setText(uh.getSex());

        // Lifestyle
        mBinding.fitnessLevel.setText(uh.getFitness_level());
        mBinding.medication.setText(Converter.convertBoolean(uh.getMedication()));
        mBinding.allergies.setText(Converter.convertBoolean(uh.getAllergies()));
        mBinding.smoking.setText(Converter.convertBoolean(uh.getSmoking()));
        mBinding.diabetes.setText(Converter.convertBoolean(uh.getDiabetes()));
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
   * Adding a temp userHistory object to the database. TODO is getUserHistoryLatest correct?!
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
        if (!newUh.equals(userHistory)) {
          mViewModel.insertUserHistory(newUh);
        }
      }
    });
  }
}
