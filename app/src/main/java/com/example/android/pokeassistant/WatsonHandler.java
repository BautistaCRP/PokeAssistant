package com.example.android.pokeassistant;

import android.widget.Toast;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

public class WatsonHandler implements ContextListener{
    public static final String APIKEY = "X7rs9_0KyhpeVfFTV7ECcpb4WKyXohNidd-u4EY-qGH8";
    public static final String VERSION = "2018-09-20";
    public static final String WORKSPACE_ID = "1defb899-3c40-4a68-8474-79c1898f8233";
    public static final String URL = "https://gateway.watsonplatform.net/assistant/api";

    private static final String TAG = "WatsonHandler";

    private Assistant assistant;
    private MessageListener listener;
    private Context lastContext;


    public WatsonHandler(MessageListener listener) {
        this.listener = listener;
        lastContext = null;
        IamOptions options = new IamOptions.Builder()
                .apiKey(APIKEY)
                .build();

        this.assistant = new Assistant(VERSION, options);
        this.assistant.setEndPoint(URL);
        sendMessage("");
    }

    public void sendMessage(String text){
        InputData input = new InputData.Builder(text).build();
        MessageOptions options;

        if(lastContext != null)
            options = new MessageOptions.Builder(WORKSPACE_ID)
                    .input(input)
                    .context(lastContext)
                    .build();
        else
            options = new MessageOptions.Builder(WORKSPACE_ID)
                    .input(input)
                    .build();

        new SendMessageTask(assistant,options,listener,this).execute();
    }

    @Override
    public void setLastContext(Context context) {
        lastContext = context;
    }

    public void clearContext() {
        lastContext = null;
    }
}
