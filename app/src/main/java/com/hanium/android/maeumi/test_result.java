<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context=".test_result" >

<Button
        android:id="@+id/button9"
                android:layout_width="match_parent"
                android:layout_height="58dp"
                android:text="설문조사 결과"
                android:textColor="#000000"
                app:backgroundTint="#D3D0CC"
                tools:textSize="18sp" />

<LinearLayout
        android:layout_width="match_parent"
                android:layout_height="78dp"
                android:orientation="horizontal">

<ImageView
            android:id="@+id/imageView3"
                    android:layout_width="158dp"
                    android:layout_height="71dp"
                    android:layout_weight="1"
                    android:background="#F40404"
                    app:srcCompat="@drawable/ic_launcher_foreground" />

<TextView
            android:id="@+id/textView5"
                    android:layout_width="247dp"
                    android:layout_height="74dp"
                    android:text="가해 정도"
                    android:textColor="#070707"
                    android:textSize="24sp" />

</LinearLayout>

<SeekBar
        android:id="@+id/seekBar"
                android:layout_width="match_parent"
                android:layout_height="60dp" />

<LinearLayout
        android:layout_width="match_parent"
                android:layout_height="82dp"
                android:orientation="horizontal">

<ImageView
            android:id="@+id/imageView2"
                    android:layout_width="169dp"
                    android:layout_height="match_parent"
                    android:background="#FFC107"
                    app:srcCompat="@drawable/ic_launcher_foreground" />

<TextView
            android:id="@+id/textView6"
                    android:layout_width="247dp"
                    android:layout_height="74dp"
                    android:layout_weight="1"
                    android:text="피해 정도"
                    android:textColor="#070707"
                    android:textSize="24sp" />

</LinearLayout>

<SeekBar
        android:id="@+id/seekBar3"
                android:layout_width="match_parent"
                android:layout_height="60dp" />

<Button
        android:id="@+id/button14"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="테스트 결과 설명 더보기"
                android:textColor="#000000"
                app:backgroundTint="#9E9E9E"
                tools:textSize="18sp" />

<Button
        android:id="@+id/button15"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="메인화면으로 가기"
                android:textColor="#000000"
                app:backgroundTint="#D3D0CC"
                tools:textSize="18sp" />

<Button
        android:id="@+id/button16"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="결과 전송하기"
                android:textColor="#000000"
                app:backgroundTint="#D3D0CC"
                tools:textSize="18sp" />

<Button
        android:id="@+id/button17"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="게시글 작성하러 가기"
                android:textColor="#000000"
                app:backgroundTint="#D3D0CC"
                tools:textSize="18sp" />

<Button
        android:id="@+id/button18"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="상담하러 가기"
                android:textColor="#000000"
                app:backgroundTint="#D3D0CC"
                tools:textSize="18sp" />

</LinearLayout>