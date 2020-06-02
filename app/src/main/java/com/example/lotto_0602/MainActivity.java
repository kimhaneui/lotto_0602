package com.example.lotto_0602;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.lotto_0602.databinding.ActivityMainBinding;

import java.util.Arrays;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    int[] winLottoNumber = new int[6];
    int bonusNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        binding.buyoneLottoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeLottoWinNumbers();
            }
        });
    }

    @Override
    public void setValues() {

    }
    void  makeLottoWinNumbers(){
        for (int i=0;i<winLottoNumber.length;i++){
            winLottoNumber[i]=0;
        }
        for(int i =0 ; i<winLottoNumber.length;i++){
            while (true){
                int randomNum = (int) (Math.random()*45+1);
                boolean isDuplicatedOk = true;
                for (int num:winLottoNumber){
                    if(num==randomNum){
                        isDuplicatedOk = false;
                        break;
                    }
                }
                if(isDuplicatedOk){
                    winLottoNumber[i]=randomNum;
                    break;
                }
            }
        }

        Arrays.sort(winLottoNumber);

        for (int winNum:winLottoNumber){
            Log.d("당첨번호",winNum+"");
        }
    }
}
