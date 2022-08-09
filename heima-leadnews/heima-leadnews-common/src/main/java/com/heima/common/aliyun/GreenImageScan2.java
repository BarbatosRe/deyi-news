package com.heima.common.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.heima.common.aliyun.util.ClientUploader;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ciModel.auditing.AuditingJobsDetail;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingRequest;
import com.qcloud.cos.model.ciModel.auditing.ImageAuditingResponse;
import com.qcloud.cos.region.Region;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "qcloud")
public class GreenImageScan2 {

    private String secretId;
    private String secretKey;

    public Map imageScan(String imageUrl) throws Exception {
        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID和SECRETKEY请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);


        //1.创建任务请求对象
        ImageAuditingRequest request = new ImageAuditingRequest();
        //2.添加请求参数 参数详情请见api接口文档
        //2.1设置请求bucket
        request.setBucketName("leadnews-1312963146");
        //2.2设置审核类型
        request.setDetectType("all");
        //2.3设置bucket中的图片位置
        request.setDetectUrl(imageUrl);
        //3.调用接口,获取任务响应对象
        ImageAuditingResponse response = cosClient.imageAuditing(request);
        //保存结果的
        Map<String, String> resultMap = new HashMap<>();
        try {
            //Success（审核成功）
            if ("Success".equals(response.getState())){
                //将返回结果转换成json格式
                String scrResponse = JSONObject.toJSONString(response,true);
                System.out.println(scrResponse);
                String label = response.getLabel();
                String result = response.getResult();
                if (!result.equals("0")){
                    resultMap.put("result",result);
                    resultMap.put("label",label);
                    return resultMap;
                }

                resultMap.put("result",result);
                resultMap.put("label","pass");
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}