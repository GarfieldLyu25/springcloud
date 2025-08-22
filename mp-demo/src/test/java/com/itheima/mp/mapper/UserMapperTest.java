package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(args = "--mpw.key=e801354ddf538ab8")
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService userService;

    @Test
    void testInsert() {
        User user = new User();
        user.setUsername("hanmeimei");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        //user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(24,"英文老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testBatchByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

//    @Test
//    void testQueryByIds() {
//        List<User> users = userMapper.queryUserByIds(List.of(1L, 2L, 3L, 4L));
//        users.forEach(System.out::println);
//    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(4L);
        user.setBalance(20000);
        userMapper.updateById(user);
        //userMapper.updateUser(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }

    @Test
    void testQueryWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateByQueryWrapper() {
//        // 1.构建查询条件 where name = "Jack"
//        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", "Jack");
//        // 2.更新数据，user中非null字段都会作为set语句
//        User user = new User();
//        user.setBalance(2000);
//        userMapper.update(user, wrapper);


        userMapper.update(null,new UpdateWrapper<User>().eq("username", "jack").set("balance", 2000));
    }

    @Test
    void testUpdateWrapper() {
        List<Long> ids = List.of(1L, 2L, 4L);
        // 1.生成SQL
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200") // SET balance = balance - 200
                .in("id", ids); // WHERE id in (1, 2, 4)
        // 2.更新，注意第一个参数可以给null，也就是不填更新字段和数据，
        // 而是基于UpdateWrapper中的setSQL来更新
        userMapper.update(null, wrapper);
    }
    @Test
    void testLambdaQueryWrapper() {
        // 1.构建条件 WHERE username LIKE "%o%" AND balance >= 1000
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        // 2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testCustomWrapper() {
        // 1.准备自定义查询条件
        List<Long> ids = List.of(1L, 2L, 4L);
        QueryWrapper<User> wrapper = new QueryWrapper<User>().in("id", ids);

        // 2.调用mapper的自定义方法，直接传递Wrapper
        userMapper.deductBalanceByIds(200, wrapper);
    }

    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }
    //30s以上自己停了 估计要10分钟了

    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_batch" + i);
        user.setPassword("123");
        user.setPhone("" + (18688190000L + i));
        user.setBalance(2000);
        //user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(24,"英文老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    @Test
    void testSaveBatch() {
        // 准备10万条数据
        List<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            list.add(buildUser(i));
            // 每1000条批量插入一次
            if (i % 1000 == 0) {
                userService.saveBatch(list);
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }
    //20.948s   减少了连接次数
    //&rewriteBatchedStatements=true  7s 把sql语句合并
    @Test
    void testDbGet() {
        User user = Db.getById(1L, User.class);
        System.out.println(user);
    }

    @Test
    void testDbList() {
        // 利用Db实现复杂条件查询
        List<User> list = Db.lambdaQuery(User.class)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000)
                .list();
        list.forEach(System.out::println);
    }

    @Test
    void testDbUpdate() {
        Db.lambdaUpdate(User.class)
                .set(User::getBalance, 2000)
                .eq(User::getUsername, "Rose");
    }

    @Test
    void testService() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }

    @Test
    void testPageQuery() {
        int pageNo = 1, pageSize = 5;
        // 分页参数
        Page<User> page = Page.of(pageNo, pageSize);
        // 排序参数, 通过OrderItem来指定
        page.addOrder(new OrderItem("balance", false));

        Page<User> p = userService.page(page);
        // 1.分页查询，new Page()的两个参数分别是：页码、每页大小
        //Page<User> p = userService.page(new Page<>(2, 2));
        // 2.总条数
        System.out.println("total = " + p.getTotal());
        // 3.总页数
        System.out.println("pages = " + p.getPages());
        // 4.数据
        List<User> records = p.getRecords();
        records.forEach(System.out::println);


    }
}