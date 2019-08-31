package hochschule.de.bachelorthesis.utility;

import android.content.Context;

import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.enums.MeasurementType;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Maik Thielen
 * <p>
 * This class provides some example data for foods and measurements.
 * <p>
 * There will be a total of four sample foods as soon as the APP starts,
 * the reference product glucose, which is the only one which cannot be deleted since it is needed
 * for the GI calculation. The other products are: Apple, Pizza and Coke.
 * <p>
 * ALso, there can be two sample measurements, unfinished and finished ones. Those are used for
 * debugging reasons, to be able to create measurements faster.
 */
public class Samples {

    /**
     * Empty Food object is used for the header line in the food list.
     *
     * @return An empty food object.
     */
    public static Food getEmptyFood() {
        return new Food("",
                "",
                "",
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1);
    }

    public static Food getGlucose() {
        return new Food("Glucose",
                "Reference Product",
                "Glucose",
                400,
                1676,
                0,
                0,
                0,
                100,
                100,
                -1);
    }

    public static Food getApple() {
        return new Food("Apple",
                "Pink Lady",
                "Fruit",
                52,
                217,
                0.2f,
                0.03f,
                0.3f,
                13.8f,
                10.4f,
                0);
    }

    public static Food getPizza() {
        return new Food("Pizza Salame",
                "Dr. Oetker",
                "Fast food",
                273,
                1142,
                14,
                4.8f,
                10f,
                26f,
                3.1f,
                1.4f);
    }

    public static Food getCoke() {
        return new Food("Coca-Cola",
                "Coca-Cola Company",
                "Drink",
                42,
                180,
                0,
                0f,
                0f,
                10.6f,
                10.6f,
                0f);
    }

    /**
     * Empty Measurement object is used for the header line in the measurement list.
     *
     * @return An empty measurement object.
     */
    public static Measurement getEmptyMeasurement() {
        return new Measurement(-1, -1, false, "", -1, "", "", false, false, false, false, false, -1);
    }

    /**
     * @param context       - The current context
     * @param foodId        - The food id
     * @param userHistoryId - The user history id
     * @return Returns an unfinished non gi measurement
     */
    public static Measurement getRandomMeasurementUnfinished(Context context, int foodId,
                                                             int userHistoryId, boolean isGi, String foodName) {
        // Get current time
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy_KK:mm aa", Locale.getDefault());
        String timeStamp = sdf.format(currentTime);

        int amount;

        switch (foodName) {
            case "Apple":
                amount = 362;
                break;

            case "Coca-Cola":
                amount = 471;
                break;

            case "Pizza Salame":
                amount = 192;
                break;

            case "Glucose":
                amount = 50;
                break;

            default:
                throw new IllegalStateException("Unexpected state at random measurement!");
        }

        String randomStress = context.getResources().getStringArray(R.array.stress)[MyMath
                .getRandomInt(0, 2)];
        String randomTired = context.getResources().getStringArray(R.array.tired)[MyMath
                .getRandomInt(0, 2)];

        // boolean randomGi = (MyMath.getRandomInt(0, 1) != 0);
        boolean randomPhysicallyActive = (MyMath.getRandomInt(0, 1) != 0);
        boolean randomAlcoholConsumed = (MyMath.getRandomInt(0, 1) != 0);
        boolean randomIll = (MyMath.getRandomInt(0, 1) != 0);
        boolean randomMedication;
        if (randomIll) {
            randomMedication = (MyMath.getRandomInt(0, 1) != 0);
        } else {
            randomMedication = false;
        }

        boolean randomPeriod = (MyMath.getRandomInt(0, 1) != 0);

        return new Measurement(foodId, userHistoryId,
                isGi,
                timeStamp,
                amount,
                randomStress, randomTired, randomPhysicallyActive, randomAlcoholConsumed, randomIll,
                randomMedication, randomPeriod,
                100);
    }

