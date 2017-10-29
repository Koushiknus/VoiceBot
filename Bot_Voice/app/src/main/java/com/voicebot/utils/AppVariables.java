package com.voicebot.utils;


import android.content.Context;
import android.content.SharedPreferences;


public class AppVariables {
	private static final String APP_SETTINGS_PREFERENCES = "ApplicationSettingPreferences";

	public static final int LANGUAGE_EN = 0;
	public static final int LANGUAGE_CN = 1;
	public static final int LANGUAGE_KO = 2;
	public static final int LANGUAGE_JA = 3;

	public static final int ORIENTATION_PORT = 0;
	public static final int ORIENTATION_LAND = 1;
	
	//TODO App Config
	private static final String KEY_ORIENTATION = "Orientation";
	private static final String KEY_LANGUAGE_NAME = "LanguageName";

	//TODO App Info
	private static final String KEY_PRINT = "print";
	private static final String KEY_OLD_PASSWORD1 = "oldPassword1";
	private static final String KEY_OLD_PASSWORD2 = "oldPassword2";
	private static final String KEY_OLD_PASSWORD3 = "oldPassword3";
	private static final String KEY_PASS_WORD = "passWord";
	private static final String KEY_PW_EXPIRE_DATE = "PwExpireDate";
	private static final String KEY_AUTH_DONE = "authDone";
	private static final String KEY_TERMINAL_TYPE = "terminalType";
	private static final String PREVIOUS_ID = "previousId";
	private static byte[] mSignImage = null;
	private static final String KEY_SELECTED_DONGLE = "selectedDongle";

	public static SharedPreferences getSharedPref(Context ctx) {
		return ctx.getSharedPreferences(APP_SETTINGS_PREFERENCES, Context.MODE_PRIVATE);
	}

	// =========================================================================
	// Getter
	// =========================================================================

	public static String getPrint(Context ctx, String defaultValue) {
		return getSharedPref(ctx).getString(KEY_PRINT, defaultValue);
	}

	public static String getOldPassword1(Context ctx, String defaultValue) {
		return getSharedPref(ctx).getString(KEY_OLD_PASSWORD1, defaultValue);
	}

	public static String getOldPassword2(Context ctx, String defaultValue) {
		return getSharedPref(ctx).getString(KEY_OLD_PASSWORD2, defaultValue);
	}

	public static String getOldPassword3(Context ctx, String defaultValue) {
		return getSharedPref(ctx).getString(KEY_OLD_PASSWORD3, defaultValue);
	}

	public static Boolean getAuthDone(Context ctx) {
		return getSharedPref(ctx).getBoolean(KEY_AUTH_DONE, false);
	}

	public static String getPassWord(Context ctx, String defaultValue) {
		return getSharedPref(ctx).getString(KEY_PASS_WORD, defaultValue);
	}

	public static String getPwExpireDate(Context ctx, String defaultValue) {
		return getSharedPref(ctx).getString(KEY_PW_EXPIRE_DATE, defaultValue);
	}

	public static String getTerminalType(Context ctx) {
		return getSharedPref(ctx).getString(KEY_TERMINAL_TYPE, null);
	}

	public static int getLanguageInfo(Context ctx, int defaultValue) {
		return getSharedPref(ctx).getInt(KEY_LANGUAGE_NAME, defaultValue);
	}

	public static int getOrientationSetting(Context ctx) {
		return getSharedPref(ctx).getInt(KEY_ORIENTATION, 0);
	}

	public static String getPreviousId(Context ctx) {
		return getSharedPref(ctx).getString(PREVIOUS_ID, "");
	}
	public static String getLanguageCode(Context ctx) {
		int language = getSharedPref(ctx).getInt(KEY_LANGUAGE_NAME, 0);
		String languageCode = null;
		switch (language) {
			case LANGUAGE_EN:
				languageCode = "EN";
				break;

			case LANGUAGE_CN:
				languageCode = "CN";
				break;

			case LANGUAGE_KO:
				languageCode = "KR";
				break;

			case LANGUAGE_JA:
				languageCode = "JP";
				break;

			default:
				languageCode = "EN";
				break;
		}
		return languageCode;
	}

	public static byte[] getSignImage() {
		return mSignImage;
	}
	// =========================================================================
	// Setter
	// =========================================================================

	public static boolean setPassword(Context ctx, boolean authDone, String pw,
									  String oldPw1, String oldPw2, String oldPw3, String expireDate) {
		SharedPreferences.Editor editor = getSharedPref(ctx).edit();
		editor.putBoolean(KEY_AUTH_DONE, authDone);
		editor.putString(KEY_PASS_WORD, pw);
		if (oldPw1 != null)
			editor.putString(KEY_OLD_PASSWORD1, oldPw1);
		if (oldPw2 != null)
			editor.putString(KEY_OLD_PASSWORD2, oldPw2);
		if (oldPw3 != null)
			editor.putString(KEY_OLD_PASSWORD3, oldPw3);
		editor.putString(KEY_PW_EXPIRE_DATE, expireDate);
		return editor.commit();
	}

	public static void setLanguageInfo(Context ctx, int languageInfo) {
		SharedPreferences.Editor editor = getSharedPref(ctx).edit();
		editor.putInt(KEY_LANGUAGE_NAME, languageInfo);
		editor.apply();
	}

	public static void setOrientationSetting(Context ctx, int value) {
		SharedPreferences.Editor editor = getSharedPref(ctx).edit();
		editor.putInt(KEY_ORIENTATION, value);
		editor.apply();
	}

	public static boolean setPreviousId(Context ctx, String id) {
		SharedPreferences.Editor editor = getSharedPref(ctx).edit();
		editor.putString(PREVIOUS_ID, id);
		return editor.commit();
	}

	public static boolean setTerminalType(Context ctx, String type) {
		SharedPreferences.Editor editor = getSharedPref(ctx).edit();
		editor.putString(KEY_TERMINAL_TYPE, type);
		return editor.commit();
	}

	public static void setSignImage(byte[] signImage) {
		mSignImage = signImage;
	}
}
