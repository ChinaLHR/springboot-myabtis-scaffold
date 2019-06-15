package cn.bfreeman.api.web;

import cn.bfreeman.common.result.ApiResult;
import cn.bfreeman.core.domain.param.UserReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author : lhr
 * @Date : 17:28 2019/6/14
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserApi {

    @RequestMapping(value = "/insertUser", method = RequestMethod.GET)
    public ApiResult insertUser(@Valid UserReq req) {
        log.info("req:{}", req);
        return ApiResult.success();
    }
}
