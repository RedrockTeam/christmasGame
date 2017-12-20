package org.redrock.christmas.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.omg.CORBA.OBJ_ADAPTER;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Save" , value = "/servlet/save")
public class SaveUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
//        User user = (User) session.getAttribute("user");
        String openid = (String) session.getAttribute("openid");
        String score = req.getParameter("score");
        UserDaoImpl dao = new UserDaoImpl();
        User user = dao.find(openid);
//        String oursecret = req.getParameter("secret");//获取secret
//        if (oursecret == "xxxxx") {
        user.setCount(user.getCount() - 1);//先减少游戏次数
        if (user.getScore() < Integer.parseInt(score)) {
            user.setScore(Integer.parseInt(score));//获取传入的score
        }
        ResultSet rs = dao.getRank();//刷新排名
        user.Rankinfo(rs);
        dao.update(user);
        //json返回
        resp.setHeader("Access-Control-Allow-Origin" , "*");
        session.setAttribute("user", user);
        Map<String, Object> jsonObject = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("nickname", user.getNickname());
        data.put("imgurl", user.getImgurl());
        data.put("rank", user.getRank());
        jsonObject.put("status", 200);
        jsonObject.put("msg", "ok");
        jsonObject.put("data", data);
        JsonUtil.json(resp, jsonObject);
    }
}
