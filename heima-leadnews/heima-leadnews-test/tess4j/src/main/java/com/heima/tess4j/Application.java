package com.heima.tess4j;


import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Application {

    /**
     * 识别图中文字
     * @param args
     */
    public static void main(String[] args) throws TesseractException {
        //创建实例
        ITesseract tesseract=new Tesseract();

        //设置字体库路径
        tesseract.setDatapath("D:\\JavaPlace\\WorkSpace_Java\\Tesseract");

        //设置语言
        tesseract.setLanguage("chi_sim");

        File file = new File("F:\\test.png");
        //识别图片
        String result = tesseract.doOCR(file);

        System.out.println("识别的结果是："+result);
    }

}
