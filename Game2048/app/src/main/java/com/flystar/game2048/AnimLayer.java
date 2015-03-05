package com.flystar.game2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flystar on 2015/3/1.
 */
public class AnimLayer extends FrameLayout
{
    //卡片的集合
    private List<Card> cards = new ArrayList<Card>();


    public AnimLayer(Context context) {
        super(context);
        initLayer();
    }

    public AnimLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayer();
    }

    public AnimLayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayer();
    }

    private void initLayer()
    {

    }

    //创建卡片的移动动画
    //from 需要移动的卡片，to目的卡片， fromx 需要移动卡片的数组索引（0.1.2.3）
    public void createMoveAnim(final Card from,final Card to,int fromX , int toX , int fromY, int toY)
    {
        final Card c = getCard(from.getNum()); //根据需要移动的卡片的数字生成一个卡片// from为什用括号????

        //LayoutParams 封装了一个Layout的宽高等信息,
        LayoutParams lp = new LayoutParams(Config.CARD_WIDTH,Config.CARD_WIDTH);

        lp.leftMargin = fromX*Config.CARD_WIDTH;
        lp.topMargin = fromY*Config.CARD_WIDTH;

        c.setLayoutParams(lp);  // 上面三行的代码，用来指定卡片的位置是需要移动的卡片的位置上  为什么不直接使用能够Card from?
        //因为Card from是 不能从CardMap中移除的
        //执行的过程是from瞬间就到达了to的位置
        //而动画的 执行需要一段过程
        //如何直接使用from会怎样???
        //经测试是可以正常运行的
        //但是在重玩的时候会有一个卡片没有清除,并且这个没有清除的卡片将会保持不动,为什么？

        if(to.getNum()<=0)
        {
            to.getTvLabel().setVisibility(View.INVISIBLE);
        }

        TranslateAnimation ta = new TranslateAnimation(0,Config.CARD_WIDTH*(toX-fromX),0,Config.CARD_WIDTH*(toY-fromY));

        ta.setDuration(100);

        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                to.getTvLabel().setVisibility(View.VISIBLE); //移动后显示目的的标签显示
                reclycleCard(c);

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {


            }
        });

        c.startAnimation(ta);

    }


    private Card getCard(int num)
    {
        Card c;
        if(cards.size() > 0)
        {
            c = cards.remove(0);
        }
        else
        {
            c = new Card(getContext());
            addView(c);
        }

        c.setVisibility(View.VISIBLE);
        c.setNum(num);

        return c;
    }

    private void reclycleCard(Card c)
    {
        c.setVisibility(View.INVISIBLE);
        c.setAnimation(null);
        cards.add(c);
    }


    //无到有的放大动画
    public void createScaleTo1(Card target)
    {
        ScaleAnimation sa = new ScaleAnimation(0.1f,1,0.1f,1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(100);
        target.setAnimation(null);  //为什么要是设置setAnimation(null);
        target.getTvLabel().startAnimation(sa);
    }
}
