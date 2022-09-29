package app.com.ezata.adapter

import app.com.ezata.model.OrderDetail
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import app.com.ezata.R
import android.widget.TextView
import app.com.ezata.model.OrderItems

class OrderDetailAdapter() :
    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {


    var orderItems = arrayListOf<OrderItems>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        val orderDetail = orderItems[position]
        holder.tvQty.text = "${orderDetail.ordersMenuParse?.quantity}x"
        holder.tvItem.text = orderDetail.ordersMenuParse?.menu?.dishTitle
        holder.tvPrice.text = "$${orderDetail.ordersMenuParse?.price}"

        val extraItem = orderDetail.ordersMenuExtraItemParse.joinToString(separator = "") {
            "${it?.menuExtraItem?.name} $${it?.menuExtraItem?.price}\n"
        }

        if(extraItem.isNotBlank()){
            holder.tvExtraItem.text = extraItem
            holder.tvExtraItem.visibility = View.VISIBLE
        }else{
            holder.tvExtraItem.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return orderItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvQty: TextView = itemView.findViewById(R.id.tvQty)
        var tvItem: TextView = itemView.findViewById(R.id.tvItem)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvInstruction: TextView = itemView.findViewById(R.id.tvInstruction)
        var tvExtraItem: TextView = itemView.findViewById(R.id.extraItemTv)

    }
}