package app.com.ezata.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.com.ezata.R
import app.com.ezata.model.ColorsEnum
import app.com.ezata.model.parse.OrdersParse

class OrdersRecyclerViewAdapter(var context: Context) :
    RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder>() {


    var itemClicked: ((orderItem: OrdersParse?) -> Unit)? = null

    var orderList = listOf<OrdersParse?>()
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
            layoutInflater.inflate(R.layout.new_order_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newOrder = orderList[position]
        holder.tvOrderNumber.text = newOrder?.orderNumber ?: "Not Set"

        holder.tvOrderNumber.backgroundTintList = ContextCompat.getColorStateList(
            context,
            ColorsEnum.values().toList().shuffled().first().color
        )

        newOrder?.user?.fetchIfNeeded()?.let {
            holder.tvCustomerName.text =
                "${it.getString("first_name") ?: ""} ${it.getString("last_name") ?: ""}"
        }

//        holder.tvOrderTime.text = newOrder?.createdAt.toString()
        holder.tvOrderItems.text = "${newOrder?.quantity ?: "x"} items"

        holder.cardView.setOnClickListener {
            itemClicked?.invoke(newOrder)
        }
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