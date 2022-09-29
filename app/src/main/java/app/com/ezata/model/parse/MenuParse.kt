package app.com.ezata.model.parse

import com.parse.ParseClassName
import com.parse.ParseObject
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields


@ParseClassName("Menu")
class MenuParse : ParseObject() {
    val dishDescription: String? by ParseDelegateFields()
    val dishTitle: String? by ParseDelegateFields()
    val price: Number? by ParseDelegateFields()
}