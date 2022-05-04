package app.com.ezata.ui.fragments

import app.com.ezata.model.InProgressOrder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.com.ezata.R
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.ezata.adapter.InProgressOrderAdapter
import app.com.ezata.databinding.FragmentInProgressBinding

class InProgressOrderFragment : Fragment() {
    var binding: FragmentInProgressBinding? = null
    lateinit var list: Array<InProgressOrder>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress, container, false)
        val view = binding?.root
        initRecyclerview()
        return view
    }

    private fun initRecyclerview() {
        list = arrayOf(
            InProgressOrder("JFOQ12", "Alex Ferguson", "11 Mins", "6 Items"),
            InProgressOrder("JFOQ12", "Alex Ferguson", "11 Mins", "6 Items"),
            InProgressOrder("JFOQ12", "Alex Ferguson", "11 Mins", "6 Items"),
            InProgressOrder("JFOQ12", "Alex Ferguson", "11 Mins", "6 Items")
        )
        binding!!.rvInProgress!!.setHasFixedSize(true)
        binding!!.rvInProgress!!.layoutManager = LinearLayoutManager(activity)
        val adapter = InProgressOrderAdapter(list)
        binding!!.rvInProgress!!.adapter = adapter
    }
}