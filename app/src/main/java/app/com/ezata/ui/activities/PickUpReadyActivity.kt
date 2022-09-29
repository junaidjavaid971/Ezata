package app.com.ezata.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.R
import app.com.ezata.adapter.OnRouteOrderAdapter
import app.com.ezata.adapter.PickUpReadyAdapter
import app.com.ezata.databinding.ActivityPickUpReadyBinding
import app.com.ezata.databinding.OnRouteCountdownDialogBinding
import app.com.ezata.databinding.ReadyForPickupCountdownDialogBinding
import app.com.ezata.model.OnRouteOrders
import app.com.ezata.model.PickUpReady
import app.com.ezata.model.parse.OrdersParse
import app.com.ezata.utils.*
import app.com.ezata.utils.livedata.CountDownData
import app.com.ezata.utils.livedata.CountDownLiveData
import app.com.ezata.utils.livedata.OnRouteCountDownLiveData
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PickUpReadyActivity : AppCompatActivity() {
    var binding: ActivityPickUpReadyBinding? = null
    lateinit var pickup: Array<PickUpReady>
    lateinit var orders: Array<OnRouteOrders>
    var activeOrders: List<OrdersParse?> = emptyList()
    var inRouteOrders: List<OrdersParse?> = emptyList()

    private var readyForPickupCountDownDialog: AlertDialog? = null
    private var readyForPickupCountdownDialogBinding: ReadyForPickupCountdownDialogBinding? = null
    private var currentWorkingOrder: OrdersParse? = null
    private val countDownLiveData = CountDownLiveData.getInstance()

    private var onRouteCountDownDialog: AlertDialog? = null
    private var onRouteCountdownDialogBinding: OnRouteCountdownDialogBinding? = null
    private var currentOnRouteOrder: OrdersParse? = null
    private val onRouteCountDownLiveData = OnRouteCountDownLiveData.getInstance()

    private var adapter: PickUpReadyAdapter? = null
    private var onRouteAdapter: OnRouteOrderAdapter? = null
    private val DEBUG = true
    private val TAG = "PickUpReadyAct"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_up_ready)

        setupClickListeners()
        setUpParseLiveQuery()
    }

    private fun setupClickListeners() {
        binding?.ivHistory?.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    applicationContext, HistoryActivity::class.java
                )
            )
        }
        binding?.ivMenu?.setOnClickListener { view: View? -> openNavDrawer() }

        binding?.ivPrepareOrder?.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    applicationContext, MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    private fun openNavDrawer() {
        binding!!.drawer.openDrawer(Gravity.LEFT)
    }

    private fun setUpParseLiveQuery() {
        if (DEBUG) Log.d(
            "Debug " + TAG,
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
                .include("deliveryPerson")
                .whereEqualTo(
                    "store",
                    ParseObject.createWithoutData(
                        "Store",
                        SharedPrefUtils.getString(
                            this@PickUpReadyActivity,
                            SharedPrefKey.STORE_OBJECT_ID
                        )
                    )
                )
                .findListInBackgroundCoroutine()

            activeOrders = result.filter {
                it.orderStatus == OrderStatus.PICKUP_READY ||
                it.orderStatus == OrderStatus.IN_ROUTE
            }

            inRouteOrders = result.filter {
                it.orderStatus == OrderStatus.LAST_MILE
            }

            withContext(Dispatchers.Main) {
                setOrdersValue(activeOrders)
                setInOrdersValue(inRouteOrders)
            }
        }
    }

    private fun setOrdersValue(mOrders: List<OrdersParse?>) {
        activeOrders = mOrders

        binding?.overlay?.visibility = View.GONE
        binding?.loadingLayout?.root?.visibility = View.GONE

        binding!!.rvPickup.setHasFixedSize(true)
        binding!!.rvPickup.layoutManager = LinearLayoutManager(applicationContext)
        adapter = PickUpReadyAdapter(activeOrders, this@PickUpReadyActivity)
        binding!!.rvPickup.adapter = adapter

        setupReadyForPickupDialog()
        Log.d(TAG, activeOrders.size.toString())
    }

    private fun setupReadyForPickupDialog() {
        readyForPickupCountdownDialogBinding =
            ReadyForPickupCountdownDialogBinding.inflate(layoutInflater)

        readyForPickupCountDownDialog = AlertDialog.Builder(this).apply {
            setView(readyForPickupCountdownDialogBinding?.root)
            setCancelable(false)
        }.apply {
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }.create()

        adapter?.changeStatusCountDownTimer = {
            currentWorkingOrder = it
            countDownLiveData.startCountDown()
            CountDownLiveData.setCurrentOrderParse(it)
        }
/*
        adapter?.changeStatusCountDownTimer = {
            currentWorkingOrder = it
            countDownLiveData.startCountDown()
            CountDownLiveData.setCurrentOrderParse(it)
        }*/
        countDownLiveData.observe(this, Observer {
            when (it) {
                is CountDownData.Finished -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        currentWorkingOrder?.let { _ordersParse ->
                            val nOrderParse =
                                ParseQuery(OrdersParse::class.java).findInBackgroundCoroutine(
                                    _ordersParse.objectId
                                )
                            if (nOrderParse.orderStatus == OrderStatus.IN_PROGRESS) {
                                currentWorkingOrder = nOrderParse
                                changeCurrentWorkingOrderStatus(OrderStatus.PICKUP_READY)
                            }
                        }
                    }
                    hideReadyForPickupCountDownDialog()
                }
                is CountDownData.Ticking -> {
                    showReadyForPickupCountDownDialog(it.tick)
                }
                else -> hideReadyForPickupCountDownDialog()
            }
        })

        readyForPickupCountdownDialogBinding?.readyForPickupButton?.setOnClickListener {
            binding?.onRouteLoader?.root?.visibility = View.VISIBLE
            binding?.onRouteOverlay?.visibility = View.VISIBLE

            changeCurrentWorkingOrderStatus(OrderStatus.PICKUP_READY)
        }

        readyForPickupCountdownDialogBinding?.inRouteButton?.setOnClickListener {
            changeCurrentWorkingOrderStatus(OrderStatus.IN_ROUTE)
        }

        readyForPickupCountdownDialogBinding?.cancelButton?.setOnClickListener {
            changeCurrentWorkingOrderStatus(OrderStatus.CANCELED)
        }

        readyForPickupCountdownDialogBinding?.tvClosePopup?.setOnClickListener {
            countDownLiveData.stopCountDown()
        }
    }

    private fun setupOnRouteDialog() {
        onRouteCountdownDialogBinding =
            OnRouteCountdownDialogBinding.inflate(layoutInflater)

        onRouteCountDownDialog = AlertDialog.Builder(this).apply {
            setView(onRouteCountdownDialogBinding?.root)
            setCancelable(false)
        }.apply {
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }.create()

        onRouteAdapter?.onRouteCountDownTimer = {
            currentOnRouteOrder = it
            onRouteCountDownLiveData.startCountDown()
            CountDownLiveData.setCurrentOrderParse(it)
        }

        onRouteCountDownLiveData.observe(this, Observer {
            when (it) {
                is CountDownData.Finished -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        currentOnRouteOrder?.let { _ordersParse ->
                            val nOrderParse =
                                ParseQuery(OrdersParse::class.java).findInBackgroundCoroutine(
                                    _ordersParse.objectId
                                )
                            if (nOrderParse.orderStatus == OrderStatus.IN_ROUTE) {
                                currentWorkingOrder = nOrderParse
                                changeCurrentWorkingOrderStatus(OrderStatus.CANCELED)
                            }
                        }
                    }
                    hideOnRouteCountDownDialog()
                }
                is CountDownData.Ticking -> {
                    showOnRouteCountDownDialog(it.tick)
                }
                else -> hideOnRouteCountDownDialog()
            }
        })

        onRouteCountdownDialogBinding?.cancelButton?.setOnClickListener {
            cancelOrder(OrderStatus.CANCELED)
        }
        onRouteCountdownDialogBinding?.tvClosePopup?.setOnClickListener {
            onRouteCountDownLiveData.stopCountDown()
        }
    }

    private fun setInOrdersValue(mOrders: List<OrdersParse?>) {
        inRouteOrders = mOrders

        binding?.onRouteOverlay?.visibility = View.GONE
        binding?.onRouteLoader?.root?.visibility = View.GONE

        binding!!.rvOnRoute.setHasFixedSize(true)
        binding!!.rvOnRoute.layoutManager = LinearLayoutManager(applicationContext)
        onRouteAdapter = OnRouteOrderAdapter(inRouteOrders)
        binding!!.rvOnRoute.adapter = onRouteAdapter

        setupOnRouteDialog()
        Log.d(TAG, activeOrders.size.toString())
    }


    private fun hideReadyForPickupCountDownDialog() {
        if (readyForPickupCountDownDialog?.isShowing == true) {
            readyForPickupCountDownDialog?.dismiss()
        }
    }

    private fun showReadyForPickupCountDownDialog(tick: Long) {
        readyForPickupCountdownDialogBinding?.countDownText?.text = "${tick / 1000}s"

        if (readyForPickupCountDownDialog?.isShowing == false) {
            readyForPickupCountDownDialog?.show()
        }
    }

    private fun hideOnRouteCountDownDialog() {
        if (onRouteCountDownDialog?.isShowing == true) {
            onRouteCountDownDialog?.dismiss()
        }
    }

    private fun showOnRouteCountDownDialog(tick: Long) {
        onRouteCountdownDialogBinding?.countDownText?.text = "${tick / 1000}s"

        if (onRouteCountDownDialog?.isShowing == false) {
            onRouteCountDownDialog?.show()
        }
    }

    private fun changeCurrentWorkingOrderStatus(orderStatus: String) {
        currentWorkingOrder ?: return
        if (orderStatus == OrderStatus.CANCELED) {
            countDownLiveData.stopCountDown()
            onRouteCountDownLiveData.stopCountDown()
        }

        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            currentWorkingOrder?.orderStatus = orderStatus
            currentWorkingOrder?.save()
            withContext(Dispatchers.Main) {
                hideLoading()
                showOverlay()
                hideLoading()
                countDownLiveData.stopCountDown()
            }
        }
    }

    private fun cancelOrder(orderStatus: String) {
        currentOnRouteOrder ?: return
        if (orderStatus == OrderStatus.CANCELED) {
            onRouteCountDownLiveData.stopCountDown()
        }

        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            currentOnRouteOrder?.orderStatus = orderStatus
            currentOnRouteOrder?.save()
            withContext(Dispatchers.Main) {
                hideLoading()
                showOverlay()
                hideLoading()
                onRouteCountDownLiveData.stopCountDown()
            }
        }
    }

    fun showLoading() {
        try {
            binding?.loadingLayout?.root?.visibility = View.VISIBLE
            binding?.onRouteLoader?.root?.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }

    fun hideLoading() {
        try {
            binding?.loadingLayout?.root?.visibility = View.GONE
            binding?.onRouteLoader?.root?.visibility = View.GONE
            binding?.onRouteOverlay?.visibility = View.GONE
        } catch (e: Exception) {

        }
    }

    fun showOverlay() {
        try {
            binding?.overlay?.visibility = View.VISIBLE
            binding?.onRouteOverlay?.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }
}