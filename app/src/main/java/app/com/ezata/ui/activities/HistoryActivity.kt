package app.com.ezata.ui.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.com.ezata.OnItemClicked
import app.com.ezata.model.HistoryDetails
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.com.ezata.R
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.HistoryAdapter
import androidx.recyclerview.widget.RecyclerView
import app.com.ezata.adapter.OrderDetailAdapter
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import app.com.ezata.databinding.ActivityHistoryBinding
import app.com.ezata.model.OrderItems
import app.com.ezata.model.parse.OrdersMenuExtraItemParse
import app.com.ezata.model.parse.OrdersMenuParse
import app.com.ezata.model.parse.OrdersParse
import app.com.ezata.utils.*
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity(), OnItemClicked {
    var binding: ActivityHistoryBinding? = null
    lateinit var details: Array<HistoryDetails>
    private var currentOrder: OrdersParse? = null
    lateinit var orders: ArrayList<OrdersParse?>

    private val DEBUG = true
    private val TAG = "HistoryActivity"
    private var orderDetailAdapter: OrderDetailAdapter = OrderDetailAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)

        setUpParseLiveQuery()
        manageClicks()
    }

    private fun manageClicks() {
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

    private fun showAlertDialog(orderItems: ArrayList<OrderItems>, orderItem: OrdersParse) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater1 = layoutInflater

        val dialogView = inflater1.inflate(R.layout.dialog_history, null)
        val rvOrders: RecyclerView = dialogView.findViewById(R.id.rvFoodOrder)
        val ivCancelFullScreen = dialogView.findViewById<ImageView>(R.id.ivIcon2)
        val tvCustomerName = dialogView.findViewById<TextView>(R.id.tvCustomerName)
        val tvDeliveredOn = dialogView.findViewById<TextView>(R.id.tvDelieverdTime)
        val tvSubTotal = dialogView.findViewById<TextView>(R.id.tvSubTotal)
        val tvTotal = dialogView.findViewById<TextView>(R.id.tvTotal)
        val tvTax = dialogView.findViewById<TextView>(R.id.tvTax)

        orderItem.user?.fetchIfNeeded()?.let {
            tvCustomerName.text =
                "${it.getString("first_name") ?: ""} ${it.getString("last_name") ?: ""}" ?: "N/A"
        }

        tvDeliveredOn.text = getDateTime(orderItem.createdAt.toString())

        rvOrders.setHasFixedSize(true)
        rvOrders.layoutManager = LinearLayoutManager(this)
        rvOrders.adapter = orderDetailAdapter

        currentOrder = orderItem
        orderDetailAdapter.orderItems = orderItems
        val subTotal = orderItems.sumOf {
            it.ordersMenuParse?.price?.toDouble() ?: 0.0
        }

        val tax = getTax(subTotal)

        tvSubTotal.text = "$$subTotal"
        tvTax.text = "$$tax"
        tvTotal.text = "$" + (subTotal + tax).toString()

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

    private fun getTax(total: Double): Double {
        return total * (15.0 / 100.0)
    }

    private fun setUpParseLiveQuery() {
        if (DEBUG) Log.d(
            "Debug $TAG",
            " setUpParseLiveQuery: Store id ${
                SharedPrefUtils.getString(
                    this,
                    SharedPrefKey.STORE_OBJECT_ID
                )
            } "
        )
        loadOrders()
        ParseQuery(OrdersParse::class.java)
            .whereEqualTo(
                "store",
                ParseObject.createWithoutData(
                    "Store",
                    SharedPrefUtils.getString(this, SharedPrefKey.STORE_OBJECT_ID)
                )
            )
            .orderByDescending("created_at")
            .parseLiveQuery(
                context = this,
                lifecycle = lifecycle,
                updated = { errors, updatedValue ->
                    /*orderDetailsFragment.valueChanged(updatedValue)
                    inProgressOrderDetailsFragment.valueChanged(updatedValue)*/
                },
                createdOrUpdatedOrDeleted = { errors, value ->
                    if (DEBUG) Log.d("Debug $TAG", " setUpParseLiveQuery:  ")
                    loadOrders()
                },
            )
    }

    private fun loadOrders() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = ParseQuery(OrdersParse::class.java)
                .orderByDescending("createdAt")
                .include("userOrdered")
                .include("createdAt")
                .whereEqualTo(
                    "store",
                    ParseObject.createWithoutData(
                        "Store",
                        SharedPrefUtils.getString(
                            this@HistoryActivity,
                            SharedPrefKey.STORE_OBJECT_ID
                        )
                    )
                )
                .findListInBackgroundCoroutine()

            orders = result.filter {
                it.orderStatus == OrderStatus.COMPLETED
            } as ArrayList<OrdersParse?>

            withContext(Dispatchers.Main) {
                setOrdersValue(orders)
            }
        }
    }

    private fun showOrderDetails(orderItem: OrdersParse?) = lifecycleScope.launch {
        val orderItems = arrayListOf<OrderItems>()
        val result = ParseQuery(OrdersMenuParse::class.java)
            .whereEqualTo("orderId", orderItem)
            .include("menuItemId")
            .include("ParentCategory")
            .findListInBackgroundCoroutine()

        result.forEach { orderMenu ->
            val stringBuilder = StringBuilder()

            stringBuilder.append("Dish -> ${orderMenu.menu?.dishTitle} ")
            stringBuilder.append("Quantity -> ${orderMenu.quantity} ")
            stringBuilder.append("Price -> ${orderMenu.price}\n")

            val extraItems = ParseQuery(OrdersMenuExtraItemParse::class.java)
                .whereEqualTo("ParentOrderMenu", orderMenu)
                .include("ParentMenuExtra")
                .include("ParentMenuExtraGroup")
                .findListInBackgroundCoroutine()

            extraItems.forEach {
                stringBuilder.append("Extra -> ${it.menuExtraItem?.name} Price -> ${it.menuExtraItem?.price}\n")
            }

            orderItems.add(OrderItems(orderMenu, extraItems))

            if (DEBUG) Log.d("Debug $TAG", "$stringBuilder")
        }

        if (orderItem?.orderStatus == OrderStatus.COMPLETED) {
            showAlertDialog(orderItems, orderItem)
        }
    }

    private fun setOrdersValue(mOrders: ArrayList<OrdersParse?>) {
        orders = mOrders

        binding?.overlay?.visibility = View.GONE
        binding?.loader?.root?.visibility = View.GONE

        binding!!.rvHistory.setHasFixedSize(true)
        binding!!.rvHistory.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = HistoryAdapter(orders, this)
        binding!!.rvHistory.adapter = adapter
    }


    override fun onItemSelect(ordersParse: OrdersParse) {
        showOrderDetails(ordersParse)
    }


    private fun getDateTime(orderDate: String): String {
        val dateFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
        val date: Date? = dateFormatter.parse(orderDate)

        val formatter2 = SimpleDateFormat("yyyy-MM-dd HH:mm")

        return formatter2.format(date)
    }
}