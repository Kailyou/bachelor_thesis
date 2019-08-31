package hochschule.de.bachelorthesis.utility;

import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * @author Maik Thielen
 * <p>
 * Simple class which returns values out of a list so they can be used as labels, for the
 * charts from the MPAndroidChart library.
 */
public class BarChartValueFormatter extends ValueFormatter {
    private String[] mLabels;

    public BarChartValueFormatter(String[] labels) {
        mLabels = labels;
    }

    @Override
    public String getFormattedValue(float value) {
        return mLabels[(int) value];
    }
}
