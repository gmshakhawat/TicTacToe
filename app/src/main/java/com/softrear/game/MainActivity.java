package com.softrear.game;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    enum Player{

           ONE,TWO,NO
    }

    private Button playAgain,singleMode,multiMode,changeMode ;
    private Button Sreset;
    private  boolean gaemOver = false;
    private GridLayout gridLout;
    private TextView WinerMsg,mcount,scount;
    Player currentPlayer=Player.ONE;
    private int Mcount=0,Scount=0,cc=0;
    private Boolean isSingleMode;

    private LinearLayout modeSelection;


    public static String ocount = "0";
    public static String xcount = "0";
    String Xvalue,Ovalue ;

    public int [] board=new int[9];

    Player [] playerChoice= new Player[9];

    int[][] winer = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};


    Integer[] movePosition = {

            R.id.img1, R.id.img2,R.id.img3 ,
            R.id.img4, R.id.img5 , R.id.img6,
            R.id.img7,R.id.img8,R.id.img9
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playAgain=findViewById(R.id.btnreset);
        gridLout=findViewById(R.id.gridLayout);
        WinerMsg=findViewById(R.id.winerMsg);
        mcount=findViewById(R.id.mcount);
        scount=findViewById(R.id.scount);
        Sreset=findViewById(R.id.sreset);
        modeSelection=findViewById(R.id.playingMode);
        singleMode=findViewById(R.id.singlePlayer);
        multiMode=findViewById(R.id.multiPlayer);
        changeMode=findViewById(R.id.changeMode);

        changeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelection.setVisibility(View.VISIBLE);
            }
        });

        singleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSingleMode=true;
                modeSelection.setVisibility(View.GONE);
                playAgainGame();

            }
        });

        multiMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSingleMode=false;
                modeSelection.setVisibility(View.GONE);
                playAgainGame();
            }
        });


        boardInit();

        for(int i=0;i<playerChoice.length;i++)
        {
            playerChoice[i]=Player.NO;
        }
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playAgainGame();
            }
        });



        SharedPreferences Oprefs = getSharedPreferences(ocount,MODE_PRIVATE);
        Ovalue= Oprefs.getString("Ovalue","0");
        SharedPreferences Xprefs = getSharedPreferences(ocount,MODE_PRIVATE);
        Xvalue= Xprefs.getString("Xvalue","0");

        mcount.setText(""+Xvalue);
        Mcount=Integer.parseInt(Xvalue.toString());
        scount.setText(""+Ovalue);
        Scount=Integer.parseInt(Xvalue.toString());


        Sreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetScore();

            }
        });



    }

    public void resetScore(){
        Log.i("Ovalue"," "+Ovalue);
        Xvalue = "0";
        Ovalue ="0";
        mcount.setText(""+Xvalue);
        Mcount=Integer.parseInt(Xvalue.toString());
        scount.setText(""+Ovalue);
        Scount=Integer.parseInt(Xvalue.toString());

        SharedPreferences.Editor editO = getSharedPreferences(ocount ,MODE_PRIVATE).edit();
        editO.putString("Xvalue","0");

        SharedPreferences.Editor editX = getSharedPreferences(xcount ,MODE_PRIVATE).edit();
        editO.putString("Ovalue","0");

        editO.commit();
        editX.commit();
        playAgainGame();

    }
    public int botPlayer(int botNumber){

        int oppNumber;
        if(botNumber==1)
        {
            oppNumber=2;
        }
        else {
            oppNumber=1;
        }


        int botWiniingChance=getPerfectPosition(botNumber);

        Log.i("playerBotWiningChance :"," "+botWiniingChance);

        if( botWiniingChance>0)
        {
            return botWiniingChance;
        }
        else {

            int playerWiningChance=getPerfectPosition(oppNumber);
            Log.i("playerWiningChance:"," "+playerWiningChance);
            if(playerWiningChance>0)
            {
                return playerWiningChance;
            }
            else {
                if(botWiniingChance>playerWiningChance)
                {
                    return -botWiniingChance;
                }
                else {
                    return -playerWiningChance;
                }

            }

        }

    }

    public void boardInit()
    {
        for(int i=0;i<9;i++){
            board[i]=0;
        }
    }

    public int getPerfectPosition(int playerNumber){
        int checkWinPercentage=0;
        int nextMove=-99;
        int possibleNextMove=-99;
        int noMatchNextMove=-99;
        int check=0;
        Boolean nm;
        for(int i=0;i<8;i++)
        {
            nm=false;
            checkWinPercentage=0;
            for(int j=0;j<3;j++)
            {
                Log.i("playerInWinerList :"," "+winer[i][j]);
                if(board[winer[i][j]]==playerNumber)
                {
                    checkWinPercentage++;
                }
                else if(board[winer[i][j]]==0)
                {
                    nm=true;
                    nextMove=winer[i][j];
                }
            }


            if(checkWinPercentage>1 && nm)
            {
                Log.i("playerWiningPercentage "," "+checkWinPercentage);
                return nextMove;
            }
            else if(checkWinPercentage==1 && nm)
            {
                Log.i("playerWiningPercentage "," "+checkWinPercentage);
                check++;
                possibleNextMove=-nextMove;
            }
            else if(nm){
                Log.i("playerWiningPercentage "," "+checkWinPercentage);
                noMatchNextMove=-noMatchNextMove;
            }

        }

        if(check>0){
            return possibleNextMove;
        }
        else{
            return noMatchNextMove;
        }

    }


    public void isCliked(View viewImg){
        ImageView cliked= (ImageView) viewImg;

        int pcIMG=Integer.parseInt(cliked.getTag().toString());
        if(playerChoice[pcIMG]==Player.NO && gaemOver==false ) {
            cc++;
            cliked.setTranslationX(-2000);
            cliked.animate().translationXBy(2000).alpha(1).rotation(3600).setDuration(2000);
            Log.i("playerPos :"," "+pcIMG);

            playerChoice[pcIMG] = currentPlayer;

            if (currentPlayer == Player.ONE) {
                cliked.setImageResource(R.drawable.o);
                currentPlayer = Player.TWO;
                board[pcIMG]=1;


                if(isSingleMode && cc<5)
                {
                    int pos=botPlayer(2);
                    Log.i("playerWiningPos :"," "+pos);
                    playerChoice[pos] = currentPlayer;
                    ImageView view=(ImageView) findViewById(movePosition[pos]);
                    view.setImageResource(R.drawable.x);
                    view.setTranslationX(-2000);
                    view.animate().translationXBy(2000).alpha(1).rotation(3600).setDuration(2000);
                    board[pos]=2;
                    currentPlayer = Player.ONE;
                }



            } else if (currentPlayer == Player.TWO) {
                cliked.setImageResource(R.drawable.x);
                board[pcIMG]=2;
                currentPlayer = Player.ONE;

            }

            Log.i("playerBoard :","\t board"+"\n"+board[0]+"\t"+board[1]+"\t"+board[2]+"\n"+board[3]+"\t"+board[4]+"\t"+board[5]+"\n"+board[6]+"\t"+board[7]+"\t"+board[8]);

            cliked.animate().translationXBy(2000).alpha(1).rotation(3600).setDuration(1000);

            //        Toast.makeText(this,cliked.getTag().toString(),Toast.LENGTH_SHORT).show();


            String msg="";
            for (int[] winnerCol : winer) {

                if (playerChoice[winnerCol[0]] == playerChoice[winnerCol[1]] && playerChoice[winnerCol[1]] == playerChoice[winnerCol[2]] && playerChoice[winnerCol[0]] != Player.NO) {
                    playAgain.setVisibility(View.VISIBLE);
                    WinerMsg.setVisibility(View.VISIBLE);
                    gaemOver=true;

                    cc=0;
                    if (currentPlayer == Player.ONE) {
                        msg=" X ";
                        Mcount++;

                    } else if (currentPlayer == Player.TWO) {
                        msg=" O ";
                        Scount++;
                    }

                    WinerMsg.setText(msg+" is Winnner ! ");
                   // WinerMsg.animate().translationXBy(2000).alpha(0.4f).rotation(360).setDuration(100000);

                }
                else if( (cc>=9)||(isSingleMode && cc>=4)){

                    playAgain.setVisibility(View.VISIBLE);
                    WinerMsg.setVisibility(View.VISIBLE);
                    WinerMsg.setText("Match Draw ! ");
                    //WinerMsg.animate().translationXBy(2000).alpha(0.4f).rotation(360).setDuration(100000);
                    gaemOver=true;

                    cc=0;

                }


            }

            mcount.setText(""+Mcount);
            scount.setText(""+Scount);


        }
//        else
//        {
////            playAgain.setVisibility(View.VISIBLE);
////            WinerMsg.setVisibility(View.VISIBLE);
////            WinerMsg.setText("Match Draw es ! ");
//            cc=0;
//        }


        SharedPreferences.Editor editO = getSharedPreferences(ocount ,MODE_PRIVATE).edit();
        editO.putString("Xvalue",Mcount+"");

        SharedPreferences.Editor editX = getSharedPreferences(xcount ,MODE_PRIVATE).edit();
        editO.putString("Ovalue",Scount+"");

        editO.commit();
        editX.commit();





    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Are you Sure you want Exit ?")
                .setCancelable(false)
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }).setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void playAgainGame()
    {
        boardInit();
        playAgain.setVisibility(View.INVISIBLE);
        WinerMsg.setVisibility(View.INVISIBLE);
        gaemOver=false;
        for(int i=0;i<gridLout.getChildCount();i++)
        {
            ImageView imageView;
            imageView = (ImageView) gridLout.getChildAt(i);
            imageView.setImageDrawable(null);
            imageView.setAlpha(0.4f);
        }
        currentPlayer=Player.ONE;
        for(int i=0;i<playerChoice.length;i++)
        {
            playerChoice[i]=Player.NO;
        }




    }
}
