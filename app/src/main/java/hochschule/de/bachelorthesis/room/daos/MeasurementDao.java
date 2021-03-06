package hochschule.de.bachelorthesis.room.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hochschule.de.bachelorthesis.room.tables.Measurement;

/**
 * @author Maik Thielen
 * <p>
 * DaO class for the Measurement table
 * <p>
 * Contains functions to wrap SQL functions in.
 */
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

    // ORDER BY id it was before
    @Query("SELECT * FROM measurement_table ORDER BY food_id DESC")
    LiveData<List<Measurement>> getAllMeasurements();

    @Query("SELECT COUNT(id) FROM measurement_table WHERE food_id=:foodId")
    LiveData<Integer> getRowCount(int foodId);
}
