package app.com.ezata.ui.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.AdapterView
import app.com.ezata.R
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import android.content.Intent
import app.com.ezata.ui.activities.HistoryActivity
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat
import app.com.ezata.ui.activities.PickUpReadyActivity
import android.net.NetworkRequest
import android.net.NetworkCapabilities
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.content.SharedPreferences
import android.graphics.Color
import app.com.ezata.ui.activities.MainActivity
import android.view.Gravity
import app.com.ezata.ui.fragments.InProgressOrderFragment
import app.com.ezata.ui.fragments.InProgressOrderDetailsFragment
import app.com.ezata.ui.fragments.OrderDetailFragment
import app.com.ezata.ui.fragments.NewOrderFragment
import android.view.LayoutInflater
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.widget.ImageView
import app.com.ezata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var binding: ActivityMainBinding? = null
    var state = intArrayOf(R.drawable.ic_active, R.drawable.ic_pause, R.drawable.ic_inactive)
    var State = "s"
    var text: String? = null
    var paused = "2"
    var pauseText: String? = "2"
    var menuState: String? = null
    var loadMenu: String? = "ActiveMenu"
    var ivActive: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ivActive = binding?.drawer?.findViewById(R.id.ivActive)
        binding?.inprogress?.visibility = View.GONE
        binding?.ivPrepareFoodSelect?.visibility = View.VISIBLE
        binding?.ivPrepareOrder?.visibility = View.GONE
        registerNetworkCallback()
        if (isOnline) {
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Internet is not connected",
                Snackbar.LENGTH_SHORT
            )
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        }
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
                )
            )
        }
        binding?.ivPrepareOrder?.setOnClickListener { view: View? ->
            binding?.ivPrepareOrder?.visibility = View.GONE
            binding?.ivPrepareFoodSelect?.visibility = View.VISIBLE
            binding?.ivPickupReady?.visibility = View.VISIBLE
            binding?.ivSelectPickup?.visibility = View.GONE
        }
    }

    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        var connectivityManager: ConnectivityManager? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager = getSystemService(ConnectivityManager::class.java)
        }
        connectivityManager?.requestNetwork(networkRequest, networkCallback)
    }

    val isOnline: Boolean
        get() {
            val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    var networkCallback: NetworkCallback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Internet is available now",
                Snackbar.LENGTH_SHORT
            )
            snackbar.setBackgroundTint(Color.GREEN)
            snackbar.show()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Internet is not connected",
                Snackbar.LENGTH_SHORT
            )
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
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
        ft.replace(R.id.framelayout1, InProgressOrderFragment())
        ft.replace(R.id.framelayout, InProgressOrderDetailsFragment())
        ft.commit()
    }

    private fun setOrderDetails() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout, OrderDetailFragment())
        ft.commit()
    }

    private fun setOrderFragment() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout1, NewOrderFragment())
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