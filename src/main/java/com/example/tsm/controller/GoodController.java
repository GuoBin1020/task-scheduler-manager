package com.example.tsm.controller;

import com.example.tsm.entity.GoodInfoEntity;
import com.example.tsm.service.GoodInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/good")
public class GoodController {

    /**
     * 商品业务逻辑实现
     */
    private final GoodInfoService goodInfoService;

    @Autowired
    public GoodController(GoodInfoService goodInfoService) {
        this.goodInfoService = goodInfoService;
    }

    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public Long save(GoodInfoEntity good) throws Exception {
        return goodInfoService.saveGood(good);
    }

}
