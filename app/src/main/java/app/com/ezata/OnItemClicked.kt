package app.com.ezata

import app.com.ezata.model.parse.OrdersParse

interface OnItemClicked {
    fun onItemSelect(ordersParse: OrdersParse)
}