package hochschule.de.bachelorthesis.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import hochschule.de.bachelorthesis.model.Repository;
import hochschule.de.bachelorthesis.room.tables.UserHistory;

/**
 * @author Maik Thielen
 * <p>
 * This ViewModel class contains the data for the me related classes.
 */
public class MeViewModel extends AndroidViewModel {

    private Repository mRepository;

    public MeViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    /**
     * Inserts a new user history to the database and update the viewModel.
     *
     * @param userHistory - The new userHistory object
     */
    public void insertUserHistory(UserHistory userHistory) {
        mRepository.insert(userHistory);
    }

    /*
     * GETTER
     */

    public LiveData<UserHistory> getUserHistoryLatest() {
        return mRepository.getUserHistoryLatest();
    }
}
