package app.com.ezata.ui.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import app.com.ezata.R
import app.com.ezata.databinding.ActivityRegistrationBinding
import app.com.ezata.model.parse.StoreParse
import app.com.ezata.utils.OrderStatus
import app.com.ezata.utils.SharedPrefKey
import app.com.ezata.utils.SharedPrefUtils
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegistrationActivity : AppCompatActivity() {
    var binding: ActivityRegistrationBinding? = null
    var vehicleType = "Motorcycle"
    var itemSize = "Medium"
    var deliveryTime = "30 min"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        manageClicks()

        binding?.loadingLayout?.root?.visibility = View.GONE
        binding?.overlay?.visibility = View.GONE
    }

    private fun manageClicks() {
        binding?.ivMenu?.setOnClickListener { view: View? -> openNavDrawer() }

        binding?.navItem?.navDrawer?.setOnClickListener {}

        binding?.tvSoonPossible?.setOnClickListener {
            deliveryTime = "As soon as possible"
            it.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.orange_card)
            binding?.tv30Mins?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.gray)
        }

        binding?.tv30Mins?.setOnClickListener {
            deliveryTime = "30 Mins"
            it.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.orange_card)
            binding?.tvSoonPossible?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.gray)
        }

        binding?.tvSmall?.setOnClickListener {
            itemSize = "Small"
            it.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.orange_card)
            binding?.tvMedium?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.gray)
            binding?.tvLarge?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.gray)
        }

        binding?.tvMedium?.setOnClickListener {
            itemSize = "Medium"
            binding?.tvSmall?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.gray)
            it?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.orange_card)
            binding?.tvLarge?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.gray)
        }

        binding?.tvLarge?.setOnClickListener {
            itemSize = "Large"
            binding?.tvSmall?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.gray)
            binding?.tvMedium?.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.gray)
            it.backgroundTintList =
                ContextCompat.getColorStateList(this@RegistrationActivity, R.color.orange_card)
        }

        binding?.cardBiCycle?.setOnClickListener {
            vehicleType = "Bicycle"
            binding?.layoutBiCycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.orange_card
            )
            binding?.ivBicycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.color3
            )

            binding?.layoutMotorCycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.gray
            )
            binding?.ivMotorbike?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.lightBlack
            )

            binding?.layoutCar?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.gray
            )
            binding?.ivCar?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.lightBlack
            )
        }

        binding?.cardMotorCycle?.setOnClickListener {
            vehicleType = "MotorCycle"
            binding?.layoutBiCycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.gray
            )
            binding?.ivBicycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.lightBlack
            )

            binding?.layoutMotorCycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.orange_card
            )
            binding?.ivMotorbike?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.color3
            )

            binding?.layoutCar?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.gray
            )
            binding?.ivCar?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.lightBlack
            )
        }
        binding?.cardCar?.setOnClickListener {
            vehicleType = "Car"
            binding?.layoutBiCycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.gray
            )
            binding?.ivBicycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.lightBlack
            )

            binding?.layoutMotorCycle?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.gray
            )
            binding?.ivMotorbike?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.lightBlack
            )

            binding?.layoutCar?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.orange_card
            )
            binding?.ivCar?.backgroundTintList = ContextCompat.getColorStateList(
                this@RegistrationActivity,
                R.color.color3
            )
        }

        binding?.tvSubmit?.setOnClickListener {
            if (validateFields()) {
                val orderParse = ParseObject.create("Orders")
                val store = SharedPrefUtils.getString(
                    this@RegistrationActivity,
                    SharedPrefKey.STORE_OBJECT_ID
                )
                binding?.edName?.text?.toString()?.let { it1 -> orderParse.put("goOrderName", it1) }
                binding?.edPhoneNumber?.text?.toString()
                    ?.let { it1 -> orderParse.put("phone", it1) }
                binding?.edAddress?.text?.toString()?.let { it1 -> orderParse.put("toText", it1) }
                orderParse.put("goOrder", true)
                orderParse.put("goOrderVtype", vehicleType)
                orderParse.put("goOrderSize", itemSize)
                orderParse.put("type", "goOrder")
                orderParse.put("orderStatus", OrderStatus.PICKUP_READY)

                binding?.loadingLayout?.root?.visibility = View.VISIBLE
                binding?.overlay?.visibility = View.VISIBLE

                lifecycleScope.launch(Dispatchers.IO) {
                    ParseQuery<ParseObject>("Store")
                        .include("coordinate").getInBackground(store) { obj, e ->
                            run {
                                if (e == null) {
                                    val storeParse = obj as StoreParse
                                    orderParse.put("store", storeParse)
                                    storeParse.get("coordinate")
                                        ?.let { it2 -> orderParse.put("fromGeo", it2) }
                                    orderParse.save()
                                } else {
                                    orderParse.save()
                                }
                            }
                        }

                    withContext(Dispatchers.Main) {
                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                            Toast.makeText(
                                this@RegistrationActivity,
                                getString(R.string.str_orderCreated),
                                Toast.LENGTH_LONG
                            ).show()

                            binding?.loadingLayout?.root?.visibility = View.GONE
                            binding?.overlay?.visibility = View.GONE

                            onBackPressed()
                        }, 3000)
                    }
                }
            } else {
                Toast.makeText(
                    this@RegistrationActivity,
                    getString(R.string.str_validation),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        when {
            binding?.edPickupLocation?.text?.isEmpty() == true -> {
                return false
            }
            binding?.edName?.text?.isEmpty() == true -> {
                return false
            }
            binding?.edPhoneNumber?.text?.isEmpty() == true -> {
                return false
            }
            binding?.edAddress?.text?.isEmpty() == true -> {
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun openNavDrawer() {
        binding!!.drawer.openDrawer(Gravity.LEFT)
    }

    fun getAlphaNumericString(n: Int): String {
        val AlphaNumericString = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz")

        val sb = StringBuilder()
        for (i in 0 until n) {
            val index = (AlphaNumericString.length
                    * Math.random()).toInt()
            sb.append(AlphaNumericString[index])
        }
        return sb.toString()
    }
}