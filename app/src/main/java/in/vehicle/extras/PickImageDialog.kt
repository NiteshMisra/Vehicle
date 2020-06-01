package `in`.vehicle.extras

import `in`.vehicle.R
import `in`.vehicle.activity.AddVehicleActivity
import `in`.vehicle.activity.VehicleDetail
import `in`.vehicle.databinding.PickImageDialogBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PickImageDialog(private var activity1: FragmentActivity) : BottomSheetDialogFragment() {

    private lateinit var binding: PickImageDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.pick_image_dialog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.camera.setOnClickListener {
            dismiss()
            if (activity1 is AddVehicleActivity) {
                (activity1 as AddVehicleActivity).cameraPicker()
            } else if (activity1 is VehicleDetail) {
                (activity1 as VehicleDetail).cameraPicker()
            }
        }

        binding.gallery.setOnClickListener {
            dismiss()
            if (activity1 is AddVehicleActivity) {
                (activity1 as AddVehicleActivity).galleryPicker()
            } else if (activity1 is VehicleDetail) {
                (activity1 as VehicleDetail).galleryPicker()
            }
        }

    }

}
