package hochschule.de.bachelorthesis.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hochschule.de.bachelorthesis.room.tables.Food;

/**
 * @author Maik Thielen
 * <p>
 * DaO class for the Food table
 * <p>
 * Contains functions to wrap SQL functions in.
 */

@Dao
public interface FoodDao {

    @Insert
    void insert(Food food);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("DELETE FROM food_table")
    void deleteAllFoods();

    @Query("SELECT * FROM food_table WHERE id=:id")
    LiveData<Food> getFoodById(int id);

    @Query("SELECT * FROM food_table WHERE food_name=:foodName AND brand_name=:brandName")
    LiveData<Food> getFoodByNameAndBrandName(String foodName, String brandName);

    @Query("SELECT * FROM food_table ORDER BY id DESC")
    LiveData<List<Food>> getAllFoods();
}
