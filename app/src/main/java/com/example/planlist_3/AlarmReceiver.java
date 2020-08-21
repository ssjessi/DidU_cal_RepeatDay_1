package com.example.planlist_3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    String INTENT_ACTION=Intent.ACTION_BOOT_COMPLETED;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    // 알람 시간 되면 onReceive 호출
    public void onReceive(Context context, Intent intent)
    {
        // 추가 부분
        Intent intent1=new Intent(context, AlarmService.class);

        // Oreo(26)버전 이후부터 Background 실행 금지 > Foreground에서 실행
        context.startService(intent1);




//        final int no = intent.getIntExtra("no", -1);
//        PlanItemData planItemData = new PlanItemData();
//
//        if(no==-1)
//        {
//            Log.i("Error", "AlarmReceiver intent error");
//            return;
//        }
//
//        final String[] week={"일", "월", "화", "수", "목", "금", "토"};


//        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification.Builder builder=new Notification.Builder(context);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentTitle("addPlanName");
//        builder.setContentText("selectedDate\t\t + selectedTime 에 완료되지 않은 일정이 있습니다. 확인하세요!");
//        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE); //
//        builder.setWhen(System.currentTimeMillis());
//        builder.setNumber(1);
//        builder.setContentIntent(pendingIntent);//
//        builder.setPriority(Notification.PRIORITY_MAX); // 우선순위
//        builder.setColor(Color.RED);
//        // 사용자가 탭을 클릭하면 자동 제거
//        builder.setAutoCancel(true);
//
//        notificationManager.notify(1, builder.build());
    }
//    public AlarmReceiver() {
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Intent intent1=new Intent(context, AddNewPlanActivity.class);
//        context.startActivity(intent1);
//    }
}
