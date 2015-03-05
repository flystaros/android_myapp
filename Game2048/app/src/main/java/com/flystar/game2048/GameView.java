package com.flystar.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import com.flystar.game2048.tools.LogMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flystar on 2015/2/28.
 */
public class GameView extends GridLayout
{
    private Card[][] cardMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
    private static GameView gameView = null;

    public GameView(Context context) {
        super(context);

        initGameView();
        gameView = this;
    }

    public static GameView getGameView() {
        return gameView;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }



    public void initGameView()
    {
        setBackgroundColor(0xffbbada0);
        setColumnCount(4);
        setOnTouchListener(new OnTouchListener()
        {

            private float startX;
            private float startY;
            private float offsetX;
            private float offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if(Math.abs(offsetX) > Math.abs(offsetY))
                        {
                            if(offsetX < -5)
                            {
                                LogMessage.logPrint("left");
                                swipeLeft();
                            }
                            else if(offsetX > 5)
                            {
                                LogMessage.logPrint("right");
                                swipeRight();
                            }

                        }
                        else
                        {
                            if(offsetY < -5)
                            {
                                LogMessage.logPrint("up");
                                swipeUp();
                            }
                            else if(offsetY > 5)
                            {
                                LogMessage.logPrint("down");
                                swipeDown();
                            }
                        }
                        break;
                }



                return true;
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Config.CARD_WIDTH = (Math.min(w,h) - 10)/4;

        //添加卡片
        addCards(Config.CARD_WIDTH ,Config.CARD_WIDTH );
        startGame();
    }

    public  void startGame()
    {
        MainActivity.getMainActivity().clearScore();
        //开始游戏前先清除
        for (int y = 0 ; y < 4 ; y++)
        {
            for( int x = 0 ; x < 4 ; x++)
            {
                cardMap[x][y].setNum(0);
            }
        }

        addRandom();
        addRandom();
    }


    public  void startGameTest()
    {
        MainActivity.getMainActivity().clearScore();
        //开始游戏前先清除
        for (int y = 0 ; y < 4 ; y++)
        {
            for( int x = 0 ; x < 4 ; x++)
            {
                cardMap[x][y].setNum(0);
            }
        }


    }

    private void addCards(int cardWidht,int cardHeight)
    {
        Card c;
        for(int y=0; y < 4; y++)
        {
            for(int x = 0; x < 4; x++)
            {
                c = new Card(getContext());
                c.setNum(0);

                addView(c, cardWidht, cardHeight);
                cardMap[x][y] = c;
            }
        }
    }

    //添加随机数
    public void addRandom()
    {
        //1,先记录空数字的位置
        emptyPoints.clear();

        for (int y = 0 ; y < 4 ; y++)
        {
            for (int x = 0 ; x < 4; x++)
            {
                if(cardMap[x][y].getNum() <= 0)
                {
                    emptyPoints.add(new Point(x,y));
                }
            }
        }


        //2，在空数字的位置添加随机数
          //移除一个随机的点
        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
        cardMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2:4);

        MainActivity.getMainActivity().getAnimLayer().createScaleTo1(cardMap[p.x][p.y]);


    }

