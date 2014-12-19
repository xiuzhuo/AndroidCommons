package com.bonofa.cube7api;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import angel.zhuoxiu.library.net.Conn;
import angel.zhuoxiu.library.net.HttpResult;

/**
	You should make post to
	http://c7dev.sevendevs.de/oauth/token
	with:
	{ "username":"tester0@test.com",
	"client_secret":"29e8ba15dc1fbb22504ede3fee3da4dc3e71d144cf21278e3576577a278d9c84",
	"client_id":"c3e77287c974977928a072c3f831d7055dc18c191460723cbae22861b55cf5fb",
	"grant_type":"password",
	"redirect_uri":"http://c7dev.sevendevs.de",
	"password":"defaultpw"}
	and will get smth like
	{
	access_token: "d8ce15ddd45fbffe9d0f18d69433654c32c751f61b8140355153acaa20d4b59b"
	token_type: "bearer"
	expires_in: 7200
	refresh_token: "263b97ce9c1bae0d7209cb0e770be7607e19ab31664e914d49c18912c1a77646"
	scope: "read"
	}
	And then get to http://localhost:3000/api/accounts/current
	with Authorization: Bearer 6583674903a941b916a7a88846f053741d693dc8b732e3b3df0fd6a58eff9611
	will return current account data with jid that you will use for ejabberd auth
	Show less
	this will be data for current account
	post 'http://c7dev.sevendevs.de/api/accounts/find_email_by_jid'
	params jid: jid
	post 'http://c7dev.sevendevs.de/api/accounts/find_jid_by_email'
	params email: email
	@JanisMiezitis
	post 'http://c7dev.sevendevs.de/api/accounts/try_authorize'
	params
	email: email
	password: crypted_password
	Don't forget about sending access token in headers
	* @author Zhuo Xiu
*
*/
public class JIDManager {
	static String tag = JIDManager.class.getSimpleName();

	public static String getJIDByC7Account(Context context, String myC7Email, String password) {
		return getJIDByC7Account(context, myC7Email, password, myC7Email);
	}

	public static String getJIDByC7Account(Context context, String myC7Email, String password, String targetC7Email) {
		if (context == null || myC7Email == null || password == null || targetC7Email == null) {
			return null;
		}
		String jid = CacheManager.getJIDByC7(context, targetC7Email);
		Log.i(tag, "Cache targetC7Email=" + targetC7Email + " jid=" + jid);
		if (jid == null) {
			String token = TokenManager.getC7Token(context, myC7Email, password);
			Log.i(tag, "token=" + token);
			if (token != null) {
				String url = "http://c7dev.sevendevs.de/api/accounts/find_jid_by_email";
				try {
					JSONObject object = new JSONObject();
					object.put("email", targetC7Email);
					Conn conn = new Conn(url, object);
					conn.addHeader("Authorization", "Bearer " + token);
					HttpResult result = conn.execute();
					Log.i(tag, result.toString());
					if (result.isOK()) {
						Log.i(tag, result.getEntityString());
						jid = new JSONObject(result.getEntityString()).getString("jid");
						Log.i(tag, "Network targetC7Email=" + targetC7Email + " jid=" + jid);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (jid != null) {
				CacheManager.setC7AndJID(context, myC7Email, jid);
			}
		}
		return jid;
	}

	public static String getC7AccountByJID(Context context, String jid, String password) {
		if (context == null || jid == null || password == null) {
			return null;
		}
		String c7Email = CacheManager.getC7ByJID(context, jid);
		Log.i(tag, "JID Cache c7Email=" + c7Email + " jid=" + jid);
		if (c7Email == null) {
			String token = TokenManager.getC7Token(context, jid, password);
			Log.i(tag, "token=" + token);
			if (token != null) {
				String url = "http://c7dev.sevendevs.de/api/accounts/find_email_by_jid";
				try {
					JSONObject object = new JSONObject();
					object.put("jid", jid);
					Conn conn = new Conn(url, object);
					conn.addHeader("Authorization", "Bearer " + token);
					HttpResult result = conn.execute();
					Log.i(tag, result.toString());
					if (result.isOK()) {
						Log.i(tag, result.getEntityString());
						c7Email = new JSONObject(result.getEntityString()).getString("email");
						Log.i(tag, "Network jid=" + jid + " c7Email=" + c7Email);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (c7Email != null) {
				CacheManager.setC7AndJID(context, c7Email, jid);
			}
		}
		return c7Email;
	}

}