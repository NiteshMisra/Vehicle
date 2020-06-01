package `in`.vehicle.database

import `in`.vehicle.extras.Constants
import `in`.vehicle.database.entity.VehicleModel
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [VehicleModel::class],
    version = 1
)
abstract class VehicleDatabase : RoomDatabase() {


    abstract fun vehicleDao(): VehicleDao

    companion object {
        private var instance: VehicleDatabase? = null

        @Synchronized
        fun getInstance(context: Context): VehicleDatabase? {

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    VehicleDatabase::class.java,
                    Constants.dataBaseName
                ).build()
            }

            return instance
        }
    }
}