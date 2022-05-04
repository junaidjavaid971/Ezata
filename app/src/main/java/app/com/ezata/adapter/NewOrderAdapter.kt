package app.com.ezata.adapter

import android.content.Context
import app.com.ezata.model.NewOrder
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import app.com.ezata.R
import android.widget.TextView
import androidx.cardview.widget.CardView

class NewOrderAdapter(var orderList: Array<NewOrder>, var context: Context) :
    RecyclerView.Adapter<NewOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.new_order_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newOrder = orderList[position]
        holder.tvOrderNumber.text = newOrder.orderNumber
        holder.tvCustomerName.text = newOrder.customerName
        holder.tvOrderTime.text = newOrder.orderTime
        holder.tvOrderItems.text = newOrder.orderCount
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOrderNumber: TextView = itemView.findViewById(R.id.tvOrderNumber)
        var tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        var tvOrderTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvOrderItems: TextView = itemView.findViewById(R.id.tvItemsCount)
        var cardView: CardView = itemView.findViewById(R.id.cardview)

    }
}