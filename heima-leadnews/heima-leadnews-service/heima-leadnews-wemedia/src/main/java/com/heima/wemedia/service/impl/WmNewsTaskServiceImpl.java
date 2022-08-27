package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.schedule.IScheduleClient;
import com.heima.common.enums.TaskTypeEnum;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.common.ProtostuffUtil;
import com.heima.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient iScheduleClient;

    @Autowired
    private WmNewsAutoScanServiceImpl wmNewsAutoScanService;
    /**
     * 添加任务到延迟队列中
     *
     * @param id          文章的id
     * @param publishTime 文章的发布时间 可以试作任务执行时间
     */
    @Override
    public void addNewsToTask(Integer id, Date publishTime) {

        log.info("添加任务到延迟服务中----begin");

        Task task = new Task();
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        task.setExecuteTime(publishTime.getTime());
        //创建一个wmnews对象然后注入id值再进行一个序列化后加入task的parameters
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews));

        iScheduleClient.addTask(task);
        log.info("添加任务到延迟服务中----end");

    }

    /**
     * 消费延迟队列数据
     */
    @Override
    @Scheduled(fixedRate = 1000)
    public void scanNewsByTask() {

        log.info("文章审核---消费任务执行---begin---");
        try {
        ResponseResult result = iScheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(),
                TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if (result.getCode().equals(200) && result.getData()!=null) {


            //从ResponseResult Data中获取task数据 并且转换成json字符串 再转化成Task类
            String task_json = JSON.toJSONString(result.getData());
            Task task = JSON.parseObject(task_json, Task.class);
            WmNews wmNews = ProtostuffUtil.deserialize(task.getParameters(), WmNews.class);
            System.out.println(wmNews.getId() + "-----------");
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
            }
        } catch(Exception e){
            log.info("文章审核---消费任务执行---异常错误！!!---");
            e.printStackTrace();
        }
            log.info("文章审核---消费任务执行---end---");
        }
    }
