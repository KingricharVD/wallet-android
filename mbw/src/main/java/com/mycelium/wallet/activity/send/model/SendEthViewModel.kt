package com.mycelium.wallet.activity.send.model

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.mycelium.wallet.activity.send.adapter.FeeViewAdapter
import com.mycelium.wapi.wallet.WalletAccount
import com.mycelium.wapi.wallet.btc.bip44.HDAccountExternalSignature
import com.mycelium.wapi.wallet.coins.Value
import java.util.regex.Pattern

open class SendEthViewModel(context: Application) : SendCoinsViewModel(context) {
    override val uriPattern = Pattern.compile("0x[a-zA-Z0-9]+")

    override fun init(account: WalletAccount<*>, intent: Intent) {
        super.init(account, intent)
        model = SendEthModel(context, account, intent)
    }

    override fun sendTransaction(activity: Activity) {
        if (isColdStorage() || model.account is HDAccountExternalSignature) {
            // We do not ask for pin when the key is from cold storage or from a external device (trezor,...)
            model.signTransaction(activity)
        } else {
            mbwManager.runPinProtectedFunction(activity) { model.signTransaction(activity) }
        }
    }

    override fun getFeeFormatter() = object : FeeViewAdapter.FeeItemFormatter {
        override fun getCategoryText(value: Value?): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemText(value: Value?): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getValueText(value: Long): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}