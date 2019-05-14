package com.mycelium.wapi.wallet.coinapult

import com.mycelium.wapi.wallet.SendRequest
import com.mycelium.wapi.wallet.btc.BtcAddress
import com.mycelium.wapi.wallet.coins.Value
import com.mycelium.wapi.wallet.colu.ColuTransaction


class CoinapultSendRequest(currency: Currency, val destination: BtcAddress, val amount: Value, var fee: Value)
    : SendRequest<CoinapultTransaction>(currency, fee) {
    override fun getEstimatedTransactionSize(): Int {
        return 0
    }
}