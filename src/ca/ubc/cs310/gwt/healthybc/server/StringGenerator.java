package ca.ubc.cs310.gwt.healthybc.server;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * a helper class to generate random strings
 *
 */
public class StringGenerator {
	private static final String LOWER_CASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMERALS = "0123456789";
	private static final String PUNCTUATION = "~!@#$%^&*()-_=+[{}]\\/:;'\",<>./?";
	private static final String DEFAULT_ALGORITHM = "SHA1PRNG";
	
	private static StringGenerator instance = null;
	
	/**
	 * not to be instantiated externally
	 */
	private StringGenerator() {}
	
	/**
	 * Return a SingleGenerator instance.
	 * 
	 * @return singleton SingleGenerator
	 */
	public static StringGenerator getInstance() {
		if (instance == null) {
			instance = new StringGenerator();
		}
		
		return instance;
	}
	
	/**
	 * generate a cryptographically secure string
	 * 
	 * @param minSize minimum size of the string, cannot be 0 or greater than maxSize
	 * @param maxSize maximum size of the string, cannot be 0 or less than minSize
	 * @param caseSensitive whether the string is case sensitive
	 * @param usePunctuation whether punctuation is acceptable in the string
	 * @return resulting string
	 */
	public String generateString(int minSize, int maxSize, boolean caseSensitive, boolean usePunctuation) {
		assert (maxSize >= minSize && minSize > 0 && maxSize > 0);
		
		SecureRandom rng;
		try {
			rng = SecureRandom.getInstance(DEFAULT_ALGORITHM);
		}
		catch (NoSuchAlgorithmException e) {
			//shuold not be possible, but just in case...
			rng = new SecureRandom();
		}
		rng.setSeed(longToByteArray(System.currentTimeMillis()));
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(LOWER_CASE_ALPHABET);
		builder.append(NUMERALS);
		if (caseSensitive) {
			builder.append(LOWER_CASE_ALPHABET.toUpperCase());
		}
		if (usePunctuation) {
			builder.append(PUNCTUATION);
		}
		
		String base = builder.toString();
		int length = rng.nextInt(maxSize - minSize + 1) + minSize;
		
		//reuse string builder by clearing its content
		builder.delete(0, builder.length());
		builder.ensureCapacity(length);
		
		for (int i = 0; i < length; ++i) {
			builder.append(base.charAt(rng.nextInt(base.length())));
		}
		
		return builder.toString();
	}
	
	/**
	 * internal helper method to turn a long integer into an array of signed char
	 * 
	 * @param input input long
	 * @return corresponding byte array 
	 */
	private byte[] longToByteArray(long input) {
		long localInput = input;
		byte[] output = new byte[8];
		
		for (int i = 0; i < output.length; ++i) {
			output[i] = (byte) localInput;
			localInput >>= output.length;
		}
		
		return output;
	}
}
