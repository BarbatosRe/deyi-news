package com.heima.wemedia;

import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenImageScan2;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.aliyun.GreenTextScan2;
import com.heima.file.service.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private GreenTextScan2 greenTextScan2;

    @Autowired
    private GreenImageScan greenImageScan;

    @Autowired
    private GreenImageScan2 greenImageScan2;

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testScanText() throws Exception {
        Map map = greenTextScan2.greeTextScan("我是一个好人,冰毒");
        System.out.println(map);

    }

    @Test
    public void testScanImage() throws Exception {
        List<String> stringList = new ArrayList<>();
        stringList.add("http://42.192.81.196:9000/leadnews/2022/test/illust_69541377_20180828_193043.png");
        stringList.add("http://42.192.81.196:9000/leadnews/2022/test/12893895272b4ee50d080834bd.jpg");
        stringList.add("http://42.192.81.196:9000/leadnews/2022/test/MikoXKokomi.png");
        Map map = greenImageScan2.imageScan(stringList);

        //Map map = greenImageScan2.imageScan("");
        System.out.println(map);
    }
}