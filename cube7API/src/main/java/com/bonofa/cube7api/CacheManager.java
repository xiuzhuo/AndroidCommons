package com.bonofa.cube7api;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheManager {
	static String FILENAME_C7_JID = "c7_jid";
	static String FILENAME_C7_TOKEN = "c7_token";

	public static String getJIDByC7(Context context, String c7Email) {
		return getSharedPreferences(context, FILENAME_C7_JID).getString(c7Email, null);
	}

	public static String getC7ByJID(Context context, String jid) {
		return getSharedPreferences(context, FILENAME_C7_JID).getString(jid, null);
	}

	public static boolean setC7AndJID(Context context, String c7Email, String jid) {
		return getSharedPreferences(context, FILENAME_C7_JID).edit().putString(c7Email, jid).commit()
				&& getSharedPreferences(context, FILENAME_C7_JID).edit().putString(jid, c7Email).commit();
	}

	public static String getC7Token(Context context, String c7Email) {
		return getSharedPreferences(context, FILENAME_C7_TOKEN).getString(c7Email, null);
	}

	public static boolean setC7Token(Context context, String c7Email, String token) {
		return getSharedPreferences(context, FILENAME_C7_TOKEN).edit().putString(c7Email, token).commit();
	}

	static SharedPreferences getSharedPreferences(Context context, String name) {
		return context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
	}

}