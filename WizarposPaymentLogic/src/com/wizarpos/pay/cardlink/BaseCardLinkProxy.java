package com.wizarpos.pay.cardlink;

import android.content.Context;

/**
 * Created by wu on 16/3/23.
 */
public abstract class BaseCardLinkProxy implements CardLinkPresenter.CardLinkPresenterListener{

    protected Context context;

    protected CardLinkPresenter cardLinkPresenter;

    public BaseCardLinkProxy(Context context) {
        this.context = context;
        cardLinkPresenter = new CardLinkPresenter();
        cardLinkPresenter.setCardLinkPresenterListener(this);
        cardLinkPresenter.init();
    }

    public void endTrans(){
        cardLinkPresenter.endTrans();
    }

    public CardLinkPresenter getCardLinkPresenter() {
        return cardLinkPresenter;
    }


}
