package org.redrock.christmas.web.controller;


import org.redrock.christmas.dao.impl.UserDaoImpl;
import org.redrock.christmas.domain.User;
import org.redrock.christmas.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "userinfo" , value = "/servlet/userinfo")
public class UserInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String openid = String.valueOf(session.getAttribute("openid"));//获取用户openid;
        UserDaoImpl dao = new UserDaoImpl();
        User user = dao.find(openid);//寻找用户信息
        ResultSet rs = dao.getRank();//获取rank表
        int rank = user.Rankinfo(rs);//调用rankinfo更新user的rank信息
        Map<String , Object> jsonObject = new HashMap<>();
        //json返回
        resp.setHeader("Access-Control-Allow-Origin" , "*");
        Map<String , Object> data = new HashMap<>();
        data.put("nickname" , user.getNickname());
        data.put("imgurl" , user.getImgurl());
        data.put("rank",user.getRank());
        data.put("count" , user.getCount());
        data.put("share",user.getShare());
        jsonObject.put("data" , data);
        jsonObject.put("msg","ok");
        jsonObject.put("status" , 200);
        JsonUtil.json(resp,jsonObject);
    }
}
