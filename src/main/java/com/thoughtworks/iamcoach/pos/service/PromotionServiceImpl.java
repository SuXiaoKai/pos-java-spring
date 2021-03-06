package com.thoughtworks.iamcoach.pos.service;

import com.thoughtworks.iamcoach.pos.dao.PromotionDao;
import com.thoughtworks.iamcoach.pos.dao.PromotionDaoImpl;
import com.thoughtworks.iamcoach.pos.model.CartItem;
import com.thoughtworks.iamcoach.pos.model.Promotion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PromotionServiceImpl implements PromotionService {

    private PromotionDao promotionDao;

    public PromotionServiceImpl(PromotionDao promotionDao) {
        this.promotionDao = promotionDao;
    }

    public PromotionServiceImpl(){

    }
    @Override
    public double calculateMoney(CartItem cartItem) {

        List<Promotion> list = cartItem.getProduct().getPromotions();
        double result = cartItem.getProduct().getPrice() * cartItem.getCount();
        List<Double> money = new ArrayList<Double>();
        Promotion promotion;

        for (int i = 0; i < list.size(); i++) {
            promotion = list.get(i);
            double subTotal = promotion.getMoney(cartItem);
            money.add(subTotal);
        }

        return money.size() == 0 ? result : Collections.min(money);
    }


    @Override
    public int getDiscount(int id){
        return promotionDao.getDiscount(id);
    }

    @Override
    public List<Promotion> getPromotionList(int id) throws SQLException {

        List<Integer> promotionTypes = promotionDao.getPromotionTypes(id);
        List<Promotion> promotionList = new ArrayList<Promotion>();

        for(Integer type : promotionTypes){
            Promotion promotion = PromotionFactory.getInstance(type,this);
            promotionList.add(promotion);
        }

        return promotionList;
    }
}
