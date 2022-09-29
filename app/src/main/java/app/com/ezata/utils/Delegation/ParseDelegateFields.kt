package mh.dev.ezatapartner.Utils.Delegation

import com.parse.ParseObject
import kotlin.reflect.KProperty

class ParseDelegateFields(val propertyName: String? = null) {

    private val TAG = "ParseDelegateFields"
    private val DEBUG = true

    @Suppress("UNCHECKED_CAST")
    operator fun <T> getValue(parseObject: ParseObject, property: KProperty<*>): T {
        return parseObject.get(propertyName ?: property.name) as T
    }

    operator fun setValue(parseObject: ParseObject, property: KProperty<*>, value: Any?) {
        if (value != null) {
            parseObject.put(propertyName ?: property.name, value)
        } else {
            parseObject.remove(propertyName ?: property.name)
        }
    }

}