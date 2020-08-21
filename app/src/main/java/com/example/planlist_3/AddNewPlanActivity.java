package com.example.planlist_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class AddNewPlanActivity extends AppCompatActivity {
    Context context;
    EditText addPlanName, addMemo;
    TextView myList, planName, date, selectedDate, time, selectedTime, alarm, memo, repeatDay, selectedRepeatDay;
    Button addDate, addTime, saveButton, cancelButton, addRepeat;
    ToggleButton addAlarm;
    //    ToggleButton complete;
    private DatePickerDialog.OnDateSetListener callbackMethodDate;
    private TimePickerDialog.OnTimeSetListener callbackMethodTime;
    DatabaseReference reference;
    Integer planNumber = new Random().nextInt(10000) + 1;
    //Integer planNumber=new Random().nextInt();// For unique number // planNumber=='id' of plans
    String key = Integer.toString(planNumber);
    String alarmTime;
    int alarmHour, alarmMinute, repeatYear, repeatMonth, repeatDayOfMonth, month;

    // 현재 시각 구하기
    Calendar calendar = Calendar.getInstance();
    int cYear = calendar.get(Calendar.YEAR);
    int cMonth = calendar.get(Calendar.MONTH) + 1;
    int cDate = calendar.get(Calendar.DATE);
    int cHour = calendar.get(Calendar.HOUR_OF_DAY);
    int cMinute = calendar.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_plan);

//        new AlarmMaking(getApplicationContext()).createNotification();

        // keyboard가 UI 가릴 때
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        myList = findViewById(R.id.MyList);
        planName = findViewById(R.id.planName);
        addPlanName = findViewById(R.id.AddPlanName);

        date = findViewById(R.id.date);
        selectedDate = findViewById(R.id.selectedDate);
        addDate = findViewById(R.id.addDate);

        time = findViewById(R.id.Time);
        selectedTime = findViewById(R.id.selectedTime);
        addTime = findViewById(R.id.addTime);

        alarm = findViewById(R.id.Alarm);
        addAlarm = findViewById(R.id.addAlarm);

        memo = findViewById(R.id.memo);
        addMemo = findViewById(R.id.AddMemo);

        saveButton = findViewById(R.id.SaveButton);
        cancelButton = findViewById(R.id.CancelButton);

        repeatDay = findViewById(R.id.RepeatDay);
        selectedRepeatDay = findViewById(R.id.selectedRepeatDay);
        addRepeat = findViewById(R.id.addRepeat);

        // Date 설정 관련 코드
        this.InitializeViewDate();
        this.InitializeListenerDate();
        // Time 설정 관련 코드
        this.InitializeViewTime();
        this.InitializeListenerTime();

        // 반복설정
        addRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] dayOfWeek = new String[]{"반복 없음", "매일", "1주일마다", "2주일마다", "매달", "매년"};
                final int[] selectItem = {0};

                AlertDialog.Builder dialog = new AlertDialog.Builder(AddNewPlanActivity.this);
                dialog.setTitle("반복되는 요일을 선택하세요.").setSingleChoiceItems(dayOfWeek, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectItem[0] = i;
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedRepeatDay.setText(dayOfWeek[selectItem[0]]);
                    }
                }).create().show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // insert data to FireBase
                reference = FirebaseDatabase.getInstance().getReference().child("planlist3").child("Plan" + planNumber);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("planName").setValue(addPlanName.getText().toString());
                        dataSnapshot.getRef().child("date").setValue(selectedDate.getText().toString());
                        dataSnapshot.getRef().child("time").setValue(selectedTime.getText().toString());
                        dataSnapshot.getRef().child("alarm").setValue(addAlarm.getText().toString());
                        dataSnapshot.getRef().child("memo").setValue(addMemo.getText().toString());
                        dataSnapshot.getRef().child("key").setValue(key);
                        //dataSnapshot.getRef().child("complete").setValue(complete.getText().toString());
                        dataSnapshot.getRef().child("repeatDay").setValue(selectedRepeatDay.getText().toString());

                        if (addPlanName.getText().toString().equals("") || selectedDate.getText().toString().equals("") || selectedTime.getText().toString().equals("") || selectedRepeatDay.getText().toString().equals("")) {
                            Toast.makeText(AddNewPlanActivity.this, "입력이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Intent intent = new Intent(AddNewPlanActivity.this, PlanListActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // Set the alarm to start at 8:30 a.m.
//                calendar.setTimeInMillis(System.currentTimeMillis());
//                calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
//                calendar.set(Calendar.MINUTE, alarmMinute+1);
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
                if(addAlarm.isChecked())
                {
//                    new AlarmMaking(getApplicationContext()).createNotification(); //
                    createNotification();
                }
                else
                {
                    removeNotification();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewPlanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    // 알림 create (Add Plan)
     private void createNotification() {

         NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
         Intent intent = new Intent(this, PlanListActivity.class);//
         PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); //
         builder.setSmallIcon(R.mipmap.ic_launcher);
         builder.setContentTitle(addPlanName.getText());
         builder.setContentText(selectedDate.getText() + "\t\t" + selectedTime.getText() +"에 일정이 추가되었습니다!");
         builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE); //
         builder.setWhen(System.currentTimeMillis());
         builder.setContentIntent(pendingIntent);//
         builder.setPriority(Notification.PRIORITY_MAX); // 우선순위

         builder.setColor(Color.BLACK);
         // 사용자가 탭을 클릭하면 자동 제거
         builder.setAutoCancel(true);

         // 알림 표시
         NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
         }

         AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
         pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

//        //  Set the alarm to start at 8:30 a.m.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 8);
//        calendar.set(Calendar.MINUTE, 30);


         // setRepeating() lets you specify a precise custom interval--in this case,
         // 20 minutes.
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                 1000 * 60 * 20, pendingIntent);

         // id값은
         // 정의해야하는 각 알림의 고유한 int값
         notificationManager.notify(1, builder.build());
     }


    // 알림 삭제
    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(1);
    }

    // 사용자가 지정한 날짜 정보를 TextView 에 표시
    public void InitializeViewDate() {
        selectedDate = (TextView) findViewById(R.id.selectedDate);
    }

    // OnDateSetListener 구현 함수
    // onDateSet() 함수 오버라이딩하여 다이얼로그 창의 완료 버튼 클릭하면 실행되는 CallBack 함수
    public void InitializeListenerDate() {
        callbackMethodDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate.setText(year + " / " + monthOfYear + " / " + dayOfMonth);
                repeatYear = year;
                repeatMonth = monthOfYear;
                repeatDayOfMonth = dayOfMonth;
                month = monthOfYear;
            }
        };

        // 알림 설정 날짜 (추가 부분)
        this.calendar.set(Calendar.YEAR, repeatYear);
        switch (repeatMonth){
            case 1:
                this.calendar.set(Calendar.MONTH, Calendar.JANUARY);
                break;
            case 2:
                this.calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
                break;
            case 3:
                this.calendar.set(Calendar.MONTH, Calendar.MARCH);
                break;
            case 4:
                this.calendar.set(Calendar.MONTH, Calendar.APRIL);
                break;
            case 5:
                this.calendar.set(Calendar.MONTH, Calendar.MAY);
                break;
            case 6:
                this.calendar.set(Calendar.MONTH, Calendar.JUNE);
                break;
            case 7:
                this.calendar.set(Calendar.MONTH, Calendar.JULY);
                break;
            case 8:
                this.calendar.set(Calendar.MONTH, Calendar.AUGUST);
                break;
            case 9:
                this.calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
                break;
            case 10:
                this.calendar.set(Calendar.MONTH, Calendar.OCTOBER);
                break;
            case 11:
                this.calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
                break;
            case 12:
                this.calendar.set(Calendar.MONTH, Calendar.DECEMBER);
                break;
        }

        calendar.set(Calendar.DATE, repeatDayOfMonth);
    }

    public void OnClickHandlerDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethodDate, cYear, cMonth, cDate);
        dialog.show();
    }

    // 사용자가 지정한 시간 정보를 TextView 에 표시
    public void InitializeViewTime() {
        selectedTime = (TextView) findViewById(R.id.selectedTime);
    }

    // OnDateSetListener 구현 함수
    // onDateSet() 함수 오버라이딩하여 다이얼로그 창의 완료 버튼 클릭하면 실행되는 CallBack 함수
    public void InitializeListenerTime() {
        callbackMethodTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                selectedTime.setText(hour + "시 " + minute + "분");
                alarmTime = hour + "시" + minute + "분";
                alarmHour = hour;
                alarmMinute = minute;
            }
        };

        // 알람 시간 설정(추가 부분)
        this.calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        this.calendar.set(Calendar.MINUTE, alarmMinute);
        this.calendar.set(Calendar.SECOND, 5);
        this.calendar.set(Calendar.MILLISECOND, 0);
    }

    public void OnClickHandlerTime(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, callbackMethodTime, cHour, cMinute, true);
        dialog.show();
    }

    // 추가 부분
