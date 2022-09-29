package app.com.ezata.utils.livedata

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import app.com.ezata.model.parse.OrdersParse
import app.com.ezata.utils.OrderStatus
import app.com.ezata.utils.findInBackgroundCoroutine
import com.parse.ParseQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


sealed class CountDownData {
    object Finished : CountDownData()
    data class Ticking(val tick: Long) : CountDownData()
    object Cancelled : CountDownData()
}

class CountDownLiveData : LiveData<CountDownData>() {
    private val TAG = "CountDownLiveData"
    private val DEBUG = true

    fun startCountDown() {
        if (!is_countDownRunning) {
            countDownTimer.cancel()
            countDownTimer.start()
        }
    }

    fun stopCountDown() {
        postValue(CountDownData.Cancelled)
        countDownTimer.cancel()
        is_countDownRunning = false
    }

    companion object {
        private lateinit var instance: CountDownLiveData

        private var is_countDownRunning = false

        private var ordersParse: OrdersParse? = null

        fun setCurrentOrderParse(currentWorkingOrdersParse: OrdersParse?) {
            ordersParse = currentWorkingOrdersParse
        }

        private var countDownTimer = object : CountDownTimer(90 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("Debug ", " onTick: $millisUntilFinished ")
                is_countDownRunning = true
                instance.postValue(CountDownData.Ticking(millisUntilFinished))
            }

            override fun onFinish() {
                Log.d("Debug ", " onFinish:  ")
                is_countDownRunning = false
                instance.postValue(CountDownData.Finished)
                CoroutineScope(Dispatchers.IO).launch {
                    ordersParse?.let { _ordersParse ->
                        val nOrderParse =
                            ParseQuery(OrdersParse::class.java).findInBackgroundCoroutine(
                                _ordersParse.objectId
                            )
                        if (nOrderParse.orderStatus in setOf(OrderStatus.IN_PROGRESS)) {
                            nOrderParse.orderStatus = OrderStatus.PICKUP_READY

                            nOrderParse.save()
                        }
                        ordersParse = nOrderParse
                    }
                }
            }
        }

        fun getInstance(): CountDownLiveData {
            instance = if (Companion::instance.isInitialized) instance else CountDownLiveData()
            return instance
        }
    }


}