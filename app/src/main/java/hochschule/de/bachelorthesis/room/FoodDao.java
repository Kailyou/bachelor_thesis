package hochschule.de.bachelorthesis.room;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import hochschule.de.bachelorthesis.room.Food;
import io.reactivex.Flowable;

@Dao
public interface FoodDao {
    @Insert
    void insert(Food food);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("DELETE FROM food_table")
    void deleteAllFood();

    @Query("SELECT * FROM food_table WHERE id=:id")
    Flowable<Food> getFoodById(int id);

    @Query("SELECT * FROM food_table ORDER BY id DESC")
    LiveData<List<Food>> getAllFood();
}
