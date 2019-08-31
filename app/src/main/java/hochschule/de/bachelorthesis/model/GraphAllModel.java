package hochschule.de.bachelorthesis.model;


/**
 * @author Maik Thielen
 * <p>
 * This model class will be used to store data of the All Graphs view.
 * <p>
 * The sorting type will be stored.
 */
public class GraphAllModel {

    private int mChartType;

    /* GETTER */

    public int getChartType() {
        return mChartType;
    }

    /* SETTER */

    public void setChartType(int chartType) {
        mChartType = chartType;
    }
}
