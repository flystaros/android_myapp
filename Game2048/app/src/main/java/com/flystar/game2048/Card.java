package com.flystar.game2048;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by flystar on 2015/3/1.
 */
public class Card extends FrameLayout
{
    private int num = 0;
    private TextView tvLabel;
    private View background;

    public Card(Context context)
    {
        super(context);
        tvLabel = new TextView(getContext());
        tvLabel.setTextSize(30);
        tvLabel.setGravity(Gravity.CENTER);

        LayoutParams lp = null;
        lp = new LayoutParams(-1,-1);
        lp.setMargins(10,10,0,0);

        addView(tvLabel, lp);

        //添加背景
        background = new View(getContext());
        background.setBackgroundColor(0x33ffffff);
        lp = new LayoutParams(-1,-1);
        lp.setMargins(10,10,0,0);
        addView(background,lp);

    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if(num <= 0)
        {
            tvLabel.setText("");
        }
        else
        {
            tvLabel.setText(num + "");
        }

        switch (num)
        {
            case 0:
                tvLabel.setBackgroundColor(0x00000000);
                tvLabel.setText("");
                break;
            case 2:
                tvLabel.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                tvLabel.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                tvLabel.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                tvLabel.setBackgroundColor(0xfff59563);
                break;
            case 32:
                tvLabel.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                tvLabel.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                tvLabel.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                tvLabel.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                tvLabel.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                tvLabel.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                tvLabel.setBackgroundColor(0xffedc22e);
                break;
            default:
                tvLabel.setBackgroundColor(0xff3c3a32);
                break;
        }

    }

    public boolean equals(Card c)
    {
        return getNum() == c.getNum();
    }

    public TextView getTvLabel() {
        return tvLabel;
    }
}
