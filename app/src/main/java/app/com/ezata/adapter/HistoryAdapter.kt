package app.com.ezata.adapter

import app.com.ezata.model.HistoryDetails
import app.com.ezata.OnItemClicked
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import app.com.ezata.R
import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class HistoryAdapter(var historyDetails: Array<HistoryDetails>, var onItemClicked: OnItemClicked) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.history_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val details = historyDetails[position]
        holder.tvDate.text = details.date
        holder.tvTime.text = details.time
        holder.tvOrderNmber.text = details.orderNumber
        holder.tvCustomerName.text = details.customerName
        holder.tvType.text = details.type
        holder.tvAmount.text = details.amount
        holder.tvStatus.text = details.status
        holder.fullSize.setOnClickListener { onItemClicked.onItemSelect(position) }
    }

    override fun getItemCount(): Int {
        return historyDetails.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var tvTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvOrderNmber: TextView = itemView.findViewById(R.id.tvOrderNumber)
        var tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        var tvType: TextView = itemView.findViewById(R.id.tvType)
        var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        var fullSize: ConstraintLayout = itemView.findViewById(R.id.fullsize)

    }
}