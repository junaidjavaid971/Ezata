package app.com.ezata.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.com.ezata.R
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.OrdersRecyclerViewAdapter
import app.com.ezata.databinding.FragmentNewOrderBinding
import app.com.ezata.model.parse.OrdersParse

class NewOrderFragment : Fragment() {
    private val TAG = "NewOrderFragment"
    private val DEBUG = true

    private var _binding: FragmentNewOrderBinding? = null
    val binding get() = _binding!!

    var orders: List<OrdersParse?> = emptyList()

    lateinit var ordersRecyclerViewAdapter: OrdersRecyclerViewAdapter

    var itemClicked: ((orderItem: OrdersParse?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }


    fun setOrdersValue(mOrders: List<OrdersParse?>) {
        orders = mOrders
        ordersRecyclerViewAdapter.orderList = orders
    }

    private fun init() {
        ordersRecyclerViewAdapter = OrdersRecyclerViewAdapter(requireActivity())
        ordersRecyclerViewAdapter.itemClicked = { orderItem -> itemClicked?.invoke(orderItem) }
        binding.rvOrders.setHasFixedSize(true)
        binding.rvOrders.layoutManager = LinearLayoutManager(activity)
        binding.rvOrders.adapter = ordersRecyclerViewAdapter
        ordersRecyclerViewAdapter.orderList = orders
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}