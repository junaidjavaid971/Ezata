package app.com.ezata.model.parse

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields
import java.sql.Date
import java.sql.Driver


@ParseClassName("Orders")
class OrdersParse : ParseObject() {
    val store: ParseObject? by ParseDelegateFields()
    val user: ParseUser? by ParseDelegateFields("userOrdered")
    var orderStatus: String? by ParseDelegateFields()
    val orderNumber: String? by ParseDelegateFields()
    val quantity: Number? by ParseDelegateFields()
    val amount: Double? by ParseDelegateFields()
    val deliveryTime: Number? by ParseDelegateFields()
    val deliveryPerson: DriverParse? by ParseDelegateFields("deliveryPerson")
}