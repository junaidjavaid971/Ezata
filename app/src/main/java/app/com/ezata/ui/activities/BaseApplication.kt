package app.com.ezata.ui.activities

import android.app.Application
import android.os.Build
import app.com.ezata.BuildConfig
import app.com.ezata.R
import app.com.ezata.model.parse.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.parse.Parse
import com.parse.ParseObject
import java.sql.Driver

class BaseApplication : Application() {
    private val TAG = "BaseApplication"
    private val DEBUG = true

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        } else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }

        ParseObject.registerSubclass(StoreParse::class.java)
        ParseObject.registerSubclass(OrdersParse::class.java)
        ParseObject.registerSubclass(OrdersMenuParse::class.java)
        ParseObject.registerSubclass(MenuParse::class.java)
        ParseObject.registerSubclass(CategoriesParse::class.java)
        ParseObject.registerSubclass(MenuExtraGroupParse::class.java)
        ParseObject.registerSubclass(OrdersMenuExtraItemParse::class.java)
        ParseObject.registerSubclass(MenuExtraItemParse::class.java)
        ParseObject.registerSubclass(DriverParse::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
    }

}