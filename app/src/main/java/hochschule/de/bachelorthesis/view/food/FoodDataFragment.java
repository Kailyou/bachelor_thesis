package hochschule.de.bachelorthesis.view.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import androidx.navigation.Navigation;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.databinding.FragmentFoodDataBinding;
import hochschule.de.bachelorthesis.viewmodels.FoodViewModel;

public class FoodDataFragment extends Fragment {

  private static final String TAG = FoodDataFragment.class.getName();

  private FoodViewModel mViewModel;


  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // view model
    mViewModel = ViewModelProviders.of(getActivity()).get(FoodViewModel.class);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // Init data binding
    FragmentFoodDataBinding binding = DataBindingUtil
        .inflate(inflater, R.layout.fragment_food_data, container, false);
    binding.setLifecycleOwner(getViewLifecycleOwner());
    binding.setVm(mViewModel);

    // Fab
    binding.fab.setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.action_meFragment_to_meEditFragment));
    return binding.getRoot();
  }
}
