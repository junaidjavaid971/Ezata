package app.com.ezata.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import app.com.ezata.model.InProgressOrderDetail
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.com.ezata.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.InProgressOrderDetailAdapter
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import app.com.ezata.databinding.FragmentInProgressOrderDetailsBinding

class InProgressOrderDetailsFragment : Fragment() {
    var binding: FragmentInProgressOrderDetailsBinding? = null
    lateinit var orders: Array<InProgressOrderDetail>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_in_progress_order_details,
            container,
            false
        )
        val view = binding?.root
        binding?.ivFullsize?.setOnClickListener { showALertDialog() }
        initRecylerview()
        return view
    }

    private fun showALertDialog() {
        orders = arrayOf(
            InProgressOrderDetail("1x", "Cheeseburger", "$5.78", "-No Ketchup"),
            InProgressOrderDetail("2x", "Fries", "$2.39", ""),
            InProgressOrderDetail("1x", "Vanilla Milkshake", "$1.82", ""),
            InProgressOrderDetail("1x", "Garden Salad.", "$3.25", ""),
            InProgressOrderDetail("1x", "Diet Coke.", "$0.89", "")
        )
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater1 = layoutInflater
        val dialogView = inflater1.inflate(R.layout.dialog_inprogress, null)
        val rvOrders: RecyclerView = dialogView.findViewById(R.id.rvFoodOrder)
        val icCancelFullScreen = dialogView.findViewById<ImageView>(R.id.ivFullsize)
        rvOrders.setHasFixedSize(true)
        rvOrders.layoutManager = LinearLayoutManager(activity)
        val adapter = InProgressOrderDetailAdapter(orders)
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

    private fun initRecylerview() {
        orders = arrayOf(
            InProgressOrderDetail("1x", "Cheeseburger", "$5.78", "-No Ketchup"),
            InProgressOrderDetail("2x", "Fries", "$2.39", ""),
            InProgressOrderDetail("1x", "Vanilla Milkshake", "$1.82", ""),
            InProgressOrderDetail("1x", "Garden Salad.", "$3.25", ""),
            InProgressOrderDetail("1x", "Diet Coke.", "$0.89", "")
        )
        binding!!.rvFoodOrder.setHasFixedSize(true)
        binding!!.rvFoodOrder.layoutManager = LinearLayoutManager(activity)
        val adapter = InProgressOrderDetailAdapter(orders)
        binding!!.rvFoodOrder.adapter = adapter
    }
}