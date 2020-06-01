package `in`.vehicle.repository

import `in`.vehicle.database.VehicleDao
import `in`.vehicle.database.entity.VehicleModel
import `in`.vehicle.extras.Coroutines
import androidx.lifecycle.MutableLiveData

class VehicleRepository(
    private var dao : VehicleDao
){

    fun getRegVehicles() : MutableLiveData<List<VehicleModel>>{
        val data : MutableLiveData<List<VehicleModel>> = MutableLiveData()
        Coroutines.io {
            data.postValue(dao.getRegisteredList())
        }
        return data
    }

    fun addVehicle(vehicleModel: VehicleModel) {
        Coroutines.io {
            dao.insert(vehicleModel)
        }
    }

    fun updateVehicle(vehicleModel: VehicleModel) {
        Coroutines.io {
            dao.updateVehicleData(vehicleModel.entryId,vehicleModel.vehicleNo,vehicleModel.model,vehicleModel.variant,vehicleModel.image)
        }
    }

    fun getVehicleDetail(entryId : Int) : MutableLiveData<VehicleModel>{
        val data : MutableLiveData<VehicleModel> = MutableLiveData()
        Coroutines.io {
            data.postValue(dao.getVehicleDetail(entryId))
        }
        return data
    }

}