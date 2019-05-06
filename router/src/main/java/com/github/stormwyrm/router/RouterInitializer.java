package com.github.stormwyrm.router;

import android.app.Activity;

import java.util.Map;

public interface RouterInitializer {
    void init(Map<String, Class<? extends Activity>> sRouter);
}
