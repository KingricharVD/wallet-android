package com.mycelium.wapi.wallet.fio

import com.mrd.bitlib.bitcoinj.Base58
import com.mrd.bitlib.crypto.BipDerivationType
import com.mrd.bitlib.crypto.HdKeyNode
import com.mrd.bitlib.crypto.PublicKey
import com.mrd.bitlib.crypto.digest.RIPEMD160Digest
import com.mrd.bitlib.model.hdpath.HdKeyPath
import com.mycelium.wapi.wallet.AesKeyCipher
import com.mycelium.wapi.wallet.masterseed.MasterSeedManager

class FioKeyManager(private val masterSeedManager: MasterSeedManager) {

    private fun getFioNode(): HdKeyNode {
        val masterSeed = masterSeedManager.getMasterSeed(AesKeyCipher.defaultKeyCipher())
        // following [SLIP44](https://github.com/satoshilabs/slips/blob/master/slip-0044.md)
        // m/44'/235'
        return HdKeyNode.fromSeed(masterSeed.bip32Seed, BipDerivationType.BIP44)
                .createChildNode(HdKeyPath.valueOf("m/44'/235'"))
    }

    fun getFioPrivateKey(accountIndex: Int) = getFioNode().createHardenedChildNode(accountIndex).createChildNode(0).createChildNode(0).privateKey

    fun getFioPublicKey(accountIndex: Int) = getFioPrivateKey(accountIndex).publicKey

    fun formatPubKey(publicKey: PublicKey): String {
        val out = ByteArray(20)
        val ripeMD160 = RIPEMD160Digest()
        ripeMD160.update(publicKey.publicKeyBytes, 0, publicKey.publicKeyBytes.size)
        ripeMD160.doFinal(out, 0)
        var slicedHash = out.slice(IntRange(0,3))
        return "FIO" + Base58.encode(publicKey.publicKeyBytes + slicedHash)
    }
}