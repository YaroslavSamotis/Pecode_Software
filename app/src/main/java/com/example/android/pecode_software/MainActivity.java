package com.example.android.pecode_software;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyFragment.OnFragmentButtonsClickListener{

    private static final String BUNDLE_NUMBER_OF_CREATED_FRAGMENTS = "MainActivity.BUNDLE_NUMBER_OF_CREATED_FRAGMENTS";
    private static final String INTENT_NUMBER_OF_FRAGMENT = "INTENT_NUMBER_OF_FRAGMENT";
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private ViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;
    private ArrayList<MyFragment> mFragmentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentsList = new ArrayList<>();
        if (savedInstanceState == null) {
            mFragmentsList.add(MyFragment.getInstance("1"));
        } else {
            int counter = savedInstanceState.getInt(BUNDLE_NUMBER_OF_CREATED_FRAGMENTS);
            for (int i = 1; i <= counter; i++){
                mFragmentsList.add(MyFragment.getInstance(String.valueOf(i)));
            }
        }
        mViewPager = findViewById(R.id.view_pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragmentsList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mFragmentsList.size()-1);
        MyFragment myFragment = mFragmentsList.get(mViewPager.getCurrentItem());
        myFragment.setOnFragmentButtonClickListener(this);
    }

    @Override
    public void deleteFragment() {
        int position = mViewPager.getCurrentItem();
        if (position!=0) {
            mViewPager.setCurrentItem(position - 1, true);
            MyFragment myFragment = mFragmentsList.get(position-1);
            myFragment.setOnFragmentButtonClickListener(this);
            mFragmentsList.remove(position);
            mPagerAdapter.notifyDataSetChanged();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(position+1);
        }
    }

    @Override
    public void addFragment() {
        int count = mPagerAdapter.getCount();
        MyFragment myFragment = MyFragment.getInstance(String.valueOf(count+1));
        mFragmentsList.add(myFragment);
        myFragment.setOnFragmentButtonClickListener(this);
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(count, true);
    }

    @Override
    public void createNotification(int number) {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(INTENT_NUMBER_OF_FRAGMENT, number);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,0);
        createNotificationChannel();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("You create a notification")
                        .setContentText("Notification " + number)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(number, notification);
    }

    private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_NUMBER_OF_CREATED_FRAGMENTS, mFragmentsList.size());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        int numberOfFragment = intent.getIntExtra(INTENT_NUMBER_OF_FRAGMENT, -1);
        if (numberOfFragment != -1){
            mViewPager.setCurrentItem(numberOfFragment-1, true);
        }
        super.onNewIntent(intent);
    }
}
