package com.mycelium.wapi.wallet.bip44;

import com.google.common.base.Optional;
import com.mrd.bitlib.model.Address;
import com.mrd.bitlib.model.NetworkParameters;
import com.mycelium.wapi.api.Wapi;
import com.mycelium.wapi.model.TransactionSummary;
import com.mycelium.wapi.wallet.Bip44AccountBacking;
import com.mycelium.wapi.wallet.SpvBalanceFetcher;
import com.mycelium.wapi.wallet.currency.CurrencyBasedBalance;
import com.mycelium.wapi.wallet.currency.CurrencyValue;
import com.mycelium.wapi.wallet.currency.ExactBitcoinCashValue;
import com.mycelium.wapi.wallet.currency.ExactCurrencyValue;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Bip44BCHAccount extends Bip44Account {
    private SpvBalanceFetcher spvBalanceFetcher;
    private int blockChainHeight;
    private boolean visible;

    @Override
    public String getAccountDefaultCurrency() {
        return CurrencyValue.BCH;
    }

    public Bip44BCHAccount(Bip44AccountContext context, Bip44AccountKeyManager keyManager, NetworkParameters network, Bip44AccountBacking backing, Wapi wapi, SpvBalanceFetcher spvBalanceFetcher) {
        super(context, keyManager, network, backing, wapi);
        this.spvBalanceFetcher = spvBalanceFetcher;
        this.type = Type.BCHBIP44;
    }

    @Override
    public CurrencyBasedBalance getCurrencyBasedBalance() {
        return spvBalanceFetcher.retrieveByHdAccountIndex(getId().toString(), getAccountIndex());
    }

    @Override
    public ExactCurrencyValue calculateMaxSpendableAmount(long minerFeePerKbToUse) {
        //TODO Refactor the code and make the proper usage of minerFeePerKbToUse parameter
        String txFee = "NORMAL";
        float txFeeFactor = 1.0f;
        return ExactBitcoinCashValue.from(spvBalanceFetcher.calculateMaxSpendableAmount(getAccountIndex(), txFee, txFeeFactor));
    }

    @Override
    public UUID getId() {
        return UUID.nameUUIDFromBytes(("BCH" + super.getId().toString()).getBytes());
    }

    // need override because parent write it to context(bch and btc account have one context)
    @Override
    public void setBlockChainHeight(int blockHeight) {
        blockChainHeight = blockHeight;
    }

    @Override
    public int getBlockChainHeight() {
        return blockChainHeight;
    }

    @Override
    public List<TransactionSummary> getTransactionHistory(int offset, int limit) {
        return spvBalanceFetcher.retrieveTransactionSummaryByHdAccountIndex(getId().toString(), getAccountIndex());
    }

    @Override
    public List<TransactionSummary> getTransactionsSince(Long receivingSince) {
        return spvBalanceFetcher.retrieveTransactionSummaryByHdAccountIndex(getId().toString(), getAccountIndex(), receivingSince);
    }

    @Override
    public boolean isVisible() {
        if ((spvBalanceFetcher.getSyncProgressPercents() == 100 || !spvBalanceFetcher.isFirstSync())
                && !visible) {
            visible = !spvBalanceFetcher.retrieveTransactionSummaryByHdAccountIndex(getId().toString(), getAccountIndex()).isEmpty();
        }
        return visible;
    }

    @Override
    public int getPrivateKeyCount() {
        return spvBalanceFetcher.getPrivateKeysCount(getAccountIndex());
    }

    @Override
    public Optional<Address> getReceivingAddress() {
        return Optional.fromNullable(spvBalanceFetcher.getCurrentReceiveAddress(getAccountIndex()));
    }
}
