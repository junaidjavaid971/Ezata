package app.com.ezata.adapter

import app.com.ezata.model.InProgressOrder
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import app.com.ezata.R
import android.widget.TextView
import androidx.cardview.widget.CardView

class InProgressOrderAdapter(var list: Array<InProgressOrder>) :
    RecyclerView.Adapter<InProgressOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.inprogess_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val progress = list[position]
        holder.tvOrderNumber.text = progress.orderNumber
        holder.tvCustomerName.text = progress.customerName
        holder.tvOrderTime.text = progress.orderTime
        holder.tvOrderItems.text = progress.orderCount
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOrderNumber: TextView = itemView.findViewById(R.id.tvOrderNumber)
        var tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        var tvOrderTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvOrderItems: TextView = itemView.findViewById(R.id.tvItemsCount)
        var cardView: CardView = itemView.findViewById(R.id.cardview)

    }
}