package hochschule.de.bachelorthesis.view.me;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import hochschule.de.bachelorthesis.room.tables.UserHistory;
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
    final FragmentMeBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_me, container, false);
    binding.setLifecycleOwner(this);
    binding.setVm(mViewModel);

    binding.buttonEditMe.setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.action_meFragment_to_meEditFragment));

    final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
    ldu.observe(this, new Observer<UserHistory>() {
      @Override
      public void onChanged(UserHistory userHistory) {
        mViewModel.load(userHistory);
        if(userHistory != null)
          Log.d("yolo", "id: " + userHistory.id);
      }
    });

    return binding.getRoot();
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
    final UserHistory newUh = new UserHistory(29, 173, 88, "male", "low", false, false, false);

    final LiveData<UserHistory> ldu = mViewModel.getUserHistoryLatest();
    ldu.observe(getViewLifecycleOwner(), new Observer<UserHistory>() {
      @Override
      public void onChanged(UserHistory userHistory) {
        ldu.removeObserver(this);

        //Insert
        if(!newUh.equals(userHistory))
          mViewModel.insertUserHistory(newUh);
        }
    });
  }
}
