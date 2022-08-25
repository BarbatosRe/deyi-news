package com.heima.schedule.service.impl;

import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.ScheduleApplication;
import com.heima.schedule.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {
    @Autowired
    private TaskService taskService;

    @Test
    public void addTask(){

        for (int i = 0; i < 5; i++) {
            Task task = new Task();
            task.setTaskType(100+i);
            task.setPriority(60);
            task.setParameters(("Task test"+i).getBytes());
            task.setExecuteTime(new Date().getTime()+5000*i);

            long taskId = taskService.addTask(task);
            //System.out.println(taskId);
        }

    }

    @Test
    public void cancelTask(){
        taskService.cancelTask(1561345903674253314L);
    }

    @Test
    public void pollTask(){
        taskService.poll(110,60);
    }
}
