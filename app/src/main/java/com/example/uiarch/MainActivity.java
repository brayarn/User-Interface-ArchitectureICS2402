package com.example.uiarch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    protected static  final int RESULT_SPEECH = 1;
    private ImageButton speakButton;
    private TextView wordsTextView;
    /*This is the code that includes the text to speech implementation of our application
    This is the object that fulfills one of our requirements- The app tells the user "Here is the green/red screen
     */
    private TextToSpeech mTTs;
    private static  String welcometothegreencreen = "Here is the green screen";
    private  static  String welcometotheredscreen = "Here is the red screen";
    /*private TextToSpeech mTTS = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS){
               int result = mTTS.setLanguage(Locale.ENGLISH);
               if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                   Log.e("TTS", "The Language selected is not supported");
               }
            }else{
                Log.e("TTS","Initialization of the TTS object has failed" );
            }
        }
    });
    public void speak(int screencolor){
        if (screencolor == 1 ){
            mTTS.speak(welcometothegreencreen, TextToSpeech.QUEUE_FLUSH, null);
        }else {
            mTTS.speak(welcometotheredscreen, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    //we need to stop this speak engine once the app is closed

    //private static final String welcometothegreencreen = "Here is the green screen";
    //private static final String welcometotheredscreen = "Here is the red screen";

    //The implementation ends here
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordsTextView = (TextView) findViewById(R.id.wordsTextView);
        speakButton = (ImageButton) findViewById(R.id.speakButton);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                try{
                    startActivityForResult(intent, RESULT_SPEECH);
                    wordsTextView.setText("");
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(), "Your device does not support speech to text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
       //I am intializing the TextTospeech Object here
        mTTs = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = mTTs.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS", "The Language selected is not supported");
                    }
                }else{
                    Log.e("TTS","Initialization of the TTS object has failed" );
                }
            }
        });

    }
    public void speak(int screencolor){
        if (screencolor == 1 ){
            mTTs.speak(welcometothegreencreen, TextToSpeech.QUEUE_FLUSH, null);
        }else {
            mTTs.speak(welcometotheredscreen, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    wordsTextView.setText(text.get(0));
                    /* I am Inserting personal code from this point onwards, it specifies how  we fetch the verbal input
                    from the user and spitting the outcome to the added intents(Red and Green Screens)
                     */if ((text.get(0)).equals("green") || (text.get(0)).equals("Green")){
                         //Trying to use the TextToSpeech object everytime the activity is started
                         speak(1);
                         Intent gotogreenScreenIntent = new Intent("com.example.uiarch.greenscreen");
                         startActivity(gotogreenScreenIntent);
                    }else if ((text.get(0)).equals("red") || (text.get(0)).equals("Red")){
                         speak(2);
                         Intent gotoredScreenIntent = new Intent("com.example.uiarch.redscreen");
                         startActivity(gotoredScreenIntent);
                    }else {
                         Toast.makeText(getApplicationContext(),"Please try Red/red or Green/green keywords", Toast.LENGTH_SHORT).show();
                    }

                    //The activity activation code ends here
                }
                break;
        }
    }
/* This is the code am adding to handle the TextToSpeech initialization object once the app closes
I thought to do it by overriding the onDestroy(When you close an activity)  method
 */
    @Override
    protected void onDestroy() {
        if (mTTs != null){
            mTTs.stop();
            mTTs.shutdown();
        }
        super.onDestroy();

    }
}