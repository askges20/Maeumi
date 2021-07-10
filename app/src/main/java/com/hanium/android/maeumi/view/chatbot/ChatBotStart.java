package com.hanium.android.maeumi.view.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.adapters.ChatAdapter;
import com.hanium.android.maeumi.helpers.SendMessageInBg;
import com.hanium.android.maeumi.interfaces.BotReply;
import com.hanium.android.maeumi.model.Message;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatBotStart extends AppCompatActivity implements BotReply {

    RecyclerView chatView;
    ChatAdapter chatAdapter;
    List<Message> messageList = new ArrayList<>();  //채팅 내역
    EditText editMessage;
    ImageButton btnSend;

    //dialogFlow
    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private String uuid = UUID.randomUUID().toString();
    private String TAG = "ChatBotStart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_start);

        chatView = findViewById(R.id.chatView);
        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        chatAdapter = new ChatAdapter(messageList, this);
        chatView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() { //전송 버튼 리스너
            @Override
            public void onClick(View view) {
                String message = editMessage.getText().toString();  //입력된 문장
                if (!message.isEmpty()) {
                    messageList.add(new Message(message, false));   //채팅 내역에 추가
                    editMessage.setText("");    //채팅 입력칸 비우기
                    sendMessageToBot(message);  //채팅 전송
                    Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
                } else {    //입력 문장이 없으면
                    Toast.makeText(ChatBotStart.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpBot(); //Dialogflow Bot 세팅
    }

    private void setUpBot() {
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.credential); //Dialogflow 정보 JSON 파일
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

    private void sendMessageToBot(String message) { //Dialogflow Bot에 채팅 전송
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("ko-KR")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();    //helpers의 SendMessageInBg.java
    }

    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if (returnResponse != null) {
            String botReply = returnResponse.getQueryResult().getFulfillmentText();
            if (!botReply.isEmpty()) {
                messageList.add(new Message(botReply, true));
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    public void goToDiary(View view){
        Intent intent = new Intent(ChatBotStart.this, DiaryMain.class);
        startActivity(intent);
        System.out.println("Move To Diary");
    }
    public void goToBoardAnonymous(View view){
        Intent intent = new Intent(ChatBotStart.this, Board.class);
        startActivity(intent);
        System.out.println("Move To Board");
        //익명게시판으로 이동하도록 코드 추가 필요
    }
     */
}