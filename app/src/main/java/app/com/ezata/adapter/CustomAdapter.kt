package app.com.ezata.adapter

import android.content.Context
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import app.com.ezata.R

class CustomAdapter(var context: Context, var state: IntArray) : BaseAdapter() {
    var inflater: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return state.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        val v: View = inflater.inflate(R.layout.spinner_item, null)
        val icon = v.findViewById<View>(R.id.ivActive) as ImageView
        icon.setImageResource(state[i])
        return v
    }

}