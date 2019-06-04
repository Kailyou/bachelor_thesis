package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

public class MeasurementAddModel {

  // General
  private MutableLiveData<Boolean> mIsGi;

  // Time & Advance information
  private MutableLiveData<String> mTimestamp;
  private MutableLiveData<Integer> mAmount;
  private MutableLiveData<String> mStressed;
  private MutableLiveData<String> mTired;

  // Measurement values
  private MutableLiveData<Integer> mValue0;

  public MeasurementAddModel() {
    mIsGi = new MutableLiveData<>();
    mTimestamp = new MutableLiveData<>();
    mAmount = new MutableLiveData<>();
    mTired = new MutableLiveData<>();
    mValue0 = new MutableLiveData<>();
  }

  /* GETTER */

  public MutableLiveData<Boolean> isGi() {
    return mIsGi;
  }

  public MutableLiveData<String> getTimestamp() {
    return mTimestamp;
  }

  public MutableLiveData<Integer> getAmount() {
    return mAmount;
  }

  public MutableLiveData<String> getStressed() {
    return mStressed;
  }

  public MutableLiveData<String> getTired() {
    return mTired;
  }

  public MutableLiveData<Integer> getValue0() {
    return mValue0;
  }

  /* SETTER */

  public void setGi(boolean isGi) {
    mIsGi.setValue(isGi);
  }

  public void setTimestamp(String timestamp) {
    mTimestamp.setValue(timestamp);
  }

  public void setAmount(Integer amount) {
    mAmount.setValue(amount);
  }

  public void setStressed(String stressed) {mStressed.setValue(stressed);}

  public void setTired(String tired) {
    mTired.setValue(tired);
  }

  public void setValue0(Integer value0) {
    mValue0.setValue(value0);
  }
}
