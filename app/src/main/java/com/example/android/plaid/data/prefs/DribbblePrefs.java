/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.plaid.data.prefs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.example.android.plaid.data.api.dribbble.model.User;

/**
 * Created by nickbutcher on 2/7/15.
 */
public class DribbblePrefs {

    public static final String PLAID_CLIENT_ID =
            "e07b449e642134b6fd461df4ede20da7a0887b50748162cad43bb7d47df2ca2f";
    public static final String PLAID_CLIENT_SECRET =
            "00d2594bfc22d1c4e9f6965867d9be47da0e1d681cdab94ef7e62c838d07de47";
    public static final String LOGIN_CALLBACK = "auth-callback";
    public static final String LOGIN_URL = "https://dribbble.com/oauth/authorize?client_id=" +
            PLAID_CLIENT_ID
            + "&redirect_uri=plaid%3A%2F%2F" + LOGIN_CALLBACK +
            "&scope=public+write+comment+upload";
    private static final String DRIBBBLE_PREF = "DRIBBBLE_PREF";
    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_USER_AVATAR = "KEY_USER_AVATAR";
    private static final String PLAID_CLIENT_ACCESS_TOKEN =
            "edc78103a66d939a3a4bf069bfdf37257b9710d4dbfdd8ad3578677cff6b2383";
    private final SharedPreferences prefs;

    private String accessToken;
    private boolean isLoggedIn = false;
    private long userId;
    private String username;
    private String userAvatar;

    public DribbblePrefs(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(DRIBBBLE_PREF, Context
                .MODE_PRIVATE);
        accessToken = prefs.getString(KEY_ACCESS_TOKEN, null);
        isLoggedIn = !TextUtils.isEmpty(accessToken);
        if (isLoggedIn) {
            userId = prefs.getLong(KEY_USER_ID, 0l);
            username = prefs.getString(KEY_USER_NAME, null);
            userAvatar = prefs.getString(KEY_USER_AVATAR, null);
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getAccessToken() {
        return !TextUtils.isEmpty(accessToken) ? accessToken : PLAID_CLIENT_ACCESS_TOKEN;
    }

    public void setAccessToken(String accessToken) {
        if (!TextUtils.isEmpty(accessToken)) {
            this.accessToken = accessToken;
            isLoggedIn = true;
            prefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
        }
    }

    public void setLoggedInUser(User user) {
        if (user != null) {
            username = user.username;
            userId = user.id;
            userAvatar = user.avatar_url;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(KEY_USER_ID, userId);
            editor.putString(KEY_USER_NAME, username);
            editor.putString(KEY_USER_AVATAR, userAvatar);
            editor.apply();
        }
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return username;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void logout() {
        isLoggedIn = false;
        accessToken = null;
        userId = 0l;
        username = null;
        userAvatar = null;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, null);
        editor.putLong(KEY_USER_ID, 0l);
        editor.putString(KEY_USER_NAME, null);
        editor.putString(KEY_USER_AVATAR, null);
        editor.apply();
    }

    public void login(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LOGIN_URL)));
    }
}