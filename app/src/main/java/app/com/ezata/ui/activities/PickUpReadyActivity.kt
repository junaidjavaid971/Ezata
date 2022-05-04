package app.com.ezata.ui.activities

import androidx.appcompat.app.AppCompatActivity
import app.com.ezata.model.PickUpReady
import app.com.ezata.model.OnRouteOrders
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.com.ezata.R
import android.content.Intent
import app.com.ezata.ui.activities.HistoryActivity
import app.com.ezata.ui.activities.MainActivity
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.OnRouteOrderAdapter
import app.com.ezata.adapter.PickUpReadyAdapter
import app.com.ezata.databinding.ActivityPickUpReadyBinding

class PickUpReadyActivity : AppCompatActivity() {
    var binding: ActivityPickUpReadyBinding? = null
    lateinit var pickup: Array<PickUpReady>
    lateinit var orders: Array<OnRouteOrders>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_up_ready)
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
                )
            )
        }
        initReadyOrderRecyclerview()
        initOnRouteRecyclerview()
    }

    private fun openNavDrawer() {
        binding!!.drawer.openDrawer(Gravity.LEFT)
    }

    private fun initOnRouteRecyclerview() {
        orders = arrayOf(
            OnRouteOrders("KU17A", "Alex T.", R.drawable.white_vehicle, "10 Mins"),
            OnRouteOrders("JH17A", "David T.", R.drawable.white_vehicle, "14 Mins"),
            OnRouteOrders("KU98A", "John C.", R.drawable.white_vehicle, "19 Mins"),
            OnRouteOrders("KU17A", "Alex T.", R.drawable.white_vehicle, "32 Mins")
        )
        binding!!.rvOnRoute.setHasFixedSize(true)
        binding!!.rvOnRoute.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = OnRouteOrderAdapter(orders)
        binding!!.rvOnRoute.adapter = adapter
    }

    private fun initReadyOrderRecyclerview() {
        pickup = arrayOf(
            PickUpReady(
                "J83NW",
                "Alex T.",
                R.drawable.vehicle,
                "John S.",
                "18 Mins",
                "11:38 PM",
                "2356"
            ),
            PickUpReady(
                "J83NW",
                "Alex T.",
                R.drawable.vehicle,
                "John S.",
                "18 Mins",
                "11:38 PM",
                "2356"
            ),
            PickUpReady(
                "J83NW",
                "Alex T.",
                R.drawable.vehicle,
                "John S.",
                "18 Mins",
                "11:38 PM",
                "2356"
            ),
            PickUpReady(
                "J83NW",
                "Alex T.",
                R.drawable.vehicle,
                "John S.",
                "18 Mins",
                "11:38 PM",
                "2356"
            )
        )
        binding!!.rvPickup.setHasFixedSize(true)
        binding!!.rvPickup.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = PickUpReadyAdapter(pickup)
        binding!!.rvPickup.adapter = adapter
    }
}