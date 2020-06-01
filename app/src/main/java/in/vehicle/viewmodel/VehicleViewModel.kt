package `in`.vehicle.viewmodel

import `in`.vehicle.database.entity.VehicleModel
import `in`.vehicle.repository.VehicleRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class VehicleViewModel(
    private var vehicleRepository: VehicleRepository
) : ViewModel() {

    fun getRegList(): LiveData<List<VehicleModel>> {
        return vehicleRepository.getRegVehicles()
    }

    fun addVehicle(vehicleModel: VehicleModel) {
        vehicleRepository.addVehicle(vehicleModel)
    }

    fun updateVehicle(vehicleModel: VehicleModel) {
        vehicleRepository.updateVehicle(vehicleModel)
    }

    fun getVehicleDetail(entryId : Int) : LiveData<VehicleModel>{
        return vehicleRepository.getVehicleDetail(entryId)
    }

}