    /**
     * @param context       - The current context
     * @param foodId        - The food id
     * @param userHistoryId - The user history id
     * @return Returns a finished non gi measurement
     */
    public static Measurement getRandomMeasurement(Context context, int foodId, int userHistoryId, boolean isGi, MeasurementType measurementType, String foodType) {
        Measurement randomMeasurement = getRandomMeasurementUnfinished(context, foodId, userHistoryId, isGi, foodType);

        // Random measurement, starting with 100 and ending with 100
        int[] glucoseRandomValues = new int[9];
        glucoseRandomValues[0] = 100;

        switch (measurementType) {
            case LOW:
                glucoseRandomValues[1] = MyMath.getRandomInt(108, 113);
                glucoseRandomValues[2] = MyMath.getRandomInt(120, 125);
                glucoseRandomValues[3] = MyMath.getRandomInt(133, 138);
                glucoseRandomValues[4] = MyMath.getRandomInt(145, 150);
                glucoseRandomValues[5] = MyMath.getRandomInt(133, 138);
                glucoseRandomValues[6] = MyMath.getRandomInt(120, 125);
                glucoseRandomValues[7] = MyMath.getRandomInt(108, 113);
                glucoseRandomValues[8] = MyMath.getRandomInt(100, 107);

                break;

            case MID:
                glucoseRandomValues[1] = MyMath.getRandomInt(120, 125);
                glucoseRandomValues[2] = MyMath.getRandomInt(145, 150);
                glucoseRandomValues[3] = MyMath.getRandomInt(170, 175);
                glucoseRandomValues[4] = MyMath.getRandomInt(195, 200);
                glucoseRandomValues[5] = MyMath.getRandomInt(170, 175);
                glucoseRandomValues[6] = MyMath.getRandomInt(145, 150);
                glucoseRandomValues[7] = MyMath.getRandomInt(120, 125);
                glucoseRandomValues[8] = MyMath.getRandomInt(100, 119);
                break;

            case HIGH:
                glucoseRandomValues[1] = MyMath.getRandomInt(130, 135);
                glucoseRandomValues[2] = MyMath.getRandomInt(160, 165);
                glucoseRandomValues[3] = MyMath.getRandomInt(200, 205);
                glucoseRandomValues[4] = MyMath.getRandomInt(200, 225);
                glucoseRandomValues[5] = MyMath.getRandomInt(200, 205);
                glucoseRandomValues[6] = MyMath.getRandomInt(160, 165);
                glucoseRandomValues[7] = MyMath.getRandomInt(130, 135);
                glucoseRandomValues[8] = MyMath.getRandomInt(100, 134);
                break;

            case REF:
                glucoseRandomValues[1] = MyMath.getRandomInt(135, 140);
                glucoseRandomValues[2] = MyMath.getRandomInt(170, 175);
                glucoseRandomValues[3] = MyMath.getRandomInt(208, 213);
                glucoseRandomValues[4] = MyMath.getRandomInt(245, 250);
                glucoseRandomValues[5] = MyMath.getRandomInt(208, 213);
                glucoseRandomValues[6] = MyMath.getRandomInt(170, 175);
                glucoseRandomValues[7] = MyMath.getRandomInt(135, 140);
                glucoseRandomValues[8] = MyMath.getRandomInt(100, 134);
                break;

            default:
                throw new IllegalStateException("Illegal state at Samples");
        }

        randomMeasurement.setGlucoseStart(glucoseRandomValues[0]);
        randomMeasurement.setGlucose15(glucoseRandomValues[1]);
        randomMeasurement.setGlucose30(glucoseRandomValues[2]);
        randomMeasurement.setGlucose45(glucoseRandomValues[3]);
        randomMeasurement.setGlucose60(glucoseRandomValues[4]);
        randomMeasurement.setGlucose75(glucoseRandomValues[5]);
        randomMeasurement.setGlucose90(glucoseRandomValues[6]);
        randomMeasurement.setGlucose105(glucoseRandomValues[7]);
        randomMeasurement.setGlucose120(glucoseRandomValues[8]);

        return randomMeasurement;
    }
}
