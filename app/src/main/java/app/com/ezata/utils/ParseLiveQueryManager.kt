package app.com.ezata.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.livequery.LiveQueryException
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.ParseLiveQueryClientCallbacks

class ParseLiveQueryManager<T : ParseObject?> constructor(
    private val parseQuery: ParseQuery<T>,
    private val lifecycle_: Lifecycle,
    val context: Context
) : ParseLiveQueryClientCallbacks, LifecycleOwner, LifecycleObserver {

    private val TAG = "ParseLiveQueryManager"
    private val DEBUG = true

//    private val lifecycleRegistry = LifecycleRegistry(this)

//    private var lifeCycleOwnerIsOnBg = false

//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    fun onStop() {
//        lifeCycleOwnerIsOnBg = true
//        parseLiveQueryClient?.disconnect()
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    fun onStart() {
//        if (!isLiveryQueryClientConnected) {
//            parseLiveQueryClient?.reconnect()
//        }
//        lifeCycleOwnerIsOnBg = false
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        parseLiveQueryClient?.unsubscribe(parseQuery)
        parseLiveQueryClient?.disconnect()
    }

    private var isLiveryQueryClientConnected = true

    private var parseLiveQueryClient: ParseLiveQueryClient? = null

    init {
        lifecycle_.addObserver(this)
        NetworkConnectivity(context).observe(this) { isAvailable: Boolean ->
            if (isAvailable && !isLiveryQueryClientConnected) {
                parseLiveQueryClient?.reconnect()
            }
        }
    }

    override fun onLiveQueryClientConnected(client: ParseLiveQueryClient?) {
        if (DEBUG) Log.d("Debug " + TAG, " onLiveQueryClientConnected: ${parseQuery.className} ")
        parseLiveQueryClient = client
        isLiveryQueryClientConnected = true
    }

    override fun onLiveQueryClientDisconnected(client: ParseLiveQueryClient?, userInitiated: Boolean) {
        if (DEBUG) Log.d("Debug $TAG", " onLiveQueryClientDisconnected: ${parseQuery.className} ")
        isLiveryQueryClientConnected = false
    }

    override fun onLiveQueryError(client: ParseLiveQueryClient?, reason: LiveQueryException?) {

    }

    override fun onSocketError(client: ParseLiveQueryClient?, reason: Throwable?) {
    }

    override fun getLifecycle(): Lifecycle = lifecycle_

}