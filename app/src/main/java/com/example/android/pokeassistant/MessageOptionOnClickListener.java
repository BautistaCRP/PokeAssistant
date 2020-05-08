package com.example.android.pokeassistant;

import android.view.View;

public class MessageOptionOnClickListener implements View.OnClickListener {

    private WatsonHandler watsonHandler;
    private MessageOption option;

    public MessageOptionOnClickListener(WatsonHandler watsonHandler, MessageOption option) {
        this.watsonHandler = watsonHandler;
        this.option = option;
    }

    @Override
    public void onClick(View v) {
        watsonHandler.sendMessage(option.getInputText());
    }
}
