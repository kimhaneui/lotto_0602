package com.example.lotto_0602;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lotto_0602.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    int[] winLottoNumber = new int[6];
    int bonusNum = 0;

    List<TextView> winNumtxt = new ArrayList<>();
    List<TextView> myNumtxts = new ArrayList<>();
    long useMoney = 0L;
    long winMoney = 0L;

    int firstRankCount = 0;
    int secondRankCount = 0;
    int thirdRankCount = 0;
    int fourthRankCount = 0;
    int fifthRankCount = 0;
    int unrankedRankCount = 0;

    List<TextView> myNumTxts = new ArrayList<>();

    Handler mHandler =  new Handler();
    Runnable buyLottoRunnable = new Runnable() {

        @Override
        public void run() {
            if (useMoney<1000000){
                makeLottoWinNumbers();
                checkWinRank();
                mHandler.post(buyLottoRunnable);
            }
            else{
                Toast.makeText(mContext,"로또 구매를 종료합니다.",Toast.LENGTH_SHORT).show();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        binding.buyautoLottoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mHandler.post(buyLottoRunnable);
            }
        });

        binding.buyoneLottoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeLottoWinNumbers();
                checkWinRank();
            }
        });
    }

    @Override
    public void setValues() {
        winNumtxt.add(binding.winNum01);
        winNumtxt.add(binding.winNum02);
        winNumtxt.add(binding.winNum03);
        winNumtxt.add(binding.winNum04);
        winNumtxt.add(binding.winNum05);
        winNumtxt.add(binding.winNum06);

        myNumtxts.add(binding.myNum01);
        myNumtxts.add(binding.myNum02);
        myNumtxts.add(binding.myNum03);
        myNumtxts.add(binding.myNum04);
        myNumtxts.add(binding.myNum05);
        myNumtxts.add(binding.myNum06);

    }

    void checkWinRank() {
        useMoney += 1000;

        binding.useMoneyTxt.setText(String.format("%,d원", useMoney));

        int correctCount = 0;

        for (TextView myNumtxt : myNumtxts) {
            int myNum = Integer.parseInt(myNumtxt.getText().toString());

            for (int winNum : winLottoNumber) {
                if (myNum == winNum) {
                    correctCount++;
                }
            }
        }
        if (correctCount == 6) {
            winMoney += 1300000000;
            firstRankCount++;
        } else if (correctCount == 5) {
            boolean isBonusNumCorrect = false;
            for (TextView myNumTxt : myNumtxts) {
                int myNum = Integer.parseInt(myNumTxt.getText().toString());
                if (myNum == bonusNum) {
                    isBonusNumCorrect = true;
                    break;
                }
            }

            if (isBonusNumCorrect) {
                winMoney += 54000000;
                secondRankCount++;
            } else {
                winMoney += 1450000;
                thirdRankCount++;
            }

        } else if (correctCount == 4) {
            winMoney += 50000;
            fourthRankCount++;
        } else if (correctCount == 3) {
            winMoney += 5000;
            fifthRankCount++;
        } else {
            unrankedRankCount++;
        }
        binding.winMoneyTxt.setText(String.format("%,d원", winMoney));
        binding.useMoneyTxt.setText(String.format("%,d원", useMoney));

        binding.firstRank.setText(String.format("%,d회",firstRankCount));
        binding.secondRank.setText(String.format("%,d회",secondRankCount));
        binding.thirdRank.setText(String.format("%,d회",thirdRankCount));
        binding.fourthRank.setText(String.format("%,d회",fourthRankCount));
        binding.fifthRank.setText(String.format("%,d회",fifthRankCount));
        binding.unranked.setText(String.format("%,d회",unrankedRankCount));
    }


    void makeLottoWinNumbers() {
        for (int i = 0; i < winLottoNumber.length; i++) {

            winLottoNumber[i] = 0;
        }
        bonusNum = 0;

        for (int i = 0; i < winLottoNumber.length; i++) {
            while (true) {
                int randomNum = (int) (Math.random() * 45 + 1);
                boolean isDuplicatedOk = true;
                for (int num : winLottoNumber) {
                    if (num == randomNum) {
                        isDuplicatedOk = false;
                        break;
                    }
                }
                if (isDuplicatedOk) {
                    winLottoNumber[i] = randomNum;
                    break;
                }
            }
        }

        Arrays.sort(winLottoNumber);

        while (true) {
            int randomNum = (int) (Math.random() * 45 + 1);

            boolean isDuplicatedOk = true;

            for (int num : winLottoNumber) {
                if (num == randomNum) {
                    isDuplicatedOk = false;
                    break;
                }
            }
            if (isDuplicatedOk) {
                bonusNum = randomNum;
                break;
            }
        }

        for (int i = 0; i < winNumtxt.size(); i++) {
            int winNum = winLottoNumber[i];
            winNumtxt.get(i).setText(winNum + "");
        }
        binding.bonusNum.setText(bonusNum + "");
    }
}
