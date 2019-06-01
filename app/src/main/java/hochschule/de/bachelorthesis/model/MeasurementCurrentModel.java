package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

public class MeasurementCurrentModel {

  // General
  private boolean mIsDone;
  private MutableLiveData<Boolean> mIsGi;

  // Time & Advance information
  private MutableLiveData<String> mTimestamp;
  private MutableLiveData<Integer> mAmount;
  private MutableLiveData<String> mStressed;
  private MutableLiveData<String> mTired;

  // Measurement values
  private MutableLiveData<Integer> mValue0;
  private MutableLiveData<Integer> mValue15;
  private MutableLiveData<Integer> mValue30;
  private MutableLiveData<Integer> mValue45;
  private MutableLiveData<Integer> mValue60;
  private MutableLiveData<Integer> mValue75;
  private MutableLiveData<Integer> mValue90;
  private MutableLiveData<Integer> mValue105;
  private MutableLiveData<Integer> mValue120;


  public MeasurementCurrentModel() {
    mIsGi = new MutableLiveData<>();
    mTimestamp = new MutableLiveData<>();
    mAmount = new MutableLiveData<>();
    mStressed = new MutableLiveData<>();
    mTired = new MutableLiveData<>();
    mValue0 = new MutableLiveData<>();
    mValue15 = new MutableLiveData<>();
    mValue30 = new MutableLiveData<>();
    mValue45 = new MutableLiveData<>();
    mValue60 = new MutableLiveData<>();
    mValue75 = new MutableLiveData<>();
    mValue90 = new MutableLiveData<>();
    mValue105 = new MutableLiveData<>();
    mValue120 = new MutableLiveData<>();
  }

  /* GETTER */

  public boolean isDone() {
    return mIsDone;
  }

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

  public MutableLiveData<Integer> getValue15() {
    return mValue15;
  }

  public MutableLiveData<Integer> getValue30() {
    return mValue30;
  }

  public MutableLiveData<Integer> getValue45() {
    return mValue45;
  }

  public MutableLiveData<Integer> getValue60() {
    return mValue60;
  }

  public MutableLiveData<Integer> getValue75() {
    return mValue75;
  }

  public MutableLiveData<Integer> getValue90() {
    return mValue90;
  }

  public MutableLiveData<Integer> getValue105() {
    return mValue105;
  }

  public MutableLiveData<Integer> getValue120() {
    return mValue120;
  }


  /* SETTER */

  public void setDone(boolean isDone) {
    mIsDone = isDone;
  }

  public void setGi(boolean isGi) {
    mIsGi.setValue(isGi);
  }

  public void setTimestamp(String timestamp) {
    mTimestamp.setValue(timestamp);
  }

  public void setAmount(Integer amount) {
    mAmount.setValue(amount);
  }

  public void setStressed(String stressed) {
    mStressed.setValue(stressed);
  }

  public void setTired(String tired) {
    mTired.setValue(tired);
  }

  public void setValue0(Integer value0) {
    mValue0.setValue(value0);
  }

  public void setValue15(Integer value15) {
    mValue15.setValue(value15);
  }

  public void setValue30(Integer value30) {
    mValue30.setValue(value30);
  }

  public void setValue45(Integer value45) {
    mValue45.setValue(value45);
  }

  public void setValue60(Integer value60) {
    mValue60.setValue(value60);
  }

  public void setValue75(Integer value75) {
    mValue75.setValue(value75);
  }

  public void setValue90(Integer value90) {
    mValue90.setValue(value90);
  }

  public void setValue105(Integer value105) {
    mValue105.setValue(value105);
  }

  public void setValue120(Integer value120) {
    mValue120.setValue(value120);
  }
}