    private void swipeLeft()
    {
        boolean merge = false;
        for (int y = 0 ; y < 4 ; y++)
        {
            for(int x = 0 ; x < 4 ; x++)
            {
                for(int x1 = x+1 ; x1 < 4 ; x1++)
                {
                    if(cardMap[x1][y].getNum() > 0)
                    {
                        if(cardMap[x][y].getNum() <= 0)
                        {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardMap[x1][y],cardMap[x][y],x1,x,y,y);
                            cardMap[x][y].setNum(cardMap[x1][y].getNum());
                            cardMap[x1][y].setNum(0);
                            x--;
                            merge = true;
                        }
                        else if(cardMap[x][y].getNum() == cardMap[x1][y].getNum())
                        {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardMap[x1][y],cardMap[x][y],x1,x,y,y);

                            cardMap[x][y].setNum(cardMap[x1][y].getNum()*2);
                            cardMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if(merge)
        {
            addRandom();
            checkComplete();
        }

    }
    private void swipeRight()
    {
        boolean merge = false;
        for (int y = 0 ; y < 4 ; y++)
        {
            for(int x = 3 ; x > -1  ; x--)
            {
                for(int x1 = x-1 ; x1 > -1 ; x1--)
                {
                    if(cardMap[x1][y].getNum() > 0)
                    {
                        if(cardMap[x][y].getNum() <= 0)
                        {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardMap[x1][y],cardMap[x][y],x1,x,y,y);
                            cardMap[x][y].setNum(cardMap[x1][y].getNum());
                            cardMap[x1][y].setNum(0);
                            x++;
                            merge = true;
                        }
                        else if(cardMap[x][y].getNum() == cardMap[x1][y].getNum())
                        {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardMap[x1][y],cardMap[x][y],x1,x,y,y);
                            cardMap[x][y].setNum(cardMap[x1][y].getNum()*2);
                            cardMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            merge = true;
                        }
                        break;

                    }
                }
            }
        }
        if(merge)
        {
            addRandom();
            checkComplete();
        }



    }
    private void swipeUp()
    {
        boolean merge = false;
        for (int x = 0 ; x < 4 ; x++)
        {
            for(int y= 0 ; y < 4 ; y++)
            {
                for(int y1 = y+1 ; y1 < 4 ; y1++)
                {
                    if(cardMap[x][y1].getNum() > 0)
                    {
                        if(cardMap[x][y].getNum() <= 0)
                        {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardMap[x][y1],cardMap[x][y],x,x,y1,y);

                            cardMap[x][y].setNum(cardMap[x][y1].getNum());
                            cardMap[x][y1].setNum(0);
                            y--;
                            merge = true;
                        }
                        else if(cardMap[x][y].getNum() == cardMap[x][y1].getNum())
                        {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardMap[x][y1],cardMap[x][y],x,x,y1,y);

                            cardMap[x][y].setNum(cardMap[x][y1].getNum()*2);
                            cardMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            merge = true;
                        }
                        break;

                    }
                }
            }
        }
        if(merge)
        {
            addRandom();
            checkComplete();
        }


    }
    private void swipeDown()
    {
        boolean merge = false;
        for (int x = 0 ; x < 4 ; x++)
        {
            for(int y= 3 ; y > -1 ; y--)
            {
                for(int y1 = y-1 ; y1 > -1 ; y1--)
                {
                    if(cardMap[x][y1].getNum() > 0)
                    {
                        if(cardMap[x][y].getNum() <= 0)
                        {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardMap[x][y1],cardMap[x][y],x,x,y1,y);

                            cardMap[x][y].setNum(cardMap[x][y1].getNum());
                            cardMap[x][y1].setNum(0);
                            y++;
                            merge = true;
                        }
                        else if(cardMap[x][y].getNum() == cardMap[x][y1].getNum())
                        {
                            MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardMap[x][y1],cardMap[x][y],x,x,y1,y);

                            cardMap[x][y].setNum(cardMap[x][y1].getNum()*2);
                            cardMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMap[x][y].getNum());
                            merge = true;
                        }
                        break;

                    }
                }
            }
        }
        if(merge)
        {
            addRandom();
            checkComplete();
        }


    }





    public void checkComplete()
    {
        boolean complete = true;
        ALL :
        for(int y = 0 ; y < 4; y++)
        {
            for(int x = 0 ; x < 4; x++)
            {
                if(cardMap[x][y].getNum() == 0 ||
                        (x>0&&cardMap[x][y].equals(cardMap[x-1][y]))||
                        (x<3&&cardMap[x][y].equals(cardMap[x+1][y]))||
                        (y>0&&cardMap[x][y].equals(cardMap[x][y-1]))||
                        (y<3&&cardMap[x][y].equals(cardMap[x][y+1]))
                        )
                {
                    complete = false;
                    break ALL;

                }
            }
        }

        if(complete)
        {
            new AlertDialog.Builder(getContext()).setTitle("hello").setMessage("gameOver").setPositiveButton("retry",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }
    }

}
