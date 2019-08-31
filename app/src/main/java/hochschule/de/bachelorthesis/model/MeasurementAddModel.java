package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

/**
 * @author Maik Thielen
 *
 * This model class will be used to store data of the measurement list view.
 * <p>
 * The sorting type will be stored.
 */
public class MeasurementAddModel {

    // General
    private MutableLiveData<Boolean> mGi;

    // Time & Advance information
    private MutableLiveData<String> mTimestamp;

    // Advance information
    private MutableLiveData<Integer> mAmount;
    private MutableLiveData<String> mStressed;
    private MutableLiveData<String> mTired;
    private MutableLiveData<Boolean> mPhysicallyActivity;
    private MutableLiveData<Boolean> mAlcoholConsumed;
    private MutableLiveData<Boolean> mIll;
    private MutableLiveData<Boolean> mMedication;
    private MutableLiveData<Boolean> mPeriod;

    // Measurement values
    private MutableLiveData<Integer> mValue0;

    public MeasurementAddModel() {
        mGi = new MutableLiveData<>();
        mTimestamp = new MutableLiveData<>();
        mAmount = new MutableLiveData<>();
        mStressed = new MutableLiveData<>();
        mTired = new MutableLiveData<>();
        mPhysicallyActivity = new MutableLiveData<>();
        mAlcoholConsumed = new MutableLiveData<>();
        mIll = new MutableLiveData<>();
        mMedication = new MutableLiveData<>();
        mPeriod = new MutableLiveData<>();
        mValue0 = new MutableLiveData<>();
    }

    /* GETTER */

    public MutableLiveData<Boolean> isGi() {
        return mGi;
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

    public MutableLiveData<Boolean> getPhysicallyActivity() {
        return mPhysicallyActivity;
    }

    public MutableLiveData<Boolean> getAlcoholConsumed() {
        return mAlcoholConsumed;
    }

    public MutableLiveData<Boolean> getIll() {
        return mIll;
    }

    public MutableLiveData<Boolean> getMedication() {
        return mMedication;
    }

    public MutableLiveData<Boolean> getPeriod() {
        return mPeriod;
    }

    public MutableLiveData<Integer> getValue0() {
        return mValue0;
    }

    /* SETTER */

    public void setGi(boolean isGi) {
        mGi.setValue(isGi);
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

    public void setPhysicallyActivity(
            boolean physicallyActivity) {
        mPhysicallyActivity.setValue(physicallyActivity);
    }

    public void setAlcoholConsumed(boolean alcoholConsumed) {
        mAlcoholConsumed.setValue(alcoholConsumed);
    }

    public void setIll(boolean ill) {
        mIll.setValue(ill);
    }

    public void setMedication(boolean medication) {
        mMedication.setValue(medication);
    }

    public void setPeriod(boolean period) {
        mPeriod.setValue(period);
    }

    public void setValue0(Integer value0) {
        mValue0.setValue(value0);
    }
}
