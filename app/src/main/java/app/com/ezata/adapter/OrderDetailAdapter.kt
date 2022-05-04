package app.com.ezata.adapter

import app.com.ezata.model.OrderDetail
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import app.com.ezata.R
import android.widget.TextView

class OrderDetailAdapter(var orderList: Array<OrderDetail>) :
    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.order_detail_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderDetail = orderList[position]
        holder.tvQty.text = orderDetail.quantity
        holder.tvItem.text = orderDetail.items
        holder.tvPrice.text = orderDetail.price
        if (orderDetail.instruction.isEmpty()) {
            holder.tvInstruction.visibility = View.GONE
        } else {
            holder.tvInstruction.text = orderDetail.instruction
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvQty: TextView = itemView.findViewById(R.id.tvQty)
        var tvItem: TextView = itemView.findViewById(R.id.tvItem)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvInstruction: TextView = itemView.findViewById(R.id.tvInstruction)

    }
}