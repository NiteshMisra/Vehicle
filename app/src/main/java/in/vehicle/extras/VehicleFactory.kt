package `in`.vehicle.extras

import `in`.vehicle.repository.VehicleRepository
import `in`.vehicle.viewmodel.VehicleViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VehicleFactory(
    private var vehicleRepository: VehicleRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VehicleViewModel(vehicleRepository) as T
    }
}