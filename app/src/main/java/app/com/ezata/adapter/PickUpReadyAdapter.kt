package app.com.ezata.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.com.ezata.R
import app.com.ezata.model.ColorsEnum
import app.com.ezata.model.OrderItems
import app.com.ezata.model.PickUpReady
import app.com.ezata.model.parse.OrdersMenuExtraItemParse
import app.com.ezata.model.parse.OrdersMenuParse
import app.com.ezata.model.parse.OrdersParse
import app.com.ezata.utils.findListInBackgroundCoroutine
import com.parse.ParseQuery
import java.text.SimpleDateFormat
import java.util.*

class PickUpReadyAdapter(var pickUp: List<OrdersParse?>, var context: Context) :
    RecyclerView.Adapter<PickUpReadyAdapter.ViewHolder>() {
    var changeStatusCountDownTimer: ((currentOrder: OrdersParse?) -> Unit)? = null
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

        holder.tvOrderNumber.text = pickUpReady?.orderNumber ?: "N/A"

        holder.tvOrderNumber.backgroundTintList = ContextCompat.getColorStateList(
            context,
            ColorsEnum.values().toList().shuffled().first().color
        )

        pickUpReady?.user?.fetchIfNeeded()?.let {
            holder.tvCustomerName.text =
                "${it.getString("first_name") ?: ""} ${it.getString("last_name") ?: ""}" ?: "N/A"
        }

        holder.tvOrderTime.text = 30.toString() + " Min"
        holder.tvOrderPickTime.text = getPickupTime(30)

        holder.layoutCheck.setOnClickListener { changeStatusCountDownTimer?.invoke(pickUpReady) }

        pickUpReady?.deliveryPerson?.let {
            holder.tvCourierName.text =
                pickUpReady.deliveryPerson?.firstName + " " + pickUpReady.deliveryPerson?.lastName
        } ?: "Awaiting Pickup"

        //TODO: Invoke countdown timer from here.
    }

    private fun getPickupTime(minute: Int): String {
        val df = SimpleDateFormat("HH:mm")
        val d: Date = df.parse(df.format(Date())) as Date
        val cal: Calendar = Calendar.getInstance()
        cal.time = d
        cal.add(Calendar.MINUTE, minute)
        return df.format(cal.time)
    }

    override fun getItemCount(): Int {
        return pickUp.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvOrderNumber: TextView = itemView.findViewById(R.id.tvOrderNumber)
        var tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        var tvCourierName: TextView = itemView.findViewById(R.id.tvCourierName)
        var tvOrderTime: TextView = itemView.findViewById(R.id.tvOrderTime)
        var tvOrderPickTime: TextView = itemView.findViewById(R.id.tvTime)
        var tvPinCode: TextView = itemView.findViewById(R.id.tvPinCode)
        var ivVehicle: ImageView = itemView.findViewById(R.id.ivVehicle)
        var layoutCheck: ConstraintLayout = itemView.findViewById(R.id.layoutCheck)

    }
}