package app.com.ezata.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Lifecycle
import com.parse.FunctionCallback
import com.parse.ParseCloud
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.livequery.LiveQueryException
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import java.net.URI
import kotlin.coroutines.suspendCoroutine

object AppUtils {
    fun hideKeyboard(activity: Activity?) {
        activity ?: return
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}


suspend fun <T : ParseObject> ParseQuery<T>.findListInBackgroundCoroutine(): List<T> {
    return suspendCoroutine { cont ->
        findInBackground { objects, e ->
            if (e != null) {
                cont.resumeWith(Result.failure(e))
            } else {
                cont.resumeWith(Result.success(objects))
            }
        }
    }

}


suspend fun <T : ParseObject> ParseQuery<T>.findInBackgroundCoroutine(objectId: String): T {
    return suspendCoroutine { cont ->
        getInBackground(objectId) { `object`, e ->
            if (e != null) {
                cont.resumeWith(Result.failure(e))
            } else {
                cont.resumeWith(Result.success(`object`))
            }
        }
    }

}

suspend fun <T : ParseObject> ParseObject.loadObjectCoroutine(): T {
    return suspendCoroutine { cont ->
        fetchIfNeededInBackground<T> { obj, e ->
            if (e != null) {
                cont.resumeWith(Result.failure(e))
            } else {
                cont.resumeWith(Result.success(obj))
            }
        }
    }
}

suspend fun <T> callParseFunctionCoroutine(funcName: String, fields: HashMap<String, *>): T? {
    return suspendCoroutine { cont ->
        ParseCloud.callFunctionInBackground(
            funcName,
            fields,
            FunctionCallback<T> { `object`, e ->
                if (e == null) {
                    cont.resumeWith(Result.success(`object`))
                } else {
                    cont.resumeWith(Result.failure(e))
                }
            })
    }
}

suspend fun ParseQuery<ParseUser>.getInBackgroundCoroutine(objectId: String): ParseUser {
    return suspendCoroutine { cont ->
        getInBackground(objectId) { `object`, e ->
            if (e != null) {
                cont.resumeWith(Result.failure(e))
            } else {
                cont.resumeWith(Result.success(`object`))
            }
        }
    }
}

fun <T : ParseObject> ParseQuery<T>.parseLiveQuery(
    context: Context? = null,
    lifecycle: Lifecycle? = null,
    created: ((errors: LiveQueryException?, createdValue: T?) -> Unit)? = null,
    updated: ((errors: LiveQueryException?, updatedValue: T?) -> Unit)? = null,
    deleted: ((errors: LiveQueryException?, deletedValue: T?) -> Unit)? = null,
    createdOrUpdatedOrDeleted: ((errors: LiveQueryException?, value: T?) -> Unit)? = null
): ParseLiveQueryClient {

    val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(URI("wss://ezata.back4app.io/"))
    val subscriptionHandling = parseLiveQueryClient.subscribe(this)

    if (created != null || createdOrUpdatedOrDeleted !== null) {
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, object : SubscriptionHandling.HandleErrorCallback<T>,
            SubscriptionHandling.HandleEventCallback<T> {
            override fun onError(query: ParseQuery<T>?, exception: LiveQueryException?) {
                created?.invoke(exception, null)
            }

            override fun onEvent(query: ParseQuery<T>?, `object`: T) {
                createdOrUpdatedOrDeleted?.invoke(null, `object`)
                created?.invoke(null, `object`)
            }
        })
    }

    if (updated != null || createdOrUpdatedOrDeleted !== null) {
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, object : SubscriptionHandling.HandleErrorCallback<T>,
            SubscriptionHandling.HandleEventCallback<T> {
            override fun onError(query: ParseQuery<T>?, exception: LiveQueryException?) {
                updated?.invoke(exception, null)
            }

            override fun onEvent(query: ParseQuery<T>?, `object`: T) {
                updated?.invoke(null, `object`)
                createdOrUpdatedOrDeleted?.invoke(null, `object`)
            }
        })
    }

    if (deleted != null || createdOrUpdatedOrDeleted !== null) {
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.DELETE, object : SubscriptionHandling.HandleErrorCallback<T>,
            SubscriptionHandling.HandleEventCallback<T> {
            override fun onError(query: ParseQuery<T>?, exception: LiveQueryException?) {
                deleted?.invoke(exception, null)
            }

            override fun onEvent(query: ParseQuery<T>?, `object`: T) {
                deleted?.invoke(null, `object`)
                createdOrUpdatedOrDeleted?.invoke(null, `object`)
            }
        })
    }

    if (context !== null && lifecycle !== null) {
        parseLiveQueryClient.registerListener(ParseLiveQueryManager(this, lifecycle, context))
    }

    return parseLiveQueryClient
}