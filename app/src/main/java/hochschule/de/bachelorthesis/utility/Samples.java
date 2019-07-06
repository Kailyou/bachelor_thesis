package hochschule.de.bachelorthesis.utility;

import android.content.Context;
import hochschule.de.bachelorthesis.R;
import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Samples {

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
        176,
        42,
        0,
        0f,
        0f,
        10.5f,
        10.5f,
        8f);
  }

  public static Measurement getRandomMeasurementUnfinished(Context context, int foodId,
      int userHistoryId) {

    String formatting = " AM";
    int randomFormatting = MyMath.getRandomInt(0, 1);
    if (randomFormatting == 1) {
      formatting = " PM";
    }

    // Get current time
    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());
    String timeStamp = sdf.format(currentTime + formatting);

    int randomAmount = MyMath.getRandomInt(50, 1000);
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

    return new Measurement(foodId, userHistoryId, false, timeStamp,
        randomAmount,
        randomStress, randomTired, randomPhysicallyActive, randomAlcoholConsumed, randomIll,
        randomMedication, randomPeriod,
        100);
  }

  public static Measurement getRandomMeasurement(Context context, int foodId, int userHistoryId) {
    Measurement randomMeasurement = getRandomMeasurementUnfinished(context, foodId, userHistoryId);

    // Random measurement, starting with 100 and ending with 100
    int[] glucoseValuesRandom = new int[]{
        100,
        MyMath.getRandomInt(100, 125),
        MyMath.getRandomInt(125, 150),
        MyMath.getRandomInt(150, 175),
        MyMath.getRandomInt(175, 225),
        MyMath.getRandomInt(195, 225),
        MyMath.getRandomInt(145, 195),
        MyMath.getRandomInt(120, 145),
        100
    };

    randomMeasurement.setGlucoseStart(glucoseValuesRandom[0]);
    randomMeasurement.setGlucose15(glucoseValuesRandom[1]);
    randomMeasurement.setGlucose30(glucoseValuesRandom[2]);
    randomMeasurement.setGlucose45(glucoseValuesRandom[3]);
    randomMeasurement.setGlucose60(glucoseValuesRandom[4]);
    randomMeasurement.setGlucose75(glucoseValuesRandom[5]);
    randomMeasurement.setGlucose90(glucoseValuesRandom[6]);
    randomMeasurement.setGlucose105(glucoseValuesRandom[7]);
    randomMeasurement.setGlucose120(glucoseValuesRandom[8]);

    return randomMeasurement;
  }
}
