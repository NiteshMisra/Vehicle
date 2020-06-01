package `in`.vehicle.database.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class VehicleModel(
    @SerializedName("vehicleNo") var vehicleNo : String,
    @SerializedName("model") var model : String,
    @SerializedName("variant") var variant : String,
    @SerializedName("image") var image : String
) {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @SerializedName("entryId")
    var entryId : Int = 0
}