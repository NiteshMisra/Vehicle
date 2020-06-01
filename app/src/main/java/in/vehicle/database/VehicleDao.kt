package `in`.vehicle.database

import `in`.vehicle.database.entity.VehicleModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg videoModel: VehicleModel)

    @Query("Select * from VehicleModel Order by entryId DESC")
    fun getRegisteredList() : List<VehicleModel>

    @Query("Update VehicleModel set vehicleNo = :vehicleNo, model =:model, variant =:variant, image =:image where entryId =:entryId")
    fun updateVehicleData(entryId : Int,vehicleNo : String,model : String,variant : String,image : String)

    @Query("Select * from VehicleModel where entryId = :entryId LIMIT 1")
    fun getVehicleDetail(entryId: Int) : VehicleModel

}