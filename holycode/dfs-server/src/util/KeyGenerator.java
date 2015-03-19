package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KeyGenerator {
	
	static private final MessageDigest md;
	static {
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException ex) {
//			Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Generate random 40-character key
	 * @return generated key
	 */
	public String generate() {
		String key = null;
		SecureRandom random = new SecureRandom();
		key = "s" + System.currentTimeMillis() + random.nextLong();
		try {
			MessageDigest localmd = (MessageDigest) KeyGenerator.md.clone();
			localmd.update(key.getBytes());
			byte[] bt = localmd.digest();
			BigInteger bigInt = new BigInteger(bt);
			key = bigInt.abs().toString(16);
		} catch (CloneNotSupportedException ex) {
			Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
		return key;
	}
}
