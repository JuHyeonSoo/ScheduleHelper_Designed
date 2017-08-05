package com.sbc.sk.schedulehelper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-08-05.
 */

public class Boosted_http extends BroadcastReceiver{



    // 부팅이 완료되었다는 브로드캐스트 정보를 수신하면 서비스를 자동으로 실행
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("여기부터 전부","Boosted_http.java");
        //필요한 변수들 정의
        SharedPreferences sp = context.getSharedPreferences("sp",Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editor = sp.edit();

        boolean boosted_flag=false;    //true되면 AnalysisReply랑 Boosted_http 초기화 시킬 것
        int flag_count=0;               //부팅 시, boosted_flag=true, 그리고 그 다음 차례에 그 true를 전송해야함 (when flag_count==1)

        int intent_code=-10;            //초기 값 말도 안되게 설정
        String[] http_links = new String[30]; //최대 30개 받도록 설정

///////////////////////////////////////////////////////////////////////////////////////////////////
        //case1) 부팅하지 않고 Analysis 이용하는 경우//
        if((intent.getAction()==null)||((intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))==false)){

            Log.d("case1)저장된 flag가 true냐", Boolean.toString(sp.getBoolean("boosted_flag2",false)));

            if(sp.getBoolean("boosted_flag2",false)==true) { //이전 flag가 true 이면
                //flag_count가 1일 때 전송, 전송 후 SharedPreferences 초기화!!
                flag_count=flag_count+1;
                sp_editor.putInt("flag_count",flag_count);
                sp_editor.commit();
                Log.d("case1)if문,flag_count 1?",Double.toString((double)sp.getInt("flag_count",0)));

                if(sp.getInt("flag_count",0)>0){
                    //SharedPreferences 초기화..
                    Log.d("ㄱboosted_flag 지역",Boolean.toString(boosted_flag));
                    Log.d("ㄱboosted_flag2 전역", Boolean.toString(sp.getBoolean("boosted_flag2",false)));

                    Intent intent_boosted = new Intent(context, AnalysisReply.class);
                    intent_boosted.putExtra("boosted_flag",sp.getBoolean("boosted_flag2",false));

                    boosted_flag=false;
                    sp_editor.putBoolean("boosted_flag2",boosted_flag);
                    sp_editor.commit();
                    Log.d("ㄴboosted_flag 지역",Boolean.toString(boosted_flag));
                    Log.d("ㄴboosted_flag2 전역", Boolean.toString(sp.getBoolean("boosted_flag2",false)));

                }
            }

            intent_code = intent.getIntExtra("Boosted_http_code", -10);
            http_links[intent_code] = intent.getStringExtra("Boosted_links");

            Log.d("Boost code", Double.toString((double) intent_code));
            Log.d("주소 잘 받냐", http_links[intent_code]);

            sp_editor.putInt("intent_code1",intent_code);                 //총 count 계속 갱신해
            sp_editor.putString("http_links1"+intent_code ,http_links[intent_code]);
            sp_editor.commit();
        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////case2)
    if((intent.getAction()!=null)){
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Toast.makeText(context, "Scheudule Helper : 부팅 완료", Toast.LENGTH_SHORT).show();
            Log.d("Case2) Boost code는?",Double.toString((double) intent_code));

            boosted_flag=true;
            flag_count=0;
            Log.d("case2) flag_count는?",Double.toString((double)sp.getInt("flag_count",0)));
            sp_editor.putBoolean("boosted_flag2",boosted_flag); //부팅시 true 저장
            Log.d("Boost할때 flag가 true냐", Boolean.toString(boosted_flag));
            sp_editor.commit();

            //////////////여기서 전송 true임



////////////여기는 부팅시, noti!!
            if(sp.getInt("intent_code1",-10)!=-10) {
                for (int i = 0; i <= sp.getInt("intent_code1",-10); i++) {
                    String text = "Check this Web site periodically :) " + (i + 1) + "번쨰 입력한 사이트입니다.";

                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(context)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(text);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    // pending implicit intent to view url
                    Intent boostIntent = new Intent(Intent.ACTION_VIEW);
                    boostIntent.setData(Uri.parse(sp.getString("http_links1"+i," ")));
                    PendingIntent pending = PendingIntent.getActivity(context, i, boostIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notificationBuilder.setContentIntent(pending);
                    // using the same tag and Id causes the new notification to replace an existing one
                    mNotificationManager.notify(String.valueOf(System.currentTimeMillis()), i, notificationBuilder.build());
                }
                sp_editor.clear();
                sp_editor.commit();
                Toast.makeText(context, "Scheudule Helper : Web notification 기능 초기화", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Scheudule Helper : 중요한 사이트는 다시 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else{
            Toast.makeText(context, "Scheudule Helper : 에러찾으셈", Toast.LENGTH_SHORT).show();
            }
        }
        Log.d("여기까지 전부","Boosted_http.java");
    }
}
