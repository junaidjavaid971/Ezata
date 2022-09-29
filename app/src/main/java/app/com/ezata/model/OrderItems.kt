package app.com.ezata.model

import app.com.ezata.model.parse.OrdersMenuExtraItemParse
import app.com.ezata.model.parse.OrdersMenuParse

data class OrderItems(
    var ordersMenuParse: OrdersMenuParse?,
    var ordersMenuExtraItemParse: List<OrdersMenuExtraItemParse?>
)