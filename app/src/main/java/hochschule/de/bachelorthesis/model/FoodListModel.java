package hochschule.de.bachelorthesis.model;

import hochschule.de.bachelorthesis.enums.SortType;

/**
 * @author Maik Thielen
 * <p>
 * This model class will be used to store data of the food list view.
 * <p>
 * The sorting type will be stored.
 */
public class FoodListModel {

    private SortType mSortType = SortType.ALPHANUMERIC;

    public SortType getSortType() {
        return mSortType;
    }

    public void setSortType(SortType sortType) {
        mSortType = sortType;
    }
}
