package app.com.ezata.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.OrderDetailAdapter
import app.com.ezata.databinding.DialogInprogressBinding
import app.com.ezata.databinding.FragmentInProgressOrderDetailsBinding
import app.com.ezata.model.OrderItems
import app.com.ezata.model.parse.OrdersParse
import app.com.ezata.utils.OrderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InProgressOrderDetailsFragment : Fragment() {

    private val TAG = "InProgressOrderDetF"
    private val DEBUG = true

    private var _binding: FragmentInProgressOrderDetailsBinding? = null
    val binding get() = _binding!!

    lateinit var orders: ArrayList<OrderItems?>
    private var currentOrder: OrdersParse? = null

    val readyForPickupLiveData : MutableLiveData<OrdersParse?> = MutableLiveData<OrdersParse?>()
    var acceptOrderClicked: ((orderItem: OrdersParse?) -> Unit)? = null
    private var orderDetailAdapter: OrderDetailAdapter = OrderDetailAdapter()

    var changeStatusCountDownTimer: ((currentOrder: OrdersParse?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInProgressOrderDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecylerview()
        binding.ivFullsize.setOnClickListener { view1: View? -> showALertDialog() }

        binding.btnAcceptOrder.setOnClickListener {
//            changeStatusCountDownTimer?.invoke(currentOrder)
            acceptOrderClicked?.invoke(currentOrder)
//            readyForPickup()
        }

        binding.btnCancelOrder.setOnClickListener {
            adjustOrder()
        }
    }

    fun valueChanged(ordersParse: OrdersParse?) {
        if (ordersParse?.objectId == currentOrder?.objectId
            && ordersParse?.orderStatus !== currentOrder?.orderStatus
        ) {
            if (DEBUG) Log.d("Debug " + TAG, " valueChanged:  ")
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    try {
                        binding.overlay.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        if (DEBUG) Log.d("Debug " + TAG, " valueChanged: Error ${e.message} ")
                    }
                }
            }
        }
    }

    private fun showALertDialog() {
        val dialogBuilder = AlertDialog.Builder(activity)
        val dialogBinding = DialogInprogressBinding.inflate(layoutInflater)

        dialogBinding.rvFoodOrder.setHasFixedSize(true)
        dialogBinding.rvFoodOrder.layoutManager = LinearLayoutManager(activity)
        dialogBinding.rvFoodOrder.adapter = orderDetailAdapter
        dialogBuilder.setView(dialogBinding.root)

        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        dialogBinding.ivFullsize.setOnClickListener { v: View? -> alertDialog.dismiss() }
        alertDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )


        dialogBinding.apply {
            btnAcceptOrder.setOnClickListener {
                alertDialog.dismiss()
                readyForPickupLiveData.postValue(currentOrder)
//                changeStatusCountDownTimer?.invoke(currentOrder)
//                readyForPickup()
            }
            btnCancelOrder.setOnClickListener {
                alertDialog.dismiss()
                adjustOrder()
            }
            tvOrderNumber.text = binding.tvOrderNumber.text
            tvCustomerName.text = binding.tvCustomerName.text
            totalItemsTv.text = binding.totalItemsTv.text
            tvSubTotal.text = binding.tvSubTotal.text
            tvSubTotal.text = binding.tvSubTotal.text
            tvTax.text = binding.tvTax.text
            tvTotal.text = binding.tvTotal.text
        }

    }

    private fun initRecylerview() {
        binding.rvFoodOrder.setHasFixedSize(true)
        binding.rvFoodOrder.layoutManager = LinearLayoutManager(activity)
        val adapter = orderDetailAdapter
        binding.rvFoodOrder.adapter = adapter
    }

    fun updateOrderDetails(orderItems: ArrayList<OrderItems>, ordersParse: OrdersParse?) {
        currentOrder = ordersParse
        orderDetailAdapter.orderItems = orderItems
        val subTotal = orderItems.sumOf {
            it.ordersMenuParse?.price?.toDouble() ?: 0.0
        }

        val tax = getTax(subTotal)

        binding.apply {
            tvOrderNumber.text = ordersParse?.orderNumber ?: "Not Set"
            tvCustomerName.text =
                "${ordersParse?.user?.get("first_name")} ${ordersParse?.user?.get("last_name")}"
            totalItemsTv.text = "${orderItems.size} items"
            tvSubTotal.text = "Subtotal $${"%.2f".format(subTotal)}"
            tvTax.text = "Tax(15%) $${"%.2f".format(tax)}"
            tvTotal.text = "Total $${"%.2f".format(subTotal + tax)}"
            overlay.visibility = View.GONE
            hideLoading()
        }

    }

    fun showLoading() {
        try {
            binding.loadingLayout.root.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }

    fun hideLoading() {
        try {
            binding.loadingLayout.root.visibility = View.GONE
        } catch (e: Exception) {

        }
    }

    private fun getTax(total: Double): Double {
        return total * (15.0 / 100.0)
    }

    private fun readyForPickup() {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            currentOrder?.orderStatus = OrderStatus.PICKUP_READY
            currentOrder?.save()
            withContext(Dispatchers.Main) {
                binding.overlay.visibility = View.VISIBLE
                hideLoading()
            }
        }
    }

    fun showOverlay() {
        try {
            binding.overlay.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }

    private fun adjustOrder() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}