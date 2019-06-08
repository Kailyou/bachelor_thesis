package hochschule.de.bachelorthesis.utility;

import java.util.ArrayList;
import java.util.Random;

public class MyMath {

  public static int getRandomInt(int min, int max) {
    Random random = new Random();
    return random.nextInt(max - min + 1) + min;
  }

  public static int getAverageFromArrayList(ArrayList<Integer> al) {
    if(al.size() == 0) {
      return 0;
    }

    if(al.size() == 1) {
      return al.get(0);
    }

    float avg = al.get(0);

    for (int i = 1; i < al.size(); i++) {
      avg += al.get(i);
    }

    return (int) avg / al.size();
  }

  /**
   * Gets the max value of an array.
   *
   * @param arr - The array.
   * @return - Returns -1 if the array is empty, else the highest value inside.
   */
  public static int getMaxFromArrayList(ArrayList<Integer> arr) {
    if (arr.size() <= 0) {
      return -1;
    }

    int res = arr.get(0);

    for (int i = 1; i < arr.size(); ++i) {
      if (arr.get(i) > res) {
        res = arr.get(i);
      }
    }

    return res;
  }
}
