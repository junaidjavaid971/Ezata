package app.com.ezata.ui.fragments

import app.com.ezata.model.NewOrder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.com.ezata.R
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.NewOrderAdapter
import app.com.ezata.databinding.FragmentNewOrderBinding

class NewOrderFragment : Fragment() {
    var binding: FragmentNewOrderBinding? = null
    lateinit var orders: Array<NewOrder>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_order, container, false)
        val view = binding?.root
        initRecylerview()
        return view
    }

    private fun initRecylerview() {
        orders = arrayOf(
            NewOrder("J83NW", "Alex T.", "32 Mins", "6 items"),
            NewOrder("K34LE", "James R.", "32 Mins", "6 items"),
            NewOrder("K40RP", "Pat W.", "32 Mins", "6 items"),
            NewOrder("J83NW", "Alex T.", "32 Mins", "6 items")
        )
        binding!!.rvOrders.setHasFixedSize(true)
        binding!!.rvOrders.layoutManager = LinearLayoutManager(activity)
        val adapter = NewOrderAdapter(orders, requireActivity())
        binding!!.rvOrders.adapter = adapter
    }
}