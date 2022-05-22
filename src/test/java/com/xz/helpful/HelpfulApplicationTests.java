package com.xz.helpful;

import com.xz.helpful.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

//@SpringBootTest
@Slf4j
class HelpfulApplicationTests {


    @Autowired
    TaskService taskService;

    @Test
    void contextLoads() {
        //List<Task> all = taskService.findAll(52);
        //log.info("大小："+all.size());
    }

    @Test
    void textMain() {
        String st = "哈哈哈哈";
        String st2 = "哈哈哈哈";
        System.out.println(st.hashCode());
        System.out.println(st2.hashCode());
    }

    @Test
    void test() {
        int[] nums = new int[]{1, 2, 3, 4};

        int[] target = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            target[i] = total(nums, i);
        }

        System.out.println(Arrays.toString(target));
    }

    int total(int[] nums, int index) {
        int temp = 0;
        for (int i = 0; i <= index; i++) {
            temp += nums[i];
        }
        return temp;
    }

}
