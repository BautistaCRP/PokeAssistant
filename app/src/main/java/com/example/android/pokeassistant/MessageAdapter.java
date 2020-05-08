package com.example.android.pokeassistant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private static final String TAG = "MessageAdapter";

    List<Message> messages;
    Context appContext;
    WatsonHandler watsonHandler;

    public MessageAdapter(Context appContext, WatsonHandler watsonHandler) {
        this.appContext = appContext;
        this.watsonHandler = watsonHandler;
        messages = new ArrayList<>();
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    public void addAll(Collection<Message> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    public void remove(int index){
        if(index < messages.size()){
            messages.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater messageInflater = (LayoutInflater) appContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final Message message = messages.get(i);

        switch (message.getType()){
            case USER:
                UserViewHolder userViewHolder = new UserViewHolder();
                convertView = messageInflater.inflate(R.layout.user_message, null);
                userViewHolder.text = convertView.findViewById(R.id.text);
                userViewHolder.text.setText(message.getText());
                convertView.setTag(userViewHolder);
                break;

            case WATSON_TEXT:
                WatsonTextViewHolder watsonTextViewHolder = new WatsonTextViewHolder();
                convertView = messageInflater.inflate(R.layout.watson_message_text, null);
                watsonTextViewHolder.text = convertView.findViewById(R.id.text);
                watsonTextViewHolder.text.setText(message.getText());
                convertView.setTag(watsonTextViewHolder);
                break;

            case WATSON_IMAGE_TEXT:
                WatsonImageTextViewHolder watsonImageTextViewHolder = new WatsonImageTextViewHolder();
                convertView = messageInflater.inflate(R.layout.watson_message_image_text, null);
                watsonImageTextViewHolder.text = convertView.findViewById(R.id.text);
                watsonImageTextViewHolder.text.setText(message.getText());
                watsonImageTextViewHolder.imageView = convertView.findViewById(R.id.image);
                Glide.with(appContext)
                        .load(message.getImageUrl())
                        .apply(new RequestOptions().override(800, 600))
                        .thumbnail(0.1f)
                        .into(watsonImageTextViewHolder.imageView);
                convertView.setTag(watsonImageTextViewHolder);
                break;

            case WATSON_IMAGE_SRC_TEXT:
                WatsonImageTextViewHolder watsonImageTextViewHolder2 = new WatsonImageTextViewHolder();
                convertView = messageInflater.inflate(R.layout.watson_message_image_text, null);
                watsonImageTextViewHolder2.text = convertView.findViewById(R.id.text);
                watsonImageTextViewHolder2.text.setText(message.getText());
                watsonImageTextViewHolder2.imageView = convertView.findViewById(R.id.image);
                watsonImageTextViewHolder2.imageView.setImageResource(appContext.getResources().getIdentifier(message.getImageUrl(), "drawable", appContext.getPackageName()));
                convertView.setTag(watsonImageTextViewHolder2);
                break;

            case WATSON_OPTIONS:
                WatsonOptionsViewHolder watsonOptionsViewHolder = new WatsonOptionsViewHolder();
                convertView = messageInflater.inflate(R.layout.watson_message_options, null);
                watsonOptionsViewHolder.title = convertView.findViewById(R.id.title);
                watsonOptionsViewHolder.title.setText(message.getOptionsTitle());

                LinearLayout linearLayoutO = convertView.findViewById(R.id.linear_layoutO);
                ArrayList<MessageOption> options = message.getOptions();
                for(MessageOption option : options){
                    TextView textView = option.getView(appContext);
                    textView.setOnClickListener(new MessageOptionOnClickListener(watsonHandler,option));
                    linearLayoutO.addView(textView);
                }
                break;


            case WATSON_TEXT_OPTIONS:
                WatsonTextOptionsViewHolder watsonTextOptionsViewHolder = new WatsonTextOptionsViewHolder();
                convertView = messageInflater.inflate(R.layout.watson_message_text_options, null);
                watsonTextOptionsViewHolder.text = convertView.findViewById(R.id.text);
                watsonTextOptionsViewHolder.text.setText(message.getText());
                watsonTextOptionsViewHolder.title = convertView.findViewById(R.id.title);
                watsonTextOptionsViewHolder.title.setText(message.getOptionsTitle());

                LinearLayout linearLayoutTO = convertView.findViewById(R.id.linear_layoutTO);
                ArrayList<MessageOption> options2 = message.getOptions();
                for(MessageOption option : options2){
                    TextView textView = option.getView(appContext);
                    textView.setOnClickListener(new MessageOptionOnClickListener(watsonHandler,option));
                    linearLayoutTO.addView(textView);
                }
                break;

            case WATSON_IMAGE_TEXT_OPTIONS:
                WatsonImageTextOptionsViewHolder watsonImageTextOptionsViewHolder = new WatsonImageTextOptionsViewHolder();
                convertView = messageInflater.inflate(R.layout.watson_message_image_text_options, null);
                watsonImageTextOptionsViewHolder.imageView = convertView.findViewById(R.id.image);
                Glide.with(appContext)
                        .load(message.getImageUrl())
                        .apply(new RequestOptions().override(512, 512))
                        .thumbnail(0.1f)
                        .into(watsonImageTextOptionsViewHolder.imageView);
                watsonImageTextOptionsViewHolder.text = convertView.findViewById(R.id.text);
                watsonImageTextOptionsViewHolder.text.setText(message.getText());
                watsonImageTextOptionsViewHolder.title = convertView.findViewById(R.id.title);
                watsonImageTextOptionsViewHolder.title.setText(message.getOptionsTitle());

                LinearLayout linearLayoutITO = convertView.findViewById(R.id.linear_layoutITO);
                ArrayList<MessageOption> options3 = message.getOptions();
                for(MessageOption option : options3){
                    TextView textView = option.getView(appContext);
                    textView.setOnClickListener(new MessageOptionOnClickListener(watsonHandler,option));
                    linearLayoutITO.addView(textView);
                }
                break;
        }
        return convertView;
    }

    public void clearAll() {
        messages = new ArrayList<>();
        notifyDataSetChanged();
    }

    private class UserViewHolder {
        public TextView text;
    }

    private class WatsonTextViewHolder {
        public TextView text;
    }

    private class WatsonImageTextViewHolder {
        public ImageView imageView;
        public TextView text;
    }

    private class WatsonImageTextOptionsViewHolder {
        public ImageView imageView;
        public TextView text;
        public TextView title;
    }

    private class WatsonOptionsViewHolder {
        public TextView title;
    }

    private class WatsonTextOptionsViewHolder {
        public TextView text;
        public TextView title;
    }

}

