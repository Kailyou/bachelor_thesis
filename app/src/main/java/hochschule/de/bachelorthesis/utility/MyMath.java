package hochschule.de.bachelorthesis.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hochschule.de.bachelorthesis.room.tables.Measurement;

public class MyMath {

    /* ANALYSE MEASUREMENTS */

    /**
     * Gets the max value of an array.
     *
     * @param arr - The array.
     * @return - Returns 0 if the array is empty, else the highest value inside.
     */
    public static int calculateMaxFromIntList(ArrayList<Integer> arr) {
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

    /**
     * Gets the max value of an array.
     *
     * @param arr - The array.
     * @return - Returns 0 if the array is empty, else the highest value inside.
     */
    public static float calculateMaxFromFloatList(ArrayList<Float> arr) {
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

    /**
     * Calculates the integral using the numeric integration: Trapezoidal Rule
     *
     * @param values - values to integrate
     * @return The integral
     */
    public static float calculateIntegral(ArrayList<Integer> values) {
        // (b-a)/n
        // b = upper limit
        // a = lower limit
        // n = amount measurements
        float deltaX = (120f - 0f) / 8f;

        // Add every value besides the first and the last
        int sum = 0;

        for (int i = 1; i < values.size() - 1; ++i) {
            sum += values.get(i);
        }

        return (deltaX / 2) * (values.get(0) + 2 * (sum) + values.get(values.size() - 1));
    }

    /**
     * Calculates the GI for a given set of glucose values.
     *
     * @param testProductValues - glucose values for a test product
     * @param refMeasurements   - all measurement values from the reference product (glucose)
     * @return - Returns the calculated GI, 0 if any list is empty.
     */
    public static float calculateGI(ArrayList<Integer> testProductValues, List<Measurement> refMeasurements) {
        if (testProductValues.size() == 0 || refMeasurements.size() == 0) {
            return 0;
        }

        // Add all glucose values from all ref measurements to one list
        ArrayList<Float> allIntegralsRefs = new ArrayList<>();

        // Calculate all integrals from all measurements
        for (Measurement m : refMeasurements) {
            allIntegralsRefs.add(m.getIntegral());
        }

        // Now use the mean from all calculated integrals for the ref food.
        return (calculateIntegral(testProductValues) / calculateMeanFromFloats(allIntegralsRefs) * 100);
    }


    /* STATISTICS */

    /**
     * @param values Array list with counts
     * @return The average from the given list.
     */
    public static float calculateMeanFromIntegers(ArrayList<Integer> values) {
        float average = 0.0f;

        for (int i = 0; i < values.size(); i++) {
            average += values.get(i);
        }
        return average / values.size();
    }

    /**
     * @param values Array list with counts
     * @return The average from the given list.
     */
    public static float calculateMeanFromFloats(ArrayList<Float> values) {
        float average = 0.0f;
        for (int i = 0; i < values.size(); i++) {
            average += values.get(i);
        }
        return average / values.size();
    }

    /**
     * This function will return the median of a list of values.
     *
     * @param values - ArrayList with values inside.
     * @return Returns the median.
     */
    public static float getMedianValue(ArrayList<Integer> values) {
        if (values.size() == 0) {
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
     * <p>
     * n = amount of values p = percentile per cent
     * <p>
     * First calculate k with k = (n * p)
     * <p>
     * Check if the result k is even or odd.
     * <p>
     * If it is odd use this formula: xp = x(k)
     * <p>
     * If it is even use this formula: 0.5 (x(k) + x(k+1)
     * <p>
     * The -1 is due the fact that arrays start at index 0 and not at 1.
     *
     * @param values - ArrayList with values inside.
     * @param p      - The percent for the percentile
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

    /**
     * Calculates the variance of a list of data.
     * <p>
     * Needed to calculate the standard deviation
     *
     * @param values ArrayList with values inside.
     * @return The variance.
     */
    private static float calculateVariance(ArrayList<Integer> values) {
        float mean = calculateMeanFromIntegers(values);
        float temp = 0f;

        for (Integer i : values) {
            temp += (i - mean) * (i - mean);
        }

        return temp / (values.size() - 1);
    }

    public static float calculateStandardDeviation(ArrayList<Integer> values) {
        if (values.size() == 0 || values.size() == 1) {
            return 0;
        }

        return (float) Math.sqrt(calculateVariance(values));
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
