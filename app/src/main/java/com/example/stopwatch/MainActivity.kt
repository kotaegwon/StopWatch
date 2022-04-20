package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var isRunning=false; //실행 여부 확인용 변수
    var timer: Timer? = null //timer 변수
    var time=0 //time 변수

    lateinit var btn_start: Button
    lateinit var btn_refresh: Button
    lateinit var tv_min: TextView
    lateinit var tv_sec: TextView
    lateinit var tv_milsec: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start=findViewById(R.id.btn_start);
        btn_refresh=findViewById(R.id.btn_refresh);
        tv_milsec=findViewById(R.id.tv_milsec);
        tv_min=findViewById(R.id.tv_min);
        tv_sec=findViewById(R.id.tv_sec);

        btn_start.setOnClickListener(this)
        btn_refresh.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_start->{
                if(isRunning){
                    pause()
                }else{
                    start()
                }
            }
            R.id.btn_refresh->{
                refresh()
            }
        }
    }
    //시작 메서드
    fun start(){
        //시작 버튼 클릭 시 텍스트 일시정지로 변경
        btn_start.text="일시정지"
        btn_start.setBackgroundColor(getColor(R.color.red))
        isRunning=true; //실행상태 변경

        //스톱워치를 시작하는 로직
        //timer(period=[주기]){} 함수는 일정한 주기로 반복하는 동작을 수행할 때 유용하게 쓰임
        timer=timer(period=10){
            //0.01초 마다 timer에 1을 더함
            time++ //10밀리초 단위 타이머

            //시간 계산
            val milsec = time%100
            val sec = (time%6000)/100
            val min = time/6000

            //runOnUiTread를 사용하여 UI작업이 백그라운드 스레드가 아닌
            //메인 스레드에서 일어남
            //isRunning이 true일 경우에만 UI가 업데이트
            runOnUiThread {
                if(isRunning){
                    //밀리초
                    tv_milsec.text=
                        if(milsec<10) ".0${milsec}" else ".${milsec}"

                    //초
                    tv_sec.text=
                        if(sec<10) ":0${sec}" else ":${sec}"

                    //분
                    tv_min.text="${min}"
                }
            };
        }
    }
    //정지 메서드
    fun pause(){
        btn_start.text="시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))

        isRunning=false;
        timer?.cancel()
    }
    //초기화 메서드
    fun refresh(){
        timer?.cancel()

        //멈춤상태로 전환
        btn_start.text="시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))
        isRunning=false;

        //타이머 초기화
        time=0
        tv_milsec.text=".00"
        tv_sec.text=":00"
        tv_min.text="00"
    }
}