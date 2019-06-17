package cn.bfreeman.api.base;

import cn.bfreeman.api.ApiApplication;
import cn.bfreeman.core.dao.UserDao;
import cn.bfreeman.core.domain.entity.UserDO;
import cn.bfreeman.core.domain.enums.SexType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author : lhr
 * @Date : 17:00 2019/6/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiApplication.class)
public class UserDaoTest {

    @Resource
    private UserDao userDao;

    @Test
    public void testInsert(){
        UserDO userDO = new UserDO();
        userDO.setSexType(SexType.MALE);
        userDO.setName("LNX");
        userDao.insertSelective(userDO);
    }
}
