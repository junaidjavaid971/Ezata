package mh.dev.ezatapartner.Utils.Delegation

import com.parse.ParseGeoPoint
import com.parse.ParseObject
import kotlin.reflect.KProperty

class GeoPointDelegation {

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(parseObject: ParseObject, property: KProperty<*>): ParseGeoPoint {
        val fromTo = parseObject.get("fromTo") as ArrayList<ParseGeoPoint>
        return when (property.name) {
            "fromGeoPoint" -> {
                fromTo.first()
            }
            "toGeoPoint" -> {
                fromTo.last()
            }
            else -> {
                ParseGeoPoint()
            }
        }
    }

}