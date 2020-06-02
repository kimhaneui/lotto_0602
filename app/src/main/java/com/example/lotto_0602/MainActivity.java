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

//    구매로직코드도 여러곳에서 사용하려고 멤버변수 사용
    Runnable buyLottoRunnable = new Runnable() {

        @Override
        public void run() {
//            사용한 금액이 1천만원 이하라면
            if (useMoney<1000000){

//                로또 번호생성/등수 맞추기 진행
                makeLottoWinNumbers();
                checkWinRank();

//                이행동을 다시 할일로 등록해달라 => 반복으로 동작하게 되는 이유
                mHandler.post(buyLottoRunnable);
            }
            else{
//                돈을 다썻으면 구매종료 안내
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
//      자동구매를 누르면
        binding.buyautoLottoBtn.setOnClickListener(new View.OnClickListener() {

//            지금 구매를 안돌리고 있다면
            @Override
            public void onClick(View v) {
//                  구매시작 코드를 할일로 등록 시키자
                if (!isAutoBuyRunning){
                    mHandler.post(buyLottoRunnable);
                    isAutoBuyRunning = true;
//                    버튼 문구도 중단하기로 변경
                    binding.buyautoLottoBtn.setText(getResources().getString(R.string.paused));
                }
//                지금 구매가 돌아가고 있다면
                else{
                    mHandler.removeCallbacks(buyLottoRunnable);
//                    지금 구매중이 아니라고 명시
                    isAutoBuyRunning = false;
//                    버튼 문구 변경
                    binding.buyautoLottoBtn.setText(getResources().getString(R.string.resume));
                }
            }
        });
//      한장을 구매할땐 => 로또 번호 만들고  / 등수 확인
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

//       증가된 사용금액을 화면에 반영
        binding.useMoneyTxt.setText(String.format("%,d원", useMoney));

//        모든 갯수 저장변수
        int correctCount = 0;

//        내입력번호가 적힌  텍스트뷰들을 꺼내봄
        for (TextView myNumtxt : myNumtxts) {

//            적혀있는 숫자를 인트로 변경
            int myNum = Integer.parseInt(myNumtxt.getText().toString());

//            내숫자를 들고 당첨번호를 돌면서 증가
            for (int winNum : winLottoNumber) {
                if (myNum == winNum) {
                    correctCount++;
                }
            }
        }
//        맞춘 갯수에 따른 등수
        if (correctCount == 6) {
            winMoney += 1300000000;
            firstRankCount++;
        } else if (correctCount == 5) {

//            5개를 맞췄을땐 보너스 번호도 확인
            boolean isBonusNumCorrect = false;

//            내입력번호 텍스트뷰목록을 돌면서 확인
            for (TextView myNumTxt : myNumtxts) {
                int myNum = Integer.parseInt(myNumTxt.getText().toString());

//                보너스번호와 비교해서 처리
                if (myNum == bonusNum) {
                    isBonusNumCorrect = true;
                    break;
                }
            }
//          보너스 맟추면  처리
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
