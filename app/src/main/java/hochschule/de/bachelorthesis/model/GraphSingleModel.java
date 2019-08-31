package hochschule.de.bachelorthesis.model;

import androidx.lifecycle.MutableLiveData;

/**
 * @author Maik Thielen
 * <p>
 * This model class will be used to store data of the single graphs view.
 * <p>
 * The sorting type will be stored.
 */
public class GraphSingleModel {

    private MutableLiveData<String> mSelectedFood;
    private int mChartType;

    public GraphSingleModel() {
        mSelectedFood = new MutableLiveData<>();
    }

    /* GETTER */

    public MutableLiveData<String> getSelectedFood() {
        return mSelectedFood;
    }

    public int getChartType() {
        return mChartType;
    }

    /* SETTER */

    public void setSelectedFood(String selectedFood) {
        mSelectedFood.setValue(selectedFood);
    }

    public void setChartType(int chartType) {
        mChartType = chartType;
    }
}