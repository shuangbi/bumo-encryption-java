package org.bumo.encryption.key;

import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.Signature;

import org.bumo.encryption.common.CheckKey;
import org.bumo.encryption.common.Sadk;
import org.bumo.encryption.model.KeyMember;
import org.bumo.encryption.model.KeyType;
import org.bumo.encryption.utils.base.Base58;
import org.bumo.encryption.utils.hex.HexFormat;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.algorithm.sm2.SM2PrivateKey;
import cfca.sadk.algorithm.sm2.SM2PublicKey;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.Base64;
import cfca.sadk.util.HashUtil;
import cfca.sadk.util.KeyUtil;
import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;

public class PrivateKey {
	private PublicKey publicKey = new PublicKey();
	private KeyMember keyMember = new KeyMember();
	
	/**
	 * generate key pair (default: ed25519)
	 * @throws Exception 
	 */
	public PrivateKey() throws Exception {
		this(KeyType.ED25519);
	}
	
	/**
	 * generate key pair
	 * @param  keyType
	 * @throws Exception 
	 */
	public PrivateKey(KeyType type) throws Exception {
		switch (type) {
		case ED25519: {
			KeyPairGenerator keyPairGenerator = new KeyPairGenerator();
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			EdDSAPrivateKey priKey = (EdDSAPrivateKey) keyPair.getPrivate();
			EdDSAPublicKey pubKey = (EdDSAPublicKey) keyPair.getPublic();
			keyMember.setRawSKey(priKey.getSeed());
			publicKey.setRawPublicKey(pubKey.getAbyte());
			break;
		}
		case ECCSM2: {
			final String deviceName = JCrypto.JSOFT_LIB;
	        JCrypto.getInstance().initialize(deviceName, null);
	        Session session = JCrypto.getInstance().openSession(deviceName);
	        
	        KeyPair keypair = KeyUtil.generateKeyPair(new Mechanism(Mechanism.SM2), 256, session);
	        SM2PublicKey pubKey = (SM2PublicKey)keypair.getPublic();
	        SM2PrivateKey priKey = (SM2PrivateKey)keypair.getPrivate();
	        keyMember.setRawSKey(priKey.getD_Bytes());
	        publicKey.setRawPublicKey(Sadk.getSM2PublicKey(pubKey));
			break;
		}
		default:
			throw new Exception("type does not exist");
		}
		setKeyType(type);
		publicKey.setKeyType(type);
	}

	/**
	 * generate key pair
	 * @param skey private key
	 * @throws Exception
	 */
	public PrivateKey(String skey) throws Exception {
		getPrivateKey(skey, keyMember);
		publicKey.setKeyType(keyMember.getKeyType());
		byte[] rawPKey = getPublicKey(keyMember);
		publicKey.setRawPublicKey(rawPKey);
		keyMember.setRawPKey(rawPKey);
	}
	
	/**
	 * set key type
	 * @param keyType key type
	 */
	public void setKeyType(KeyType keyType) {
		keyMember.setKeyType(keyType);
	}
	
	/**
	 * get key type
	 * @return key type
	 */
	public KeyType getKeyType() {
		return keyMember.getKeyType();
	}
	
	/**
	 * set raw private key
	 * @param raw private key
	 */
	public void setRawPrivateKey(byte[] rawSKey) {
		keyMember.setRawSKey(rawSKey);
	}
	
	/**
	 * get raw private key
	 * @return raw private key
	 */
	public byte[] getRawPrivateKey() {
		return keyMember.getRawSKey();
	}
	
	/**
	 * get public key
	 * @return public key
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	/**
	 *
	 * @return encode private key
	 * @throws Exception 
	 */
	public String getEncPrivateKey() throws Exception {
		byte[] rawSKey = keyMember.getRawSKey();
		if (rawSKey == null) {
			throw new Exception("raw private key is null");
		}
		return EncPrivateKey(keyMember.getKeyType(), keyMember.getRawSKey());
	}
	
	
	/**
	 *  
	 * @return encode public key
	 * @throws Exception 
	 */
	public String getEncPublicKey() throws Exception {
		byte[] rawPKey = publicKey.getRawPublicKey();
		if (rawPKey == null) {
			throw new Exception("raw public key is null");
		}
		return encPublicKey(keyMember.getKeyType(), rawPKey).toLowerCase();
	}
	
	/**
	 * @param skey encode private key
	 * @return encode public key
	 * @throws Exception 
	 */
	public static String getEncPublicKey(String skey) throws Exception {
		KeyMember member = new KeyMember();
		getPrivateKey(skey, member);
		byte[] rawPKey = getPublicKey(member);
		return encPublicKey(member.getKeyType(), rawPKey).toLowerCase();
	}
	
	/**
	 * @return encode address
	 * @throws Exception 
	 */
	public String getEncAddress() throws Exception {
		return publicKey.getEncAddress();
	}
	
	/**
	 * @param pKey encode public key
	 * @return encode address
	 * @throws Exception 
	 */
	public static String getEncAddress(String pKey) throws Exception {
		return PublicKey.getEncAddress(pKey);
	}
	
	/**
	 * sign message
	 * @param msg message
	 * @return sign data
	 * @throws Exception
	 */
	public byte[] sign(byte[] msg) throws Exception {
		return signMessage(msg, keyMember);
	}
	
	/**
	 * sign message
	 * @param msg message
	 * @param skey private key
	 * @return sign data
	 * @throws Exception
	 */
	public static byte[] sign(byte[] msg, String skey) throws Exception {
		KeyMember member = new KeyMember();
		getPrivateKey(skey, member);
		byte[] rawPKey = getPublicKey(member);
		member.setRawPKey(rawPKey);
		return signMessage(msg, member);
	}
	
