package app.com.ezata.ui.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.com.ezata.OnItemClicked
import app.com.ezata.model.HistoryDetails
import app.com.ezata.model.OrderDetail
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.com.ezata.R
import android.content.Intent
import android.graphics.Color
import app.com.ezata.ui.activities.MainActivity
import app.com.ezata.ui.activities.PickUpReadyActivity
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.HistoryAdapter
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import app.com.ezata.adapter.OrderDetailAdapter
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import app.com.ezata.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity(), OnItemClicked {
    var binding: ActivityHistoryBinding? = null
    lateinit var details: Array<HistoryDetails>
    lateinit var orders: Array<OrderDetail>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        setHistoryData()
        binding?.ivMenu?.setOnClickListener { view: View? -> openNavDrawer() }
        binding?.btnSort?.setOnClickListener { view1: View? -> }
        binding?.ivPrepareOrder?.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    applicationContext, MainActivity::class.java
                )
            )
        }
        binding?.ivPickupReady?.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    applicationContext, PickUpReadyActivity::class.java
                )
            )
        }
    }

    private fun openNavDrawer() {
        binding!!.drawer.openDrawer(Gravity.LEFT)
    }

    private fun setHistoryData() {
        details = arrayOf(
            HistoryDetails(
                "03/12/22",
                "11:38 PM",
                "J83NW",
                "Leonard N.",
                "Delivery",
                "32.23",
                "Completed"
            ),
            HistoryDetails(
                "03/12/22",
                "11:38 PM",
                "J83NW",
                "Leonard N.",
                "Delivery",
                "32.23",
                "Completed"
            ),
            HistoryDetails(
                "03/12/22",
                "11:38 PM",
                "J83NW",
                "Leonard N.",
                "Delivery",
                "32.23",
                "Completed"
            ),
            HistoryDetails(
                "03/12/22",
                "11:38 PM",
                "J83NW",
                "Leonard N.",
                "Delivery",
                "32.23",
                "Completed"
            ),
            HistoryDetails(
                "03/12/22",
                "11:38 PM",
                "J83NW",
                "Leonard N.",
                "Delivery",
                "32.23",
                "Completed"
            ),
            HistoryDetails(
                "03/12/22",
                "11:38 PM",
                "J83NW",
                "Leonard N.",
                "Delivery",
                "32.23",
                "Completed"
            )
        )
        binding!!.rvHistory.setHasFixedSize(true)
        binding!!.rvHistory.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryAdapter(details, this)
        binding!!.rvHistory.adapter = adapter
    }

    private fun showAlertDialog() {
        orders = arrayOf(
            OrderDetail("1x", "Cheeseburger", "$5.78", "- No Ketchup"),
            OrderDetail("2x", "Fries", "$2.39", ""),
            OrderDetail("1x", "Milk Shake", "$3.25", ""),
            OrderDetail("1x", "Garden Salad", "$3.25", ""),
            OrderDetail("1x", "Diet Coke", "$0.89", "")
        )
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater1 = layoutInflater
        val dialogView = inflater1.inflate(R.layout.dialog_history, null)
        val rvOrders: RecyclerView = dialogView.findViewById(R.id.rvFoodOrder)
        val ivCancelFullScreen = dialogView.findViewById<ImageView>(R.id.ivIcon2)
        rvOrders.setHasFixedSize(true)
        rvOrders.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailAdapter(orders)
        rvOrders.adapter = adapter
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        ivCancelFullScreen.setOnClickListener { v: View? -> alertDialog.dismiss() }
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT
        layoutParams.height = WindowManager.LayoutParams.FILL_PARENT
        alertDialog.window!!.attributes = layoutParams
    }

    override fun onItemSelect(position: Int) {
        showAlertDialog()
    }
}