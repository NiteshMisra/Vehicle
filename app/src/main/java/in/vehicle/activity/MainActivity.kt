@file:Suppress("DEPRECATION")

package `in`.vehicle.activity

import `in`.vehicle.R
import `in`.vehicle.adapter.VehicleListAdapter
import `in`.vehicle.database.VehicleDatabase
import `in`.vehicle.database.entity.VehicleModel
import `in`.vehicle.databinding.ActivityMainBinding
import `in`.vehicle.extras.VehicleFactory
import `in`.vehicle.repository.VehicleRepository
import `in`.vehicle.viewmodel.VehicleViewModel
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: VehicleViewModel
    private lateinit var vAdapter : VehicleListAdapter
    private lateinit var list : ArrayList<VehicleModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.registerVehicle.setOnClickListener {
            startActivity(Intent(this,AddVehicleActivity::class.java))
        }

        binding.rcv.layoutManager = LinearLayoutManager(this)

        val database: VehicleDatabase = VehicleDatabase.getInstance(this)!!
        val repository = VehicleRepository(database.vehicleDao())
        val factory = VehicleFactory(repository)
        viewModel = ViewModelProviders.of(this,factory).get(VehicleViewModel::class.java)

        list = ArrayList()
        vAdapter = VehicleListAdapter(list,this)

    }

    override fun onStart() {
        super.onStart()
        getList()
    }

    private fun getList() {

        viewModel.getRegList().observe(this,
            Observer { list2 ->
                list.clear()
                list.addAll(list2)
                vAdapter = VehicleListAdapter(list,this)
                binding.rcv.adapter = vAdapter
                vAdapter.notifyDataSetChanged()
            })
    }
}
