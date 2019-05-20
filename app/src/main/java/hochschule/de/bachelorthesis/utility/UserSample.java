package hochschule.de.bachelorthesis.utility;

import hochschule.de.bachelorthesis.model.User;
import hochschule.de.bachelorthesis.room.tables.Food;

public class UserSample {

    public static User getTestUser1() {
        return new User(29,
                173,
                88,
                "male",
                "low",
                "no",
                "no",
                "no");
    }
}
