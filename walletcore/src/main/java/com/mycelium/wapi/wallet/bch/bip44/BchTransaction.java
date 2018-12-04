package com.mycelium.wapi.wallet.bch.bip44;

import com.google.common.base.Optional;
import com.mrd.bitlib.util.Sha256Hash;
import com.mycelium.wapi.wallet.ConfirmationRiskProfileLocal;
import com.mycelium.wapi.wallet.btc.BtcTransaction;
import com.mycelium.wapi.wallet.coins.CryptoCurrency;
import com.mycelium.wapi.wallet.coins.Value;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class BchTransaction extends BtcTransaction {

    public BchTransaction(CryptoCurrency type, Sha256Hash txid,
                          long valueSent, long valueReceived, int timestamp, int confirmations,
                          boolean isQueuedOutgoing, ArrayList<GenericInput> inputs,
                          ArrayList<GenericOutput> outputs, ConfirmationRiskProfileLocal risk,
                          int rawSize, @Nullable Value fee) {
        this.type = type;
        this.hash = txid;
        this.valueSent = Value.valueOf(type, valueSent);
        this.valueReceived = Value.valueOf(type, valueReceived);
        this.value = this.valueReceived.subtract(this.valueSent);
        this.timestamp = timestamp;
        this.confirmations = confirmations;
        this.isQueuedOutgoing = isQueuedOutgoing;
        this.inputs = inputs;
        this.outputs = outputs;
        this.confirmationRiskProfile = Optional.fromNullable(risk);
        this.rawSize = rawSize;
        this.fee = fee;
    }

}