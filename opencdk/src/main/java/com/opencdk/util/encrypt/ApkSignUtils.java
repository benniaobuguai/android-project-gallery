package com.opencdk.util.encrypt;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2014-10-3
 * @Modify 2016-4-2
 */
public class ApkSignUtils {

	/**
	 * MD5指纹信息
	 * 
	 * @param context
	 * @return
	 */
	public static final String getMD5Fingerprint(Context context) {
		return getFingerprint(context, "MD5");
	}

	/**
	 * SHA1指纹信息
	 * 
	 * @param context
	 * @return
	 */
	public static final String getSHA1Fingerprint(Context context) {
		return getFingerprint(context, "SHA1");
	}

	/**
	 * 返回指定算法对应的指纹信息
	 * 
	 * @param context
	 * @param algorithm MD5 or SHA1
	 * @return
	 */
	private static String getFingerprint(Context context, String algorithm) {
		try {
			String packageName = context.getApplicationContext().getApplicationInfo().packageName;
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.reset();
			md.update(signs[0].toByteArray());
			byte[] dataHash = md.digest();
			String hexString = bytes2HexString(dataHash);
			return hexString;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 返回指定Apk文件的MD5指纹信息
	 * 
	 * @param context
	 * @param archiveFilePath
	 * @return
	 */
	public static final String getMD5Fingerprint(Context context, String archiveFilePath) {
		return getFingerprint(context, archiveFilePath, "MD5");
	}

	/**
	 * 返回指定Apk文件的SHA1指纹信息
	 * 
	 * @param context
	 * @param archiveFilePath
	 * @return
	 */
	public static final String getSHA1Fingerprint(Context context, String archiveFilePath) {
		return getFingerprint(context, archiveFilePath, "SHA1");
	}

	/**
	 * 返回指定Apk文件的指纹信息
	 * 
	 * @param context
	 * @param archiveFilePath
	 * @param algorithm MD5 or SHA1
	 * @return
	 */
	public static String getFingerprint(Context context, String archiveFilePath, String algorithm) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(archiveFilePath,
					PackageManager.GET_SIGNATURES);
			if (packageInfo == null) {
				return null;
			}
			Signature[] signs = packageInfo.signatures;
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.reset();
			md.update(signs[0].toByteArray());
			byte[] dataHash = md.digest();
			return bytes2HexString(dataHash);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static final char HEX_CHARS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	public static String bytes2HexString(byte[] b) {
		if (b == null || b.length == 0) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			sb.append(':');
			// 无符号右移，忽略符号位，空位都以0补齐
			sb.append(HEX_CHARS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_CHARS[b[i] & 0x0f]);
		}

		return sb.substring(1);
	}

	public static String hex2ByteString(String hexString) {
		String hexChars = new String(HEX_CHARS);

		ByteArrayOutputStream baos = new ByteArrayOutputStream(hexString.length() / 2);
		for (int i = 0; i < hexString.length(); i += 2) {
			// 将每2位16进制整数组装成一个字节

			baos.write((hexChars.indexOf(hexString.charAt(i)) << 4 | hexChars.indexOf(hexString.charAt(i + 1))));
		}

		return new String(baos.toByteArray());
	}

}
