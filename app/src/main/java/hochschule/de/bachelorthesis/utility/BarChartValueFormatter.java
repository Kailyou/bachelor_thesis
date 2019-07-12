package hochschule.de.bachelorthesis.utility;

import com.github.mikephil.charting.formatter.ValueFormatter;

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
