package com.bonofa.cube7api;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import angel.zhuoxiu.library.net.Conn;
import angel.zhuoxiu.library.net.HttpResult;

public class TokenManager {
	static String tag = JIDManager.class.getSimpleName();

	public static String getC7Token(Context context, String c7Email, String password) {
		String access_token = getC7TokenInCache(context, c7Email);
		Log.i(tag, "C7Token cache result=" + access_token);
		if (access_token == null) {
			String url = "http://c7dev.sevendevs.de/oauth/token";
			try {
				JSONObject object = new JSONObject();
				object.put("username", c7Email);
				object.put("client_secret", "29e8ba15dc1fbb22504ede3fee3da4dc3e71d144cf21278e3576577a278d9c84");
				object.put("client_id", "c3e77287c974977928a072c3f831d7055dc18c191460723cbae22861b55cf5fb");
				object.put("grant_type", "password");
				object.put("password", password);
				object.put("redirect_uri", "http://c7dev.sevendevs.de");
				Conn conn = new Conn(url, object);
				HttpResult result = conn.execute();
				Log.i(tag, "object=" + object.toString(2));
				Log.i(tag, "C7Token online result=" + result);
				if (result.isOK()) {
					JSONObject object2 = new JSONObject(result.getEntityString());
					access_token = object2.getString("access_token");
					CacheManager.setC7Token(context, c7Email, access_token);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return access_token;
	}

	public static String getC7TokenInCache(Context context, String c7Email) {
		return CacheManager.getC7Token(context, c7Email);
	}

}
