package app.com.ezata.model.parse

import com.parse.ParseClassName
import com.parse.ParseObject
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields


@ParseClassName("OrderMenuExtraItem")
class OrdersMenuExtraItemParse : ParseObject() {
    val ordersMenu: OrdersMenuParse? by ParseDelegateFields("ParentOrderMenu")
    val price: Number? by ParseDelegateFields()
    val menuExtraGroup: MenuExtraGroupParse? by ParseDelegateFields("ParentMenuExtraGroup")
    val menuExtraItem: MenuExtraItemParse? by ParseDelegateFields("ParentMenuExtra")
}