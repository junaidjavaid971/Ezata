package app.com.ezata.model.parse

import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseUser
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields


@ParseClassName("Store")
class StoreParse : ParseObject() {
    val name: String? by ParseDelegateFields()
    val user: ParseUser? by ParseDelegateFields()
    val address: String? by ParseDelegateFields()
    val storeDescription: String? by ParseDelegateFields()
    val storeImageURL: String? by ParseDelegateFields()
    val reviewCount: String? by ParseDelegateFields()
    val storeRating: String? by ParseDelegateFields("store_rating")
    val website: String? by ParseDelegateFields()
}