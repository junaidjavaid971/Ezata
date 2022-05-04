package app.com.ezata.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import app.com.ezata.model.OrderDetail
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.com.ezata.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.OrderDetailAdapter
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import app.com.ezata.databinding.FragmentOrderDetailBinding

class OrderDetailFragment : Fragment() {
    var binding: FragmentOrderDetailBinding? = null
    lateinit var orders: Array<OrderDetail>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_detail, container, false)
        val view = binding?.root
        initRecyclerview()
        binding?.ivFullsize?.setOnClickListener { view1: View? -> showAlertDialog() }
        return view
    }

    private fun showAlertDialog() {
        orders = arrayOf(
            OrderDetail("1x", "Cheeseburger", "$5.78", "- No Ketchup"),
            OrderDetail("2x", "Fries", "$2.39", ""),
            OrderDetail("1x", "Milk Shake", "$3.25", ""),
            OrderDetail("1x", "Garden Salad", "$3.25", ""),
            OrderDetail("1x", "Diet Coke", "$0.89", "")
        )
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater1 = layoutInflater
        val dialogView = inflater1.inflate(R.layout.dialog_neworder, null)
        val rvOrders: RecyclerView = dialogView.findViewById(R.id.rvFoodOrder)
        val icCancelFullScreen = dialogView.findViewById<ImageView>(R.id.ivFullsize)
        rvOrders.setHasFixedSize(true)
        rvOrders.layoutManager = LinearLayoutManager(activity)
        val adapter = OrderDetailAdapter(orders)
        rvOrders.adapter = adapter
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        icCancelFullScreen.setOnClickListener { v: View? -> alertDialog.dismiss() }
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.FILL_PARENT
        layoutParams.height = WindowManager.LayoutParams.FILL_PARENT
        alertDialog.window!!.attributes = layoutParams
    }

    private fun initRecyclerview() {
        orders = arrayOf(
            OrderDetail("1x", "Cheeseburger", "$5.78", "- No Ketchup"),
            OrderDetail("2x", "Fries", "$2.39", ""),
            OrderDetail("1x", "Milk Shake", "$3.25", ""),
            OrderDetail("1x", "Garden Salad", "$3.25", ""),
            OrderDetail("1x", "Diet Coke", "$0.89", "")
        )
        binding!!.rvFoodOrder.setHasFixedSize(true)
        binding!!.rvFoodOrder.layoutManager = LinearLayoutManager(activity)
        val adapter = OrderDetailAdapter(orders)
        binding!!.rvFoodOrder.adapter = adapter
    }
}