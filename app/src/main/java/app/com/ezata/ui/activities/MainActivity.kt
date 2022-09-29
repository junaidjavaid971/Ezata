package app.com.ezata.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.com.ezata.R
import app.com.ezata.databinding.ActivityMainBinding
import app.com.ezata.databinding.ReadyForPickupCountdownDialogBinding
import app.com.ezata.model.OrderItems
import app.com.ezata.model.parse.OrdersMenuExtraItemParse
import app.com.ezata.model.parse.OrdersMenuParse
import app.com.ezata.model.parse.OrdersParse
import app.com.ezata.ui.fragments.InProgressOrderDetailsFragment
import app.com.ezata.ui.fragments.InProgressOrderFragment
import app.com.ezata.ui.fragments.NewOrderFragment
import app.com.ezata.ui.fragments.OrderDetailFragment
import app.com.ezata.utils.*
import app.com.ezata.utils.livedata.CountDownData
import app.com.ezata.utils.livedata.CountDownLiveData
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val TAG = "MainActivity"
    private val DEBUG = true

    var binding: ActivityMainBinding? = null
    var state = intArrayOf(R.drawable.ic_active, R.drawable.ic_pause, R.drawable.ic_inactive)
    var State = "s"
    var text: String? = null
    var paused = "2"
    var pauseText: String? = "2"
    var menuState: String? = null
    var loadMenu: String? = "ActiveMenu"
    var ivActive: ImageView? = null

    var activeOrders: List<OrdersParse?> = emptyList()
    var inProgressOrders: List<OrdersParse?> = emptyList()

    private val countDownLiveData = CountDownLiveData.getInstance()

    private lateinit var newOrderFragment: NewOrderFragment
    private lateinit var orderDetailsFragment: OrderDetailFragment
    private lateinit var inProgressOrderFragment: InProgressOrderFragment
    private lateinit var inProgressOrderDetailsFragment: InProgressOrderDetailsFragment

    private var currentWorkingOrder: OrdersParse? = null

    private var readyForPickupCountDownDialog: AlertDialog? = null

    private var readyForPickupCountdownDialogBinding: ReadyForPickupCountdownDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        init()

        setUpParseLiveQuery()

        NetworkConnectivity(this).observe(this, Observer {
            binding?.root ?: return@Observer
            if (it) {
                Snackbar.make(binding!!.root, "Network available", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding!!.root, "Network not available", Snackbar.LENGTH_SHORT).show()
            }
        })

        ivActive = binding?.drawer?.findViewById(R.id.ivActive)
        binding?.inprogress?.visibility = View.GONE
        binding?.ivPrepareFoodSelect?.visibility = View.VISIBLE
        binding?.ivPrepareOrder?.visibility = View.GONE

        binding?.drawer?.findViewById<View>(R.id.ivActive)?.setOnClickListener { v: View? ->
            if (binding?.drawer?.findViewById<View>(R.id.layoutNavigationItems)?.visibility == View.VISIBLE) {
                binding?.drawer?.findViewById<View>(R.id.layoutNavigationItems)?.visibility =
                    View.GONE
                binding?.drawer?.findViewById<View>(R.id.layouStatus)?.visibility = View.VISIBLE
            } else {
                binding?.drawer?.findViewById<View>(R.id.layoutNavigationItems)?.visibility =
                    View.VISIBLE
                binding?.drawer?.findViewById<View>(R.id.layouStatus)?.visibility = View.GONE
            }
        }

        binding?.navItem?.logout?.setOnClickListener {
            ParseUser.logOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding?.navItem?.tvEzataGo?.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        binding?.navItem?.navDrawer?.setOnClickListener {
        }

        manageStateClick()
        loadData()
        updateViews()
        binding?.ivHistory?.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    applicationContext, HistoryActivity::class.java
                )
            )
        }
        binding?.ivMenu?.setOnClickListener { view: View? -> openNavDrawer() }
        setNewOrderBgColor()
        setOrderFragment()
        setOrderDetails()
        binding?.llNewOrders?.setOnClickListener { view: View? ->
            binding?.inprogress?.visibility = View.GONE
            binding?.newOrders?.visibility = View.VISIBLE
            setNewOrderBgColor()
            var inProgress = binding?.llinProgress?.background
            inProgress = DrawableCompat.wrap(inProgress!!)
            DrawableCompat.setTint(inProgress, Color.rgb(39, 39, 39))
            setOrderFragment()
            setOrderDetails()
        }
        binding?.llinProgress?.setOnClickListener { view: View? ->
            setInProgressBgColor()
            var newOrder = binding?.llNewOrders?.background
            newOrder = DrawableCompat.wrap(newOrder!!)
            DrawableCompat.setTint(newOrder, Color.rgb(39, 39, 39))
            setInProgressFragment()
        }
        binding?.ivPickupReady?.setOnClickListener { view: View? ->
            startActivity(
                Intent(
                    applicationContext, PickUpReadyActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
        binding?.ivPrepareOrder?.setOnClickListener { view: View? ->
            binding?.ivPrepareOrder?.visibility = View.GONE
            binding?.ivPrepareFoodSelect?.visibility = View.VISIBLE
            binding?.ivPickupReady?.visibility = View.VISIBLE
            binding?.ivSelectPickup?.visibility = View.GONE
        }

        newOrderFragment.itemClicked = { orderItem ->
            showOrderDetails(orderItem)
            if (DEBUG) Log.d("Debug " + TAG, " onCreate: Item Clicked ${orderItem?.objectId}")
        }

        newOrderFragment.itemClicked = { orderItem ->
            showOrderDetails(orderItem)
            if (DEBUG) Log.d("Debug " + TAG, " onCreate: Item Clicked ${orderItem?.objectId}")
        }

        inProgressOrderFragment.itemClicked = { orderItem ->
            showOrderDetails(orderItem)
            if (DEBUG) Log.d("Debug " + TAG, " onCreate: Item Clicked ${orderItem?.objectId}")
        }

        inProgressOrderDetailsFragment.acceptOrderClicked = { orderItem ->
            currentWorkingOrder = orderItem
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
        }

        inProgressOrderDetailsFragment.changeStatusCountDownTimer = {
            currentWorkingOrder = it
            countDownLiveData.startCountDown()
            CountDownLiveData.setCurrentOrderParse(currentWorkingOrder)
        }

        readyForPickupCountdownDialogBinding?.readyForPickupButton?.setOnClickListener {
            changeCurrentWorkingOrderStatus(OrderStatus.PICKUP_READY)
        }

        readyForPickupCountdownDialogBinding?.inRouteButton?.setOnClickListener {
            changeCurrentWorkingOrderStatus(OrderStatus.IN_ROUTE)
        }

        readyForPickupCountdownDialogBinding?.cancelButton?.setOnClickListener {
            changeCurrentWorkingOrderStatus(OrderStatus.CANCELED)
        }

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
    }

    private fun changeCurrentWorkingOrderStatus(orderStatus: String) {
        currentWorkingOrder ?: return
        if (orderStatus == OrderStatus.CANCELED) {
            countDownLiveData.stopCountDown()
        }

        inProgressOrderDetailsFragment.showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            currentWorkingOrder?.orderStatus = orderStatus
            currentWorkingOrder?.save()
            withContext(Dispatchers.Main) {
                inProgressOrderDetailsFragment.hideLoading()
                inProgressOrderDetailsFragment.showOverlay()
                inProgressOrderDetailsFragment.hideLoading()

            }
        }
    }


    private fun init() {

        newOrderFragment = NewOrderFragment()
        orderDetailsFragment = OrderDetailFragment()
        inProgressOrderFragment = InProgressOrderFragment()
        inProgressOrderDetailsFragment = InProgressOrderDetailsFragment()

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

    private fun showOrderDetails(orderItem: OrdersParse?) = lifecycleScope.launch {

        if (orderItem?.orderStatus == OrderStatus.ACTIVE) {
            orderDetailsFragment.showLoading()
        } else if (orderItem?.orderStatus == OrderStatus.IN_PROGRESS) {
            inProgressOrderDetailsFragment.showLoading()
        }

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

            if (DEBUG) Log.d("Debug " + TAG, "$stringBuilder")
        }

        if (orderItem?.orderStatus == OrderStatus.ACTIVE) {
            if (DEBUG) Log.d("Debug " + TAG, " showOrderDetails: ACTIVE ")
            orderDetailsFragment.updateOrderDetails(orderItems, orderItem)
        } else if (orderItem?.orderStatus == OrderStatus.IN_PROGRESS) {
            if (DEBUG) Log.d("Debug " + TAG, " showOrderDetails: IN PROGGRESS ")
            inProgressOrderDetailsFragment.updateOrderDetails(orderItems, orderItem)
        }
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
                    orderDetailsFragment.valueChanged(updatedValue)
                    inProgressOrderDetailsFragment.valueChanged(updatedValue)
                },
                createdOrUpdatedOrDeleted = { errors, value ->
                    if (DEBUG) Log.d("Debug " + TAG, " setUpParseLiveQuery:  ")
                    loadOrders()
                },
            )
    }

    private fun loadOrders() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = ParseQuery(OrdersParse::class.java)
                .orderByDescending("createdAt")
                .include("userOrdered")
                .whereEqualTo(
                    "store",
                    ParseObject.createWithoutData(
                        "Store",
                        SharedPrefUtils.getString(this@MainActivity, SharedPrefKey.STORE_OBJECT_ID)
                    )
                )
                .findListInBackgroundCoroutine()

            activeOrders = result.filter { it.orderStatus == OrderStatus.ACTIVE }
            inProgressOrders = result.filter { it.orderStatus == OrderStatus.IN_PROGRESS }

            withContext(Dispatchers.Main) {
                newOrderFragment.setOrdersValue(activeOrders)
                inProgressOrderFragment.setOrdersValue(inProgressOrders, this@MainActivity)
            }
        }
    }


    private fun manageStateClick() {
        binding!!.drawer.findViewById<View>(R.id.layouStatus).findViewById<View>(R.id.activeLayout)
            .setOnClickListener { view: View? ->
                ivActive!!.setImageResource(state[0])
                binding!!.ivMenu.setImageResource(R.drawable.ic_menu_active)
                binding?.drawer?.findViewById<View>(R.id.layouStatus)
                    ?.findViewById<View>(R.id.activeCheck)?.visibility = View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.inactiveCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.pauseCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layoutNavigationItems).visibility =
                    View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout).visibility = View.GONE
                activeState()
            }
        binding!!.drawer.findViewById<View>(R.id.layouStatus).findViewById<View>(R.id.pauseLayout)
            .setOnClickListener { view: View? ->
                ivActive!!.setImageResource(state[1])
                binding!!.ivMenu.setImageResource(R.drawable.ic_menu_paused)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.activeCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.inactiveCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.pauseCheck).visibility = View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout).visibility = View.VISIBLE
                pauseState()
            }
        binding!!.drawer.findViewById<View>(R.id.layouStatus).findViewById<View>(R.id.timerLayout)
            .findViewById<View>(R.id.tv15M).setOnClickListener { view1: View? ->
                ivActive!!.setImageResource(state[1])
                binding!!.ivMenu.setImageResource(R.drawable.ic_menu_paused)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.activeCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.inactiveCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.pauseCheck).visibility = View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layoutNavigationItems).visibility =
                    View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv15M)
                    .setBackgroundResource(R.drawable.ic_black_circle)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv30M).setBackgroundResource(R.drawable.ic_circle)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv1H).setBackgroundResource(R.drawable.ic_circle)
                pauseText = "1"
                pauseState()
            }
        binding!!.drawer.findViewById<View>(R.id.layouStatus).findViewById<View>(R.id.timerLayout)
            .findViewById<View>(R.id.tv30M).setOnClickListener { view1: View? ->
                ivActive!!.setImageResource(state[1])
                binding!!.ivMenu.setImageResource(R.drawable.ic_menu_paused)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.activeCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.inactiveCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.pauseCheck).visibility = View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layoutNavigationItems).visibility =
                    View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv15M).setBackgroundResource(R.drawable.ic_circle)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv30M)
                    .setBackgroundResource(R.drawable.ic_black_circle)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv1H).setBackgroundResource(R.drawable.ic_circle)
                pauseText = "2"
                pauseState()
            }
        binding!!.drawer.findViewById<View>(R.id.layouStatus).findViewById<View>(R.id.timerLayout)
            .findViewById<View>(R.id.tv1H).setOnClickListener { view1: View? ->
                ivActive!!.setImageResource(state[1])
                binding!!.ivMenu.setImageResource(R.drawable.ic_menu_paused)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.activeCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.inactiveCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.pauseCheck).visibility = View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layoutNavigationItems).visibility =
                    View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv15M).setBackgroundResource(R.drawable.ic_circle)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv30M).setBackgroundResource(R.drawable.ic_circle)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout)
                    .findViewById<View>(R.id.tv1H).setBackgroundResource(R.drawable.ic_black_circle)
                pauseText = "3"
                pauseState()
            }
        binding!!.drawer.findViewById<View>(R.id.layouStatus)
            .findViewById<View>(R.id.inactiveLayout).setOnClickListener { view: View? ->
                ivActive!!.setImageResource(state[2])
                binding!!.ivMenu.setImageResource(R.drawable.ic_menu_inactive)
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.activeCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.inactiveCheck).visibility = View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.pauseCheck).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layoutNavigationItems).visibility =
                    View.VISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus).visibility = View.INVISIBLE
                binding!!.drawer.findViewById<View>(R.id.layouStatus)
                    .findViewById<View>(R.id.timerLayout).visibility = View.GONE
                inactiveState()
            }
    }

    private fun activeState() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(State, "Active")
        editor.putString(menuState, "ActiveMenu")
        editor.apply()
    }

    private fun pauseState() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(State, "Pause")
        editor.putString(paused, pauseText)
        editor.putString(menuState, "PauseMenu")
        editor.apply()
    }

    private fun inactiveState() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(State, "Inactive")
        editor.putString(menuState, "InActiveMenu")
        editor.apply()
    }

    fun loadData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        text = sharedPreferences.getString(State, "Active")
        pauseText = sharedPreferences.getString(paused, pauseText)
        loadMenu = sharedPreferences.getString(menuState, loadMenu)
    }

    fun updateViews() {
        if (text == "Active") {
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.activeCheck).visibility = View.VISIBLE
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.inactiveCheck).visibility =
                View.INVISIBLE
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.pauseCheck).visibility = View.INVISIBLE
            binding!!.ivMenu.setImageResource(R.drawable.ic_menu_active)
            ivActive!!.setImageResource(state[0])
        }
        if (text == "Pause") {
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.activeCheck).visibility = View.INVISIBLE
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.inactiveCheck).visibility =
                View.INVISIBLE
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.pauseCheck).visibility = View.VISIBLE
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout).visibility = View.VISIBLE
            binding!!.ivMenu.setImageResource(R.drawable.ic_menu_paused)
            ivActive!!.setImageResource(state[1])
        }
        if (text == "Inactive") {
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.activeCheck).visibility = View.INVISIBLE
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.inactiveCheck).visibility = View.VISIBLE
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.pauseCheck).visibility = View.INVISIBLE
            binding!!.ivMenu.setImageResource(R.drawable.ic_menu_inactive)
            ivActive!!.setImageResource(state[2])
        }
        if (pauseText == "1") {
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv15M).setBackgroundResource(R.drawable.ic_black_circle)
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv30M).setBackgroundResource(R.drawable.ic_circle)
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv1H).setBackgroundResource(R.drawable.ic_circle)
        }
        if (pauseText == "2") {
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv15M).setBackgroundResource(R.drawable.ic_circle)
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv30M).setBackgroundResource(R.drawable.ic_black_circle)
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv1H).setBackgroundResource(R.drawable.ic_circle)
        }
        if (pauseText == "3") {
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv15M).setBackgroundResource(R.drawable.ic_circle)
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv30M).setBackgroundResource(R.drawable.ic_circle)
            binding!!.drawer.findViewById<View>(R.id.layouStatus)
                .findViewById<View>(R.id.timerLayout)
                .findViewById<View>(R.id.tv1H).setBackgroundResource(R.drawable.ic_black_circle)
        }
    }

    private fun openNavDrawer() {
        binding!!.drawer.openDrawer(Gravity.LEFT)
    }

    private fun setInProgressBgColor() {
        var inProgress = binding!!.llinProgress.background
        inProgress = DrawableCompat.wrap(inProgress!!)
        DrawableCompat.setTint(inProgress, Color.rgb(104, 107, 113))
    }

    private fun setNewOrderBgColor() {
        var linear = binding!!.llNewOrders.background
        linear = DrawableCompat.wrap(linear!!)
        DrawableCompat.setTint(linear, Color.rgb(104, 107, 113))
    }

    private fun setInProgressFragment() {
        binding!!.inprogress.visibility = View.VISIBLE
        binding!!.newOrders.visibility = View.GONE
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout1, inProgressOrderFragment)
        ft.replace(R.id.framelayout, inProgressOrderDetailsFragment)
        ft.commit()
    }

    private fun setOrderDetails() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout, orderDetailsFragment)
        ft.commit()
    }

    private fun setOrderFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout1, newOrderFragment)
        ft.commit()
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
        val dialogBuilder = AlertDialog.Builder(this@MainActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.select_merchant_state, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    companion object {
        const val SHARED_PREFS = "sharedPrefs"
    }
}