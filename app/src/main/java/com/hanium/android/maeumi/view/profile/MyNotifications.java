package com.hanium.android.maeumi.view.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import com.hanium.android.maeumi.R;
import com.hanium.android.maeumi.adapters.NotificationAdapter;
import com.hanium.android.maeumi.model.Notification;

import java.util.ArrayList;

public class MyNotifications extends AppCompatActivity {

    ListView notifyListView;
    private ArrayList<Notification> notifyList = new ArrayList<Notification>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notifyListView = findViewById(R.id.notificationListView);

        //어댑터를 이용해서 리스트뷰에 데이터 넘김
        NotificationAdapter adapter = new NotificationAdapter(this, this);
        adapter.setItems(notifyList);
        notifyListView.setAdapter(adapter);   //어댑터 등록
    }

    public void goToBack(View view) {
        finish();
    }
}
