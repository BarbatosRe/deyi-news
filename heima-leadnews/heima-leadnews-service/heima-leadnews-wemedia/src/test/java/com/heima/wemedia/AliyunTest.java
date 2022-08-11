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
       /* List<String> stringList = new ArrayList<>();
        stringList.add("http://42.192.81.196:9000/leadnews/2022/08/03/7a0a3720-64db-432c-8979-2048fd370993.png");
        stringList.add("http://42.192.81.196:9000/leadnews/2022/08/03/6367e903-5722-4f5b-ab1a-3040dd58ddf0.png");
        Map map = greenImageScan2.imageScan(stringList);*/

        Map map = greenImageScan2.imageScan("http://42.192.81.196:9000/leadnews/2022/08/03/3.png");
        System.out.println(map);
    }
}