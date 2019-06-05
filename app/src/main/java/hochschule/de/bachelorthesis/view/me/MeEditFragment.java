package hochschule.de.bachelorthesis.view.me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import java.util.Objects;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentMeEditBinding;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
import hochschule.de.bachelorthesis.utility.MyToast;
import hochschule.de.bachelorthesis.viewmodels.MeViewModel;

/**
 * @author Maik T.
 *
 * This class handles the User edit feature.
 *
 * The user will be able to update his user data by filling out the views and pressing the save
 * button on the toolbar.
 *
 * After that, there will be a new user history object created, which will be passed to the
 * database.
 *
 * The user is not able to delete old user histories since he does not know about them.
 */
public class MeEditFragment extends Fragment {

  private MeViewModel mViewModel;
  private FragmentMeEditBinding mBinding;

  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Enable menu
    setHasOptionsMenu(true);

    // View model
    mViewModel =
        ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MeViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me_edit, container, false);
    mBinding.setLifecycleOwner(getViewLifecycleOwner());
    mBinding.setVm(mViewModel);

    // dropdown
    mBinding.dropdownSex
        .setAdapter(getAdapter(getResources().getStringArray(R.array.fragment_me_spinner_sex)));

    // gets the current value and updates the view
    mViewModel.getSex().observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        mBinding.dropdownSex
            .setText(s, false);
      }
    });

    mBinding.dropdownFitnessLevel.setAdapter(
        getAdapter(getResources().getStringArray(R.array.fragment_me_spinner_fitness_level)));

    // gets the current value and updates the view
    mViewModel.getFitnessLevel().observe(this, new Observer<String>() {
      @Override
      public void onChanged(String s) {
        mBinding.dropdownFitnessLevel
            .setText(s, false);
      }
    });

    return mBinding.getRoot();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.me_edit_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.save_me) {
      save();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * If the user input is okay, save the data by passing the new user history data to the database
   * VM will update itself.
   */
  private void save() {
    if (inPutOkay()) {
      int age = Integer.parseInt(Objects.requireNonNull(mBinding.age.getText()).toString());
      int height = Integer.parseInt(Objects.requireNonNull(mBinding.height.getText()).toString());
      int weight = Integer.parseInt(Objects.requireNonNull(mBinding.weight.getText()).toString());
      String sex = mBinding.dropdownSex.getText().toString();
      String fitnessLevel = mBinding.dropdownFitnessLevel.getText().toString();
      boolean medication = mBinding.medication.isChecked();
      boolean allergies = mBinding.allergies.isChecked();
      boolean smoking = mBinding.smoking.isChecked();

      final UserHistory uh = new UserHistory(age, height, weight, sex, fitnessLevel, medication,
          allergies, smoking);

      final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
      ldu.observe(getViewLifecycleOwner(), new Observer<UserHistory>() {
        @Override
        public void onChanged(UserHistory userHistory) {
          ldu.removeObserver(this);

          // Only save the new one, if something changed
          if (!uh.equals(userHistory)) {
            mViewModel.insertUserHistory(uh);
          }
        }
      });

      // Navigate back to me fragment
      Navigation.findNavController(Objects.requireNonNull(getView()))
          .navigate(R.id.action_meEditFragment_to_meFragment);
    }
  }

  /**
   * Checks if the user input has been valid.
   *
   * @return returns true if the input was okay. returns false otherwise.
   */
  private boolean inPutOkay() {
    // checks the text fields
    if (mBinding.age.getText() == null || mBinding.age.getText().toString().equals("")) {
      toast("Please enter your age.");
      return false;
    }

    if (mBinding.height.getText() == null || mBinding.height.getText().toString().equals("")) {
      toast("Please enter your height.");
      return false;
    }

    if (mBinding.weight.getText() == null || mBinding.weight.getText().toString().equals("")) {
      toast("Please enter your weight.");
      return false;
    }

    // checks the drop down menus
    if (mBinding.dropdownSex.getText() == null || mBinding.dropdownSex.getText().toString()
        .equals("")) {
      toast("Please select the sex.");
      return false;
    }

    if (mBinding.dropdownFitnessLevel.getText() == null || mBinding.dropdownFitnessLevel.getText()
        .toString().equals("")) {
      toast("Please select the fitness level.");
      return false;
    }

    return true;
  }

  /**
   * Faster toasts ;)
   *
   * @param msg - Message to deliver!
   */
  private void toast(String msg) {
    MyToast.createToast(getContext(), msg);
  }

  /**
   * Helper function which will an ArrayAdapter object
   *
   * @param elements - The String array to build the adapter with
   * @return - Returns an ArrayAdapter of the given Strings.
   */
  private ArrayAdapter<String> getAdapter(String[] elements) {
    return new ArrayAdapter<>(Objects.requireNonNull(getContext()),
        R.layout.dropdown_menu_popup_item, elements);
  }
}
