package app.com.ezata.ui.fragments

import android.content.Context
import app.com.ezata.model.InProgressOrder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.com.ezata.R
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.OrdersRecyclerViewAdapter
import app.com.ezata.databinding.FragmentInProgressBinding
import app.com.ezata.databinding.FragmentNewOrderBinding
import app.com.ezata.model.parse.OrdersParse

class InProgressOrderFragment : Fragment() {

    private var _binding: FragmentInProgressBinding? = null
    val binding get() = _binding!!

    var orders: List<OrdersParse?> = emptyList()

    private var ordersRecyclerViewAdapter: OrdersRecyclerViewAdapter? = null

    var itemClicked: ((orderItem: OrdersParse?) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    fun setOrdersValue(mOrders: List<OrdersParse?> , context: Context) {
        orders = mOrders

        if (ordersRecyclerViewAdapter == null) {
            ordersRecyclerViewAdapter = OrdersRecyclerViewAdapter(context)
        }
        ordersRecyclerViewAdapter?.orderList = orders
    }

    private fun init() {
        ordersRecyclerViewAdapter = OrdersRecyclerViewAdapter(requireActivity())
        ordersRecyclerViewAdapter?.itemClicked = { orderItem -> itemClicked?.invoke(orderItem) }
        binding.rvInProgress.setHasFixedSize(true)
        binding.rvInProgress.layoutManager = LinearLayoutManager(activity)
        binding.rvInProgress.adapter = ordersRecyclerViewAdapter
        ordersRecyclerViewAdapter?.orderList = orders
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}