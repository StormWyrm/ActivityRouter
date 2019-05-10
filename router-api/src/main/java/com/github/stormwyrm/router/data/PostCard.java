package com.github.stormwyrm.router.data;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import com.github.stormwyrm.router.Router;

import java.io.Serializable;

/**
 * 封装用户请求数据的类
 */
public class PostCard {
    private Bundle extras;
    private String path;

    public PostCard(String path) {
        this.path = path;
        extras = new Bundle();
    }

    public PostCard(String path, Bundle extras) {
        this.extras = extras;
        this.path = path;
    }

    public Object navigation() {
        return navigation(null);
    }

    public Object navigation(Context context) {
        return navigation(context, null);
    }

    public Object navigation(Context context, NavigationCallback callback) {
        return Router.getInstance().navigation(context, this, -1, callback);
    }

    public Object navigation(Activity activity, int requestCode, NavigationCallback callback) {
        return Router.getInstance().navigation(activity, this, requestCode, callback);
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PostCard withBundle(String key, Bundle value) {
        if (value != null) {
            extras.putBundle(key, value);
        }
        return this;
    }

    public PostCard withByte(String key, byte value) {
        extras.putByte(key, value);
        return this;
    }

    public PostCard withShort(String key, short value) {
        extras.putShort(key, value);
        return this;
    }

    public PostCard withInt(String key, int value) {
        extras.putInt(key, value);
        return this;
    }

    public PostCard withLong(String key, long value) {
        extras.putLong(key, value);
        return this;
    }

    public PostCard withFloat(String key, float value) {
        extras.putFloat(key, value);
        return this;
    }

    public PostCard withDouble(String key, double value) {
        extras.putDouble(key, value);
        return this;
    }

    public PostCard withBoolean(String key, boolean value) {
        extras.putBoolean(key, value);
        return this;
    }

    public PostCard withSerializable(String key, Serializable value) {
        if (value != null) {
            extras.putSerializable(key, value);
        }
        return this;
    }

    public PostCard withParcelable(String key, Parcelable value) {
        if (value != null) {
            extras.putParcelable(key, value);
        }
        return this;
    }
}
