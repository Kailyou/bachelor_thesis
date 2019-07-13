package hochschule.de.bachelorthesis.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MyMath {

  /* ANALYSE MEASUREMENTS */


  /**
   * @param values Array list with counts
   * @return The average from the given list.
   */
  public static float getAverageFromArrayList(ArrayList<Integer> values) {
    float average = 0.0f;
    for (int i = 0; i < values.size(); i++) {
      average += values.get(i);
    }
    return average /= values.size();
  }

  /**
   * Gets the max value of an array.
   *
   * @param arr - The array.
   * @return - Returns 0 if the array is empty, else the highest value inside.
   */
  public static int getMaxFromIntegerArrayList(ArrayList<Integer> arr) {
    if (arr.size() <= 0) {
      return 0;
    }

    int res = arr.get(0);

    for (int i = 1; i < arr.size(); ++i) {
      if (arr.get(i) > res) {
        res = arr.get(i);
      }
    }

    return res;
  }

  public static float getMaxFromFloatArrayList(ArrayList<Float> arr) {
    if (arr.size() <= 0) {
      return 0;
    }

    float res = arr.get(0);

    for (int i = 1; i < arr.size(); ++i) {
      if (arr.get(i) > res) {
        res = arr.get(i);
      }
    }

    return res;
  }

  /* STATISTICS */

  /**
   * This function will return the median of a list of values.
   *
   * @param values - ArrayList with values inside.
   * @return Returns the median.
   */
  public static float getMedianValue(ArrayList<Integer> values) {
    if (values.size() == 0 || values.size() == 1) {
      return 0;
    }

    // Sort the list
    Collections.sort(values);

    int amount = values.size();

    // Last bit = 0 even, else odd.
    if ((amount & 1) == 0) {
      // -1 because arrays start at 1
      return (float) 0.5 * (values.get((amount / 2) - 1) + values.get((amount / 2)));
    } else {
      // +1 is missing here because of the -1 because of array index
      return values.get((amount) / 2);
    }
  }

  /**
   * Returns the percentile value of a given list of values and the percentage given.
   *
   * n = amount of values p = percentile per cent
   *
   * First calculate k with k = (n * p)
   *
   * Check if the result k is even or odd.
   *
   * If it is odd use this formula: xp = x(k)
   *
   * If it is even use this formula: 0.5 (x(k) + x(k+1)
   *
   * The -1 is due the fact that arrays start at index 0 and not at 1.
   *
   * @param values - ArrayList with values inside.
   * @param p - The percent for the percentile
   * @return Returns the percentile.
   */
  public static float getPercentile(ArrayList<Integer> values, float p) {
    if (values.size() == 0 || values.size() == 1) {
      return 0;
    }

    // Sort the list
    Collections.sort(values);

    double k = values.size() * p;

    // Even or odd
    if (k == (int) k) {
      // -1 because arrays start at 1
      float v1 = values.get((int) k - 1);
      float v2 = values.get((int) k);

      return (float) 0.5 * (v1 + v2);
    } else {
      return values.get(((int) Math.ceil(k)) - 1);
    }
  }

  public static void getVariance() {

  }

  public static float getStandardDeviation(ArrayList<Integer> values) {
    if (values.size() == 0 || values.size() == 1) {
      return 0;
    }

    // Step 1: Calculate average
    double average = getAverageFromArrayList(values);

    // Step 2: Calculate variance

    // Step 3: Calculate standard deviation

    return 0;
  }


  /* OTHERS */

  /**
   * @param min The minimum count (included)
   * @param max The maximum count (included)
   * @return Returns a random count between a given range.
   */
  public static int getRandomInt(int min, int max) {
    Random random = new Random();
    return random.nextInt(max - min + 1) + min;
  }

}
