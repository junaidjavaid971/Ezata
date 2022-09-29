package app.com.ezata.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import app.com.ezata.OnItemClicked
import app.com.ezata.R
import app.com.ezata.model.parse.OrdersParse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(
    var historyDetails: ArrayList<OrdersParse?>,
    var onItemClicked: OnItemClicked
) :
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
//        holder.tvDate.text = getDate(details?.createdAt.toString())
        if (position == 0) {
            holder.tvDate.text = getDate("2022-07-16 12:10:30")
        }
        holder.tvTime.text = getTime(details?.createdAt.toString())
        holder.tvOrderNmber.text = details?.orderNumber ?: "N/A"
        holder.tvAmount.text = ("$" + String.format("%.2f", details?.amount)) ?: "N/A"
        holder.tvStatus.text = details?.orderStatus.toString() ?: "N/A"

        details?.user?.fetchIfNeeded()?.let {
            holder.tvCustomerName.text =
                "${it.getString("first_name") ?: ""} ${it.getString("last_name") ?: ""}" ?: "N/A"
        }

        holder.fullSize.setOnClickListener { details?.let { it1 -> onItemClicked.onItemSelect(it1) } }
    }


    override fun getItemCount(): Int {
        return historyDetails.size
    }

    private fun getDate(orderDate: String): String {
        /*val dateFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val date: Date? = dateFormatter.parse(orderDate)

        val formatter2 = SimpleDateFormat("yyyy-MM-dd")

        return formatter2.format(date)*/
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateFormatter2 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val date: Date? = dateFormatter.parse(orderDate)

        val diff = dateFormatter2.parse(Date().toString())?.time?.minus(date?.time ?: 0)

        val seconds = diff?.div(1000)
        val minutes = seconds?.div(60)
        val hours = minutes?.div(60)
        val days = hours?.div(24)

        Log.d("Diff: ", orderDate)
        Log.d(
            "Diff: ",
            days.toString() + " Days, " + hours + " Hours, " + minutes + " Minutes, " + seconds + " Seconds"
        )
        /*val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date: Date? = dateFormatter.parse(orderDate)

        val formatter2 = SimpleDateFormat("yyyy-MM-dd")

        return formatter2.format(date)*/

        return ""
    }

    private fun getTime(orderDate: String): String {
        val dateFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val date: Date? = dateFormatter.parse(orderDate)

        val formatter2 = SimpleDateFormat("HH:mm")

        return formatter2.format(date)
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