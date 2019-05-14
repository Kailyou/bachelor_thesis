package hochschule.de.bachelorthesis.utility;

import hochschule.de.bachelorthesis.room.Food;

public class FoodSample {

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

    public static Food getCola() {
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
}
