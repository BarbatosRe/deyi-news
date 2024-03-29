package com.heima.common.aliyun;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.heima.utils.common.Base64Utils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ciModel.auditing.*;
import com.qcloud.cos.region.Region;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.xml.ws.Response;
import java.util.*;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "qcloud")
public class GreenTextScan2 {

    private String secretId;
    private String secretKey;

    public Map<String, String> greeTextScan(String content) throws Exception {
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

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * 待检测的文本，长度不超过10000个字符
         */
        task1.put("content", content);
        tasks.add(task1);
        JSONObject data = new JSONObject();
        /**
         * 检测场景，文本垃圾检测传递：antispam
         **/
        data.put("tasks", tasks);
        String base64Str = Base64Utils.encode(data.toJSONString().getBytes("UTF-8"));

        //1.创建任务请求对象
        TextAuditingRequest request = new TextAuditingRequest();
        //2.添加请求参数 参数详情请见api接口文档
        request.setBucketName("leadnews-1312963146");
        //2.1.1设置对象地址
        //request.getInput().setObject("1.txt");
        //2.1.2或直接设置请求内容,文本内容的Base64编码
        request.getInput().setContent(base64Str);
        //2.2设置审核类型参数
        request.getConf().setDetectType("all");
        //2.3设置审核模板（可选）
        //request.getConf().setBizType("aa3e9d84a6a079556b0109a935c*****");
        //3.调用接口,获取任务响应对象
        TextAuditingResponse response = cosClient.createAuditingTextJobs(request);

        //保存结果的
        Map<String, String> resultMap = new HashMap<>();
        try {
            //得到任务的详细信息
            AuditingJobsDetail jobsDetail = response.getJobsDetail();
            //Success（审核成功）
            if ("Success".equals(jobsDetail.getState())){
    //            //将返回结果转换成json格式
    //            String body = JSONObject.toJSONString(jobsDetail);
    //            Object parse1 = JSON.parse(body);
    //            String s = parse1.toString();
    //            JSONObject scrResponse = JSONObject.parseObject(s);

    //            System.out.println(JSON.toJSONString(scrResponse, true));
                String label = jobsDetail.getLabel();
                String result = jobsDetail.getResult();
                if (!"0".equals(result)){
                    resultMap.put("result",result);
                    resultMap.put("label",label);
                    return resultMap;
                }

                resultMap.put("result",result);
                return resultMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}