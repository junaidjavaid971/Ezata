package app.com.ezata.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.OrderDetailAdapter
import app.com.ezata.databinding.DialogNeworderBinding
import app.com.ezata.databinding.FragmentOrderDetailBinding
import app.com.ezata.model.OrderItems
import app.com.ezata.model.parse.OrdersParse
import app.com.ezata.utils.OrderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderDetailFragment : Fragment() {

    private val TAG = "OrderDetailFragment"
    private val DEBUG = true

    private var _binding: FragmentOrderDetailBinding? = null
    val binding get() = _binding!!

    lateinit var orders: ArrayList<OrderItems?>
    private var currentOrder: OrdersParse? = null

    private lateinit var orderDetailAdapter: OrderDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerview()
        binding.ivFullsize.setOnClickListener { view1: View? -> showAlertDialog() }

        binding.btnAcceptOrder.setOnClickListener {
            acceptOrder()
        }

        binding.btnCancelOrder.setOnClickListener {
            cancelOrder()
        }

    }

    fun valueChanged(ordersParse: OrdersParse?) {
        if (ordersParse?.objectId == currentOrder?.objectId
            && ordersParse?.orderStatus !== currentOrder?.orderStatus
        ) {
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    try {
                        binding.overlay.visibility = View.VISIBLE
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    private fun acceptOrder() {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            currentOrder?.orderStatus = OrderStatus.IN_PROGRESS
            currentOrder?.save()
            withContext(Dispatchers.Main) {
                binding.overlay.visibility = View.VISIBLE
                hideLoading()
            }
        }
    }

    private fun cancelOrder() {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            currentOrder?.orderStatus = OrderStatus.CANCELED
            currentOrder?.save()
            withContext(Dispatchers.Main) {
                binding.overlay.visibility = View.VISIBLE
                hideLoading()
            }
        }
    }

    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(activity)
        val dialogBinding = DialogNeworderBinding.inflate(layoutInflater)
        dialogBinding.rvFoodOrder.setHasFixedSize(true)
        dialogBinding.rvFoodOrder.layoutManager = LinearLayoutManager(activity)
        dialogBinding.rvFoodOrder.adapter = orderDetailAdapter
        dialogBuilder.setView(dialogBinding.root)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        dialogBinding.ivFullsize.setOnClickListener { v: View? -> alertDialog.dismiss() }
        //    alertDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        alertDialog.window!!.setLayout(1400, WindowManager.LayoutParams.MATCH_PARENT)
        dialogBinding.apply {
            btnAcceptOrder.setOnClickListener {
                alertDialog.dismiss()
                acceptOrder()
            }
            btnCancelOrder.setOnClickListener {
                alertDialog.dismiss()
                cancelOrder()
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


    private fun initRecyclerview() {
        binding.rvFoodOrder.setHasFixedSize(true)
        binding.rvFoodOrder.layoutManager = LinearLayoutManager(activity)
        orderDetailAdapter = OrderDetailAdapter()
        binding.rvFoodOrder.adapter = orderDetailAdapter
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
                "${ordersParse?.user?.get("first_name") ?: ""} ${ordersParse?.user?.get("last_name") ?: ""}".ifBlank { "Customer Name" }
            totalItemsTv.text = "${orderItems.size} items"
            tvSubTotal.text = "Subtotal $${"%.2f".format(subTotal)}"
            tvTax.text = "Tax(15%) $${"%.2f".format(tax)}"
            tvTotal.text = "Total $${"%.2f".format(subTotal + tax)}"
            overlay.visibility = View.GONE
            hideLoading()
        }

    }

    fun showLoading() {
        binding.loadingLayout.root.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.loadingLayout.root.visibility = View.GONE
    }

    private fun getTax(total: Double): Double {
        return total * (15.0 / 100.0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }
}