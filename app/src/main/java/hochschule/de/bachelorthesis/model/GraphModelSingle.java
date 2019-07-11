package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

public class GraphModelSingle {

  private MutableLiveData<String> mSelectedFood;

  public GraphModelSingle() {
    mSelectedFood = new MutableLiveData<>();
  }

  /* GETTER */

  public MutableLiveData<String> getSelectedFood() {
    return mSelectedFood;
  }

  /* SETTER */

  public void setSelectedFood(String selectedFood) {
    mSelectedFood.setValue(selectedFood);
  }
}
