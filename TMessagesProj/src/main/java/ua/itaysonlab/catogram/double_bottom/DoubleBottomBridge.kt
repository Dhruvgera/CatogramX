package ua.itaysonlab.catogram.double_bottom

import com.google.android.exoplayer2.util.Log
import com.dhruv.catogramx.EarlyConfig
import org.telegram.messenger.SharedConfig
import org.telegram.messenger.UserConfig
import ua.itaysonlab.catogram.CatogramConfig

/**
 * Option is AVAILABLE:
 * - in first 2 minutes from the logging of the any account (every login creates the process again)
 * - only if account size >= 2
 */
object DoubleBottomBridge {
    fun startTimer() {
        // In 2 minutes time you can now visit CG settings and see a "Double Bottom" option here (25 in debug)
        DoubleBottomStorageBridge.dbTimerExpireDate = System.currentTimeMillis() + DoubleBottomStorageBridge.DB_TIMER_END
    }

    fun isDbConfigAvailable(): Boolean {
        return (((System.currentTimeMillis() <= DoubleBottomStorageBridge.dbTimerExpireDate || DoubleBottomStorageBridge.DB_TIMER_END == -1) && !isDbActivatedForAccount(0) /* no settings pin */) || EarlyConfig.alwaysShowDoubleBottom) && UserConfig.getActivatedAccountsCount() > 1 && SharedConfig.passcodeHash.isNotEmpty()
    }

    fun isDbSetupCompleted(): Boolean {
        return DoubleBottomStorageBridge.storageInstance.map.isNotEmpty()
    }

    fun isDbActivatedForAccount(id: Long): Boolean {
        DoubleBottomStorageBridge.storageInstance.map.values.forEach {
            if (it.id == id) return true
        }
        return false
    }

    // return -1 if no account is found
    @JvmStatic
    fun checkPasscodeForAnyOfAccounts(code: String): Long {
        if (!isDbSetupCompleted()) return -1

        val masterHash = DoubleBottomPasscodeActivity.getHash(code)
        DoubleBottomStorageBridge.storageInstance.map.values.forEach {
            if (it.hash == masterHash) return it.id
        }

        return -1
    }

    @JvmStatic
    fun findLocalAccIdByTgId(id: Long): Int {
        Log.e("u", id.toString())
        for (i in 0 until UserConfig.MAX_ACCOUNT_COUNT) {
            val uc = UserConfig.getInstance(i)
            if (uc.isClientActivated) Log.e("u", uc.currentUser.id.toString())
            if (uc.isClientActivated && uc.currentUser.id == id) return i
        }

        return 0
    }
}