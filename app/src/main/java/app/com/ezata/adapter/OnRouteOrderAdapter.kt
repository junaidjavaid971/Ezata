package app.com.ezata.adapter

import app.com.ezata.model.OnRouteOrders
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import app.com.ezata.R
import android.widget.TextView

class OnRouteOrderAdapter(var orders: Array<OnRouteOrders>) :
    RecyclerView.Adapter<OnRouteOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.onroute_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val onRouteOrders = orders[position]
        holder.tvOrderNumber.text = onRouteOrders.orderNumber
        holder.tvCourierName.text = onRouteOrders.courierName
        holder.tvTime.text = onRouteOrders.deliveryTime
        holder.ivVehicle.setImageResource(onRouteOrders.vehicle)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOrderNumber: TextView = itemView.findViewById(R.id.tvOrderNumber)
        var tvCourierName: TextView = itemView.findViewById(R.id.tvCourierName)
        var tvTime: TextView = itemView.findViewById(R.id.tvDelieverdTime)
        var ivVehicle: ImageView = itemView.findViewById(R.id.ivVehicle)

    }
}