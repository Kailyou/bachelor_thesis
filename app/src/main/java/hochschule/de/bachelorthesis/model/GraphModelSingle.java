package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

public class GraphModelSingle {

  private MutableLiveData<String> mSelectedFood;
  private MutableLiveData<Boolean> mAverage;
  private MutableLiveData<Boolean> mMedian;

  public GraphModelSingle() {
    mSelectedFood = new MutableLiveData<>();
  }

  /* GETTER */

  public MutableLiveData<String> getSelectedFood() {
    return mSelectedFood;
  }

  public MutableLiveData<Boolean> getAverage() {
    return mAverage;
  }

  public MutableLiveData<Boolean> getMedian() {
    return mMedian;
  }

  /* SETTER */

  public void setSelectedFood(String selectedFood) {
    mSelectedFood.setValue(selectedFood);
  }

  public void setAverage(Boolean average) {
    mAverage.setValue(average);
  }

  public void setMedian(Boolean median) {
    mMedian.setValue(median);
  }
}
