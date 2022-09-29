package app.com.ezata.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import app.com.ezata.databinding.ActivityLoginBinding
import app.com.ezata.model.parse.StoreParse
import app.com.ezata.utils.AppUtils
import app.com.ezata.utils.SharedPrefKey
import app.com.ezata.utils.SharedPrefUtils
import app.com.ezata.utils.findListInBackgroundCoroutine
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private val DEBUG = true

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ParseUser.getCurrentUser() != null) {
            startMainActivity()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            loginButton.setOnClickListener {
                performLogin()
            }
            passwordTextInputLayout.editText?.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performLogin()
                    return@setOnEditorActionListener true
                }
                false
            }
        }

    }

    private fun performLogin() {
        binding.apply {
            AppUtils.hideKeyboard(this@LoginActivity)
            loadingLayout.root.visibility = View.VISIBLE
            emailTextInputLayout.error = null
            passwordTextInputLayout.error = null

            val email = emailTextInputLayout.editText?.text.toString()
            val password = passwordTextInputLayout.editText?.text.toString()

            ParseUser.logInInBackground(email, password) { user, e ->
                loadingLayout.root.visibility = View.GONE
                if (e == null) {
                    if (user.get("userStore") as? Boolean? == true) {
                        lifecycleScope.launch(Dispatchers.IO) {

                            val store = ParseQuery(StoreParse::class.java)
                                .whereEqualTo("user", ParseObject.createWithoutData("_User", user.objectId))
                                .findListInBackgroundCoroutine().firstOrNull()

                            if (store !== null) {
                                SharedPrefUtils.save(this@LoginActivity, SharedPrefKey.STORE_OBJECT_ID, store.objectId)
                                startMainActivity()
                            } else {
                                ParseUser.logOutInBackground()
                                emailTextInputLayout.error = "Store not found!"
                                passwordTextInputLayout.error = "Store not found!"
                            }

                        }
                    } else {
                        ParseUser.logOutInBackground()
                        emailTextInputLayout.error = "Invalid Credentials"
                        passwordTextInputLayout.error = "Invalid Credentials"
                    }

                } else {
                    emailTextInputLayout.error = e.message
                    passwordTextInputLayout.error = e.message
                }
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}