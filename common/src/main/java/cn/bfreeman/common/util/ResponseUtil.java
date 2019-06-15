package cn.bfreeman.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lhr
 * @date 2019/6/13
 */
@Slf4j
public class ResponseUtil {

    public static void sendJsonNoCache(HttpServletResponse response, String message) {
        response.setContentType("application/json;charset=UTF-8");
        // response.setHeader("Content-Length", message.getBytes().length + "");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        try {
            response.getWriter().print(message);
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
            ioe.printStackTrace();
        }
    }
}
