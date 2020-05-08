package com.example.android.pokeassistant;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageOption implements Parcelable{

    private String label;
    private String inputText;

    public MessageOption() {
        this.label = "";
        this.inputText = "";
    }

    protected MessageOption(Parcel in) {
        label = in.readString();
        inputText = in.readString();
    }

    public static final Creator<MessageOption> CREATOR = new Creator<MessageOption>() {
        @Override
        public MessageOption createFromParcel(Parcel in) {
            return new MessageOption(in);
        }

        @Override
        public MessageOption[] newArray(int size) {
            return new MessageOption[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(inputText);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public TextView getView(Context context){
        TextView textView = new TextView(context);
        textView.setText(label);
        textView.setTextSize(18);
        textView.setPadding(10,10,10,10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(params);
        textView.setTextColor(context.getResources().getColor(R.color.colorLink));
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot, 0, 0, 0);
        return textView;
    }

    @Override
    public String toString() {
        return label;
    }
}
