package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

public class HomeViewModel extends AndroidViewModel {

  private Repository mRepository;

  public HomeViewModel(@NonNull Application application) {
    super(application);
    mRepository = new Repository(application);
  }
}
