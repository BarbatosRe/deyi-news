package com.heima.kafka.listener;

import com.alibaba.fastjson.JSON;
import com.heima.kafka.pojo.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HelloListener {

    @KafkaListener(topics = "ikun-hz")
    public void lintenermsg(String msg){
        if (!StringUtils.isEmpty(msg)){
            User user = JSON.parseObject(msg, User.class);
            System.out.println(user);
        }
    }

}
