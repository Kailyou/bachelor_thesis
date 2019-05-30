package hochschule.de.bachelorthesis.view.fragments.mainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        mViewModel.load(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Init data binding
        FragmentMeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setVm(mViewModel);

        binding.buttonEditMe.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_meFragment_to_meEditFragment));

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.me_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_tmp_user:
                mViewModel.addTemplateUser();
                return true;
            case R.id.delete_user:
                mViewModel.deleteAllUserHistoryIds();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
