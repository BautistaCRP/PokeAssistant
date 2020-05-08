package com.example.android.pokeassistant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MessageListener{

    private EditText editText;
    private FloatingActionButton floatingActionButton;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages;
    private WatsonHandler watsonHandler;
    private boolean isLoading;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLoading = true;
        enableLoadingAnimation();
        messages = new ArrayList<>();
        editText = findViewById(R.id.editText);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        watsonHandler = new WatsonHandler(this);
        messageAdapter = new MessageAdapter(getApplicationContext(),watsonHandler);
        ListView listView = findViewById(R.id.messages_view);
        listView.setAdapter(messageAdapter);
        //Esto es para que el teclado no tape a la listView cuando aparece
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });

        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    onSend();
                    return true;
                }
                return false;
            }
        });
    }

    private void onSend(){
        String text = editText.getText().toString();
        text = text.trim();
        if (!text.equals("")) {
            watsonHandler.sendMessage(text);
            Message message = new Message();
            message.setType(Message.Type.USER);
            message.setText(text);
            messages.add(message);
            messageAdapter.add(message);
            editText.setText("");
            InputMethodManager inputManager = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else{
            editText.setText("");
            InputMethodManager inputManager = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void enableLoadingAnimation(){
        getSupportActionBar().hide();
        LinearLayout linearLayout = findViewById(R.id.animation_layout);
        GifView gifView = findViewById(R.id.gif);
        gifView.play();
        linearLayout.setVisibility(View.VISIBLE);
    }

    private void disableLoadingAnimation(){
        getSupportActionBar().show();
        LinearLayout linearLayout = findViewById(R.id.animation_layout);
        GifView gifView = findViewById(R.id.gif);
        gifView.pause();
        linearLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("messages",messages);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        messages = savedInstanceState.getParcelableArrayList("messages");
        messageAdapter.addAll(messages);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.our_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.clear_chat:
                messages = new ArrayList<>();
                messageAdapter.clearAll();
                watsonHandler.clearContext();
                break;

            case R.id.about_us_item:
                Intent intent = new Intent(getApplicationContext(), AboutUs.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setResponse(Message message) {
        if(isLoading){
            isLoading = false;
            disableLoadingAnimation();
        }
        Log.d(TAG, "setResponse: "+message);
        messages.add(message);
        messageAdapter.add(message);
    }
}
