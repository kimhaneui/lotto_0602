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

    boolean isAutoBuyRunning;
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

                if (!isAutoBuyRunning){
                    mHandler.post(buyLottoRunnable);
                    isAutoBuyRunning = true;
                    binding.buyautoLottoBtn.setText(getResources().getString(R.string.paused));
                }
                else{
                    mHandler.removeCallbacks(buyLottoRunnable);
                    isAutoBuyRunning = false;
                    binding.buyautoLottoBtn.setText(getResources().getString(R.string.resume));
                }
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
//        기존 당첨번호를 모두 0으로 세팅
        for (int i = 0; i < winLottoNumber.length; i++) {

            winLottoNumber[i] = 0;
        }
//        보너스 번호도 세팅

        bonusNum = 0;

//        당첨번호 6개를 뽑기위한  for
        for (int i = 0; i < winLottoNumber.length; i++) {

//            조건에 맞는 숫자를 뽑을때 까지 무한반복
            while (true) {

//                1~45까지 하나 랜덤
                int randomNum = (int) (Math.random() * 45 + 1);

//                중복 검사 결과 저장 변수 => 일단 맞다고 햇다가 수틀리면 false변경
                boolean isDuplicatedOk = true;

//                당첨번호중 같은게 있다면 false
//                한번도 같은게 없다면 true
                for (int num : winLottoNumber) {
                    if (num == randomNum) {
                        isDuplicatedOk = false;
                        break;
                    }
                }

//                중복검사가 통과되었다면
                if (isDuplicatedOk) {
                    winLottoNumber[i] = randomNum;
//                    무한반복 탈출 => 다음 당첨번호 뽑으러감 (for문)
                    break;
                }
            }
        }

//        6개를 다 돌고 다면 => 순서가  뒤죽박죽 이기때문에 오름차순 정렬(Arrays)
        Arrays.sort(winLottoNumber);

//        보너스 번호를 뽑는 무한반복 =>1개만
        while (true) {

//            랜덤 추출
            int randomNum = (int) (Math.random() * 45 + 1);
//  중복검사 진행
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

//        당첨번호들을 텍스트뷰에 표시
        for (int i = 0; i < winNumtxt.size(); i++) {
            int winNum = winLottoNumber[i];
//            화면에 있는 텍스트뷰를 ArrayList에 담아두고 활용
            winNumtxt.get(i).setText(winNum + "");
        }
//        보너스번호도 화면에 표시
        binding.bonusNum.setText(bonusNum + "");
    }
}
