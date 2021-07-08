package com.hanium.android.maeumi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hanium.android.maeumi.helpers.SendMessageInBg;
import com.hanium.android.maeumi.interfaces.BotReply;
import com.hanium.android.maeumi.view.board.Board;
import com.hanium.android.maeumi.view.chatbot.ChatBot;
import com.hanium.android.maeumi.view.diary.DiaryMain;
import com.hanium.android.maeumi.view.profile.Profile;
import com.hanium.android.maeumi.view.selftest.SelfTest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToDiary(View view) {
        Intent intent = new Intent(MainActivity.this, DiaryMain.class);
        startActivity(intent);
    }

    public void goToSelfTest(View view) {
        Intent intent = new Intent(MainActivity.this, SelfTest.class);
        startActivity(intent);
    }

    public void goToChatBot(View view) {
        Intent intent = new Intent(MainActivity.this, ChatBot.class);
        startActivity(intent);
    }

    public void goToBoard(View view) {
        Intent intent = new Intent(MainActivity.this, Board.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        startActivity(intent);
    }

    //catbot 영상 참고 소스
    public void implements BotReply {

        RecyclerView chatView;
        ChatAdapter chatAdapter;
        List<Message> messageList = new ArrayList<>();
        EditText editMessage;
        ImageButton btnSend;

        //dialogFlow
        private SessionsClient sessionsClient;
        private SessionName sessionName;
        private String uuid = UUID.randomUUID().toString();
        private String TAG = "mainactivity";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            chatView = findViewById(R.id.chatView);
            editMessage = findViewById(R.id.editMessage);
            btnSend = findViewById(R.id.btnSend);

            chatAdapter = new ChatAdapter(messageList, this);
            chatView.setAdapter(chatAdapter);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    String message = editMessage.getText().toString();
                    if (!message.isEmpty()) {
                        messageList.add(new Message(message, false));
                        editMessage.setText("");
                        sendMessageToBot(message);
                        Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                        Objects.requireNonNull(chatView.getLayoutManager())
                                .scrollToPosition(messageList.size() - 1);
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            setUpBot();
        }

        private void setUpBot() {
            try {
                InputStream stream = this.getResources().openRawResource(R.raw.credential);
                GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
                String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

                SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
                SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                        FixedCredentialsProvider.create(credentials)).build();
                sessionsClient = SessionsClient.create(sessionsSettings);
                sessionName = SessionName.of(projectId, uuid);

                Log.d(TAG, "projectId : " + projectId);
            } catch (Exception e) {
                Log.d(TAG, "setUpBot: " + e.getMessage());
            }
        }

        private void sendMessageToBot(String message) {
            QueryInput input = QueryInput.newBuilder()
                    .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
            new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
        }

        @Override
        public void callback(DetectIntentResponse returnResponse) {
            if(returnResponse!=null) {
                String botReply = returnResponse.getQueryResult().getFulfillmentText();
                if(!botReply.isEmpty()){
                    messageList.add(new Message(botReply, true));
                    chatAdapter.notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
                }else {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}