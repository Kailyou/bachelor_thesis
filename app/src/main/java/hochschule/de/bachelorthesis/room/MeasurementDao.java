package hochschule.de.bachelorthesis.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hochschule.de.bachelorthesis.room.tables.Food;
import hochschule.de.bachelorthesis.room.tables.Measurement;

@Dao
public interface MeasurementDao {

  @Insert
  void insert(Measurement measurement);

  @Update
  void update(Measurement measurement);

  @Delete
  void delete(Measurement measurement);

  @Query("DELETE FROM measurement_table WHERE food_id=:foodId")
  void deleteAllMeasurementsWithFoodId(int foodId);

  @Query("SELECT * FROM measurement_table WHERE id=:id")
  LiveData<Measurement> getMeasurementById(int id);

  @Query("SELECT * FROM measurement_table WHERE food_id=:foodId")
  LiveData<List<Measurement>> getAllMeasurementsByFoodId(int foodId);

  @Query("SELECT * FROM measurement_table ORDER BY id DESC")
  LiveData<List<Measurement>> getAllMeasurements();
}
