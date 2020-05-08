package com.example.android.pokeassistant;

import android.os.AsyncTask;
import android.util.Log;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.DialogNodeOutputOptionsElement;
import com.ibm.watson.developer_cloud.assistant.v1.model.DialogRuntimeResponseGeneric;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant.v1.model.OutputData;

import java.util.ArrayList;
import java.util.List;

public class SendMessageTask extends AsyncTask<Object,Object,MessageResponse> {

    private Assistant assistant;
    private MessageOptions messageOptions;
    private MessageListener messageListener;
    private ContextListener contextListener;

    private Message message;
    private ArrayList<String> texts;
    private ArrayList<String> images;
    private ArrayList<String> optionsTitles;
    private ArrayList<ArrayList<MessageOption>> options;


    private static final String TAG = "SendMessageTask";

    public SendMessageTask(Assistant assistant, MessageOptions messageOptions, MessageListener messageListener, ContextListener contextListener) {
        this.assistant = assistant;
        this.messageOptions = messageOptions;
        this.messageListener = messageListener;
        this.contextListener = contextListener;

        this.message = new Message();
        this.texts = new ArrayList<>();
        this.images = new ArrayList<>();
        this.optionsTitles = new ArrayList<>();
        this.options = new ArrayList<>();
    }

    @Override
    protected MessageResponse doInBackground(Object[] objects) {

        try {
            MessageResponse response = assistant.message(messageOptions).execute();
            Log.d(TAG, "doInBackground: \n" + response);
            return response;
        } catch (RuntimeException e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(MessageResponse o) {
        if(o == null){
            Message message = new Message();
            message.setType(Message.Type.WATSON_IMAGE_SRC_TEXT);
            message.setText("Hubo un problema en la conexión, reintente nuevamente");
            message.setImageUrl("img_psyduck_confused");
            messageListener.setResponse(message);
            return;
        }
        OutputData outputData = o.getOutput();
        List<DialogRuntimeResponseGeneric> list = outputData.getGeneric();

        for(DialogRuntimeResponseGeneric value : list){
            String type = value.getResponseType();
            switch (type) {
                case "text":
                    texts.add(value.getText());
                    break;
                case "image":
                    images.add(value.getSource());
                    break;
                case "option":
                    optionsTitles.add(value.getTitle());
                    ArrayList<MessageOption> actualOptions = new ArrayList<>();
                    List<DialogNodeOutputOptionsElement> elems =  value.getOptions();
                    for(DialogNodeOutputOptionsElement elem : elems){
                        MessageOption messageOption = new MessageOption();
                        messageOption.setLabel(elem.getLabel());
                        messageOption.setInputText(elem.getValue().getInput().text());
                        actualOptions.add(messageOption);
                    }
                    options.add(actualOptions);
                    break;
            }
        }

        buildMessage();
        messageListener.setResponse(message);
        contextListener.setLastContext(o.getContext());

    }


    private boolean hasImage(){
        return !images.isEmpty();
    }

    private boolean hasOptions(){
        return !optionsTitles.isEmpty();
    }

    private boolean hasText(){
        return !texts.isEmpty();
    }


    private void buildMessage(){

        if(!hasText() && !hasImage() && !hasOptions()){
            message.setType(Message.Type.WATSON_TEXT);
            message.setText("No comprendi, podrias repetirlo?");
            return;
        }

        if(hasImage() && hasText() && hasOptions()){
            message.setType(Message.Type.WATSON_IMAGE_TEXT_OPTIONS);
            message.setText(texts.get(0));
            message.setImageUrl(images.get(0));
            message.setOptionsTitle(optionsTitles.get(0));
            message.addAllOptions(options.get(0));
            return;
        }

        if(hasImage() && hasText()){
            message.setType(Message.Type.WATSON_IMAGE_TEXT);
            message.setText(texts.get(0));
            message.setImageUrl(images.get(0));
            return;
        }
        if(hasOptions() && hasText()){
            message.setType(Message.Type.WATSON_TEXT_OPTIONS);
            message.setText(texts.get(0));
            message.setOptionsTitle(optionsTitles.get(0));
            message.addAllOptions(options.get(0));
            return;
        }

        if(hasOptions() && !hasText()){
            message.setType(Message.Type.WATSON_OPTIONS);
            message.setOptionsTitle(optionsTitles.get(0));
            message.addAllOptions(options.get(0));
            return;
        }

        if(hasText()){
            message.setType(Message.Type.WATSON_TEXT);
            message.setText(texts.get(0));
            return;
        }

    }
}
