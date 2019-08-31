package hochschule.de.bachelorthesis.model;

import hochschule.de.bachelorthesis.enums.SortType;

/**
 * @author Maik Thielen
 * <p>
 * This model class will be used to store data of the measurement list view.
 * <p>
 * The sorting type will be stored.
 */
public class MeasurementListModel {

    private SortType mSortType = SortType.GLUCOSE_MAX;

    public SortType getSortType() {
        return mSortType;
    }

    public void setSortType(SortType sortType) {
        mSortType = sortType;
    }
}
