package com.example.android.pokeassistant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Message implements Parcelable{
    private String text;
    private String imageUrl;
    private String optionsTitle;
    private ArrayList<MessageOption> options;
    private Type type;

    public enum Type {
        USER,
        WATSON_TEXT,
        WATSON_IMAGE_TEXT,
        WATSON_IMAGE_TEXT_OPTIONS,
        WATSON_IMAGE_SRC_TEXT,
        WATSON_OPTIONS,
        WATSON_TEXT_OPTIONS
    }

    public Message() {
        text = "";
        imageUrl = "";
        optionsTitle = "";
        options = new ArrayList<>();
        type = Type.WATSON_TEXT;
    }

    protected Message(Parcel in) {
        text = in.readString();
        imageUrl = in.readString();
        optionsTitle = in.readString();
        options = in.createTypedArrayList(MessageOption.CREATOR);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(imageUrl);
        dest.writeString(optionsTitle);
        dest.writeTypedList(options);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getOptionsTitle() {
        return optionsTitle;
    }

    public void setOptionsTitle(String optionsTitle) {
        this.optionsTitle = optionsTitle;
    }

    public void addOption(MessageOption option){
        options.add(option);
    }

    public void addAllOptions(ArrayList<MessageOption> ops){
        options.addAll(ops);
    }

    public ArrayList<MessageOption> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        switch (type){
            case USER:
                builder.append("USER: ")
                        .append(text);
                break;
            case WATSON_TEXT:
                builder.append("WATSON_TEXT: ")
                        .append(text);
                break;
            case WATSON_IMAGE_TEXT:
                builder.append("WATSON_IMAGE_TEXT: ")
                        .append("ImageUrl: ")
                        .append(imageUrl)
                        .append(" - Text: ")
                        .append(text);
                break;
            case WATSON_OPTIONS:
                builder.append("WATSON_OPTIONS: ")
                        .append("Title: ")
                        .append(optionsTitle)
                        .append(" - Options: ")
                        .append(options);
                break;
            case WATSON_TEXT_OPTIONS:
                builder.append("WATSON_TEXT_OPTIONS: ")
                        .append("Text: ")
                        .append(text)
                        .append(" - Title: ")
                        .append(optionsTitle)
                        .append(" - Options: ")
                        .append(options);
                break;
        }
        return builder.toString();
    }
}
