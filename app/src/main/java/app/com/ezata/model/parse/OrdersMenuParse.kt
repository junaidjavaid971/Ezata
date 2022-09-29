package app.com.ezata.model.parse

import com.parse.ParseClassName
import com.parse.ParseObject
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields


@ParseClassName("OrdersMenu")
class OrdersMenuParse : ParseObject() {
    val order: OrdersParse? by ParseDelegateFields("orderId")
    val menu: MenuParse? by ParseDelegateFields("menuItemId")
    val category: CategoriesParse? by ParseDelegateFields("ParentCategory")
    val price:Number? by ParseDelegateFields()
    val quantity:Number? by ParseDelegateFields()
    val cartItemNo:Int? by ParseDelegateFields()
}