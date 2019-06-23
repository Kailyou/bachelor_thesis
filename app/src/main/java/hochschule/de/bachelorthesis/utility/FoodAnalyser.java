package hochschule.de.bachelorthesis.utility;

import hochschule.de.bachelorthesis.room.tables.Measurement;
import java.util.List;

public class FoodAnalyser {

  /**
   * Removes all measurements from the given list.
   *
   * @param measurements - List of measurements
   * @return - List without unfinished values.
   */
  public static List<Measurement> removeNotFinishedMeasurements(List<Measurement> measurements) {
    for (Measurement m : measurements) {
      // Remove all measurements which has not been finished yet from list.
      if (!m.isDone()) {
        measurements.remove(m);
      }
    }
    return measurements;
  }

  /**
   * Calculate the maximum glucose value for that food by analysing all measurements for this food.
   *
   * @param measurements - All measurements for this food
   * @return Returns the max Glucose
   */
  public static int getMaxGlucoseForFood(List<Measurement> measurements) {
    if (measurements.size() == 0) {
      return -1;
    }

    // Remove unfinished measurements
    List<Measurement> measurementsFinished = removeNotFinishedMeasurements(measurements);

    if (measurementsFinished.size() == 1) {
      return measurementsFinished.get(0).getGlucoseMax();
    }

    int maxGlucose = measurementsFinished.get(0).getGlucoseMax();

    for (int i = 1; i < measurementsFinished.size(); ++i) {
      if (measurementsFinished.get(i).getGlucoseMax() > maxGlucose) {
        maxGlucose = measurementsFinished.get(i).getGlucoseMax();
      }
    }

    return maxGlucose;
  }
}
