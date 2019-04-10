package hochschule.de.bachelorthesis.other;

import hochschule.de.bachelorthesis.room.Food;

public class FoodSample {

    public static Food getApple() {
        return new Food("Apple",
                "Pink Lady",
                "Fruit",
                52,
                0.2,
                0.03,
                0.3,
                13.8,
                10.4,
                0);
    }

    public static Food getPizza() {
        return new Food("Pizza Salame",
                "Dr. Oetker",
                "Fast food",
                273,
                14,
                4.8,
                10,
                26,
                3.1,
                1.4);
    }

    public static Food getCola() {
        return new Food("Coca-Cola",
                "Coca-Cola Company",
                "Drink",
                42,
                0,
                0,
                0,
                10.5,
                10.5,
                8);
    }
}