	private static void getPrivateKey(String bSkey, KeyMember member) throws Exception {
		if (null == bSkey) {
			throw new Exception("private key cannot be null");
		}
		
		byte[] skeyTmp = Base58.decode(bSkey);
		if (skeyTmp.length <= 9) {
			throw new Exception("private key (" + bSkey + ") is invalid");
		}
		
		if (skeyTmp[3] > 4 || skeyTmp[3] < 1) {
			throw new Exception("private key (" + bSkey + ") is invalid");
		}
		KeyType type = KeyType.values()[skeyTmp[3] - 1];
		
		// checksum
		if (!CheckKey.CheckSum(type, skeyTmp)) {
			throw new Exception("private key (" + bSkey + ") is invalid");
		}
		
		byte[] rawSKey = new byte[skeyTmp.length - 9];
		System.arraycopy(skeyTmp, 4, rawSKey, 0, rawSKey.length);
		
		member.setKeyType(type);
		member.setRawSKey(rawSKey);
	}
	private static byte[] getPublicKey(KeyMember member) throws Exception {
		byte[] rawPKey = null;
		switch (member.getKeyType()) {
		case ED25519: {
	        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
	        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(member.getRawSKey(), spec);
	        EdDSAPublicKeySpec spec2 = new EdDSAPublicKeySpec(privKey.getA(),spec);
	        EdDSAPublicKey pDsaPublicKey = new EdDSAPublicKey(spec2);
	        rawPKey = pDsaPublicKey.getAbyte();
			break;
		}
		case ECCSM2: {
			SM2PrivateKey sm2PrivateKey = new SM2PrivateKey(member.getRawSKey());
			SM2PublicKey sm2PublicKey =  sm2PrivateKey.getSM2PublicKey();
			rawPKey = Sadk.getSM2PublicKey(sm2PublicKey);
			break;
		}
		default:
			throw new Exception("type does not exist");
		}
		return rawPKey;
	}
	private static String EncPrivateKey(KeyType type, byte[] raw_skey) throws Exception {
		if (null == raw_skey) {
			throw new Exception("private key is null");
		}
		byte[] buff = new byte[raw_skey.length + 5];
		buff[0] = (byte) 0xDA;
		buff[1] = (byte) 0x37;
		buff[2] = (byte) 0x9F;
		System.arraycopy(raw_skey, 0, buff, 4, raw_skey.length);
		
		buff[3] = (byte) (type.ordinal() + 1);
		
		byte[] hash1 = CheckKey.CalHash(type, buff);
		byte[] hash2 = CheckKey.CalHash(type, hash1);

		byte[] tmp = new byte[buff.length + 4];

		System.arraycopy(buff, 0, tmp, 0, buff.length);
		System.arraycopy(hash2, 0, tmp, buff.length, 4);
		
		return Base58.encode(tmp);
	}
	private static String encPublicKey(KeyType type, byte[] raw_pkey) throws Exception {
		if (null == raw_pkey) {
			throw new Exception("public key is null");
		}
		int length = raw_pkey.length + 2;
		byte[] buff = new byte[length];
		buff[0] = (byte)0xB0;
		buff[1] = (byte) (type.ordinal() + 1);

		System.arraycopy(raw_pkey, 0, buff, 2, raw_pkey.length);
		
		byte[] hash1 = CheckKey.CalHash(type, buff);
		byte[] hash2 = CheckKey.CalHash(type, hash1);
		byte[] tmp = new byte[buff.length + 4];

		System.arraycopy(buff, 0, tmp, 0, buff.length);
		System.arraycopy(hash2, 0, tmp, buff.length, 4);
		
		return HexFormat.byteToHex(tmp);
	}
	private static byte[] signMessage(byte[] msg, KeyMember member) throws Exception {
		if (null == member.getRawSKey()) {
			throw new Exception("raw private key is null");
		}
		byte[] signMsg = null;
		
		switch (member.getKeyType()) {
		case ED25519: {
			Signature sgr = new EdDSAEngine(MessageDigest.getInstance("SHA-512"));
			EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
			EdDSAPrivateKeySpec sKeySpec = new EdDSAPrivateKeySpec(member.getRawSKey(), spec);
			EdDSAPrivateKey sKey = new EdDSAPrivateKey(sKeySpec);
			sgr.initSign(sKey);
			sgr.update(msg);
			
			signMsg = sgr.sign();
			break;
		}
			
			
		case ECCSM2: {
			final String deviceName = JCrypto.JSOFT_LIB;
			JCrypto.getInstance().initialize(deviceName, null);
			Session session = JCrypto.getInstance().openSession(deviceName);
			final cfca.sadk.util.Signature signature = new cfca.sadk.util.Signature();
			
			SM2PrivateKey sm2PrivateKey = KeyUtil.getSM2PrivateKey(member.getRawSKey(), null, null) ;
			SM2PublicKey sm2PublicKey  = Sadk.getSM2PublicKey(member.getRawPKey());
			final byte[] userId = "1234567812345678".getBytes("UTF8");
			final String signAlg = Mechanism.SM3_SM2;
	        // 
	        byte[] hash = HashUtil.SM2HashMessageByBCWithZValue(userId, msg, sm2PublicKey.getPubXByInt(), sm2PublicKey.getPubYByInt());
	        signMsg = Sadk.ASN1toRS(Base64.decode(signature.p1SignByHash(signAlg, hash, sm2PrivateKey, session)));
			break;
		}
		default:
			throw new Exception("type does not exist");
		}
		
		return signMsg;
	}

}
