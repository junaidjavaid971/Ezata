package app.com.ezata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.com.ezata.R
import app.com.ezata.model.parse.OrdersParse
import com.parse.ParseObject

class OnRouteOrderAdapter(var orders: List<OrdersParse?>) :
    RecyclerView.Adapter<OnRouteOrderAdapter.ViewHolder>() {
    var onRouteCountDownTimer: ((currentOrder: OrdersParse?) -> Unit)? = null

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

        holder.tvOrderNumber.text = onRouteOrders?.orderNumber ?: "N/A"
        holder.tvTime.text = onRouteOrders?.deliveryTime.toString() + " Mins"

        onRouteOrders?.user?.fetchIfNeeded()?.let {
            holder.tvCourierName.text =
                "${it.getString("first_name") ?: ""} ${it.getString("last_name") ?: ""}" ?: "N/A"
        }

        holder.itemView.setOnClickListener {
            onRouteCountDownTimer?.invoke(onRouteOrders)
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOrderNumber: TextView = itemView.findViewById(R.id.tvOrderNumber)
        var tvCourierName: TextView = itemView.findViewById(R.id.tvCourierName)
        var tvTime: TextView = itemView.findViewById(R.id.tvDelieverdTime)
    }
}