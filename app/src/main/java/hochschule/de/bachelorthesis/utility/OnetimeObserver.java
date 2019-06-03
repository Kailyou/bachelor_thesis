package hochschule.de.bachelorthesis.utility;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class OnetimeObserver<T> implements Observer<T> {
  private final LiveData<T> mLiveData;

  public OnetimeObserver(LiveData<T> liveData) {
    mLiveData = liveData;
  }

  @Override
  public void onChanged(T t) {

  }
}
