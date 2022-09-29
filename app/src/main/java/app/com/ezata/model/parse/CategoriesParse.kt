package app.com.ezata.model.parse

import com.parse.ParseClassName
import com.parse.ParseObject
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields


@ParseClassName("Categories")
class CategoriesParse : ParseObject() {
    val itemTitle: String? by ParseDelegateFields()
    val store: StoreParse? by ParseDelegateFields("ParentStore")
}