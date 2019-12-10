package com.tobiasy.simple.utils;

import com.tobiasy.simple.bean.ObjectData;
import com.tobiasy.simple.bean.User;
import org.junit.Test;

import java.util.List;

/**
 * @author tobiasy
 * @date 2019/10/21
 */
public class CollectionUtilsTest {

    @Test
    public void splitList() {
        List<User> list = ObjectData.getUserList(100);
        List<List<User>> lists = CollectionUtils.incise(list, 5);
        for (List<User> users : lists) {
            users.forEach(System.out::println);
            System.out.println();
        }
    }

}