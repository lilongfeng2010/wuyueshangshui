package com.wuyueshangshui.yuanxinkangfu.utils.encrypt;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {
	private static byte[] iv1 = { (byte) 0x12, (byte) 0x34, (byte) 0x56,
			(byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

	
	private static String key = "AO89c12G";
	
	public static void main(String[] args) {
		System.out.println("dd");
		//System.out.println("13701014606");
//		DES des = new DES();
//		System.out.print(des.encrypt("13701014606"));
	}

	public static  byte[] desEncrypt(byte[] plainText) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(iv1);

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	
	public static byte[] desDecrypt(byte[] plainText) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(iv1);

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	public static String encrypt(String input) {
		String result = "input";
		try {
			result = base64Encode(desEncrypt(input.getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String d = result.substring(result.length() - 1);
		if (d.equals("\n")) {
			result = result.substring(0, result.length() - 1);
		}

		return result;
	}

	

	public static String decrypt(String output) {
		String result = "output";
		try {
			result = new String(desDecrypt(base64Decode(output.getBytes())),
					"US-ASCII");
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String base64Encode(byte[] s) {
		if (s == null)
			return null;
		return Base64.encodeToString(s, Base64.DEFAULT);
	}

	

	public static byte[] base64Decode(byte[] str) {

		if (str == null)
			return null;

		return Base64.decode(str, Base64.DEFAULT);
	}
}
