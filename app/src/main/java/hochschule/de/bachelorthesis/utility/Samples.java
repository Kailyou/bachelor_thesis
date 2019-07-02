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

  public static Measurement getRandomMeasurement(Context context, int foodId, int userHistoryId) {
    // Get current time
    Date currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm", Locale.getDefault());
    String timeStamp = sdf.format(currentTime);

    int randomAmount = MyMath.getRandomInt(50, 1000);
    String randomStress = context.getResources().getStringArray(R.array.stress)[MyMath
        .getRandomInt(0, 2)];
    String randomTired = context.getResources().getStringArray(R.array.tired)[MyMath
        .getRandomInt(0, 2)];

    boolean randomGi = (MyMath.getRandomInt(0, 1) != 0);
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

    Measurement measurement = new Measurement(foodId, userHistoryId, randomGi, timeStamp,
        randomAmount,
        randomStress, randomTired, randomPhysicallyActive, randomAlcoholConsumed, randomIll,
        randomMedication, randomPeriod,
        100);

    // Random measurement, starting with 100 and ending with 100
    int[] measurements = new int[]{
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

    measurement.setGlucoseStart(measurements[0]);
    measurement.setGlucose15(measurements[1]);
    measurement.setGlucose30(measurements[2]);
    measurement.setGlucose45(measurements[3]);
    measurement.setGlucose60(measurements[4]);
    measurement.setGlucose75(measurements[5]);
    measurement.setGlucose90(measurements[6]);
    measurement.setGlucose105(measurements[7]);
    measurement.setGlucose120(measurements[8]);

    return measurement;
  }
}
