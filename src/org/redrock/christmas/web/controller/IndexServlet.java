package org.redrock.christmas.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.redrock.christmas.dao.impl.UserDaoImpl;
import org.redrock.christmas.domain.User;
import org.redrock.christmas.util.DateUtil;
import org.redrock.christmas.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 判断是否绑定了小帮手
 * 并且初始化user数据库数据,然后进行跳转
 */
@WebServlet(name = "Index" , value = "/index")
public class  IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/start.html").forward(req,resp);
    }
}
