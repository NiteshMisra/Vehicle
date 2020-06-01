package `in`.vehicle.adapter

import `in`.vehicle.R
import `in`.vehicle.activity.VehicleDetail
import `in`.vehicle.database.entity.VehicleModel
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.ByteArrayInputStream

class VehicleListAdapter(
    private var list: List<VehicleModel>,
    private var context: Context
) : RecyclerView.Adapter<VehicleListAdapter.VH>() {

    private val key = "entryId"

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val vehicleNo: TextView = view.findViewById(R.id.vehicleNo)
        val subTitle: TextView = view.findViewById(R.id.subTitle)
        val layout : LinearLayout = view.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.element_vehicle, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val currentItem: VehicleModel = list[position]
        holder.vehicleNo.text = currentItem.vehicleNo
        holder.subTitle.text = currentItem.model

        val stream = ByteArrayInputStream(Base64.decode(currentItem.image, Base64.DEFAULT))
        val bitmap = BitmapFactory.decodeStream(stream)
        holder.image.setImageBitmap(bitmap)

        holder.layout.setOnClickListener {
            val intent = Intent(context,VehicleDetail::class.java)
            intent.putExtra(key,currentItem.entryId)
            context.startActivity(intent)
        }
    }

}