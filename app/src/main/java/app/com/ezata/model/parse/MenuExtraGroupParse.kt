package app.com.ezata.model.parse

import com.parse.ParseClassName
import com.parse.ParseObject
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields


@ParseClassName("MenuExtraGroup")
class MenuExtraGroupParse : ParseObject() {
    val title: String? by ParseDelegateFields()
    val groupText: String? by ParseDelegateFields()
    val menu: MenuParse? by ParseDelegateFields("parentMenu")
}