//    public void setAlarm() {
//        // 현재보다 이전이면 등록 실패
//        if(this.calendar.before(Calendar.getInstance()))
//        {
//            Toast.makeText(this, "알람시간이 현재시간보다 전일 수 없습니다.", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        // Receiver 설정
//        Intent intent=new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent=PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // 알람 설정
//        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(), pendingIntent);
//
//        // Toast로 알람 시간 보여주기
//        SimpleDateFormat format=new SimpleDateFormat("yyyy/mm/dd HH:mm:ss", Locale.getDefault());
//        Toast.makeText(this, "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_LONG).show();

//        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.putExtra("no", alarmIdx);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmIdx, intent, 0);
//
//        long aTime = System.currentTimeMillis();
//        long bTime = calendar.getTimeInMillis();
//
//        //하루의 시간을 나타냄
//        long interval = 1000 * 60 * 60  * 24;
//
//        //만일 내가 설정한 시간이 현재 시간보다 작다면 알람이 바로 울려버리기 때문에 이미 시간이 지난 알람은 다음날 울려야 한다.
//        while(aTime>bTime){
//            bTime += interval;
//        }
//
//        //알람 매니저를 통한 반복알람 설정
//        alarmManager.setRepeating(AlarmManager.RTC, bTime, interval, pendingIntent);
//    }

//    public void cancelAlarm(){
//
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(context, PlanListActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmIdx, intent, 0);
//        alarmManager.cancel(pendingIntent);
//
//    }
}