package app.com.ezata.model.parse

import com.parse.Parse
import com.parse.ParseClassName
import com.parse.ParseObject
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields


@ParseClassName("MenuExtraItem")
class MenuExtraItemParse : ParseObject() {
    val name: String? by ParseDelegateFields()
    val price: Number? by ParseDelegateFields()
    val menuExtraGroup: MenuExtraGroupParse? by ParseDelegateFields("parentGroup")
}