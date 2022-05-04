package app.com.ezata.adapter

import app.com.ezata.model.PickUpReady
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import app.com.ezata.R
import android.widget.TextView

class PickUpReadyAdapter(var pickUp: Array<PickUpReady>) :
    RecyclerView.Adapter<PickUpReadyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.pickup_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pickUpReady = pickUp[position]
        holder.tvOrderNumber.text = pickUpReady.orderNumber
        holder.tvCustomerName.text = pickUpReady.customerName
        holder.tvCourierName.text = pickUpReady.courierName
        holder.tvOrderTime.text = pickUpReady.orderTime
        holder.tvOrderPickTime.text = pickUpReady.pickUpTime
        holder.tvPinCode.text = pickUpReady.pinCode
        holder.ivVehicle.setImageResource(pickUpReady.vehicleId)
    }

    override fun getItemCount(): Int {
        return pickUp.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOrderNumber: TextView = itemView.findViewById(R.id.tvOrderNumber)
        var tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        var tvCourierName: TextView = itemView.findViewById(R.id.tvCourierName)
        var tvOrderTime: TextView = itemView.findViewById(R.id.tvOrderTime)
        var tvOrderPickTime: TextView = itemView.findViewById(R.id.tvPickup)
        var tvPinCode: TextView = itemView.findViewById(R.id.tvPinCode)
        var ivVehicle: ImageView = itemView.findViewById(R.id.ivVehicle)

    }
}