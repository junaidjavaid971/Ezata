package app.com.ezata.model.parse

import com.parse.ParseClassName
import com.parse.ParseObject
import mh.dev.ezatapartner.Utils.Delegation.ParseDelegateFields
import java.sql.Date

@ParseClassName("Driver")
class DriverParse: ParseObject() {
    val objectID: String? by ParseDelegateFields()
    val name: String? by ParseDelegateFields()
    val firstName: String? by ParseDelegateFields()
    val lastName: String? by ParseDelegateFields()
    val phone: String? by ParseDelegateFields()
    val updatedAt: Date? by ParseDelegateFields()
    val status: Number? by ParseDelegateFields()
}