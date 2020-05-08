package com.example.android.pokeassistant;

import com.ibm.watson.developer_cloud.assistant.v1.model.Context;

public interface ContextListener {
    void setLastContext(Context context);
}
