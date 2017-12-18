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

        User user = new User();
        HttpSession session = req.getSession();
//        user.setOpenid(String.valueOf(session.getAttribute("openid")));//获取用户openid
        user.setOpenid("ouRCyjpYbjwuHt2n7CjpOPnh0Spc");
        user.setScore(Integer.parseInt(req.getParameter("score")));//获取传入的score
//        String oursecret = req.getParameter("secret");//获取secret
//        if (oursecret == "xxxxx") {
            UserDaoImpl dao = new UserDaoImpl();
            User ruser = dao.find(user.getOpenid());//获取该用户数据库信息
            if (ruser != null) {
                ruser.setCount(ruser.getCount() - 1);//先减少游戏次数
                if (ruser.getScore() < user.getScore()) {//如果score比用户数据库信息高
                    ruser.setScore(user.getScore());//刷新分数
                }
                dao.update(ruser);
                ResultSet rs = dao.getRank();//刷新排名
                ruser.Rankinfo(rs);
            } else {
                //似乎不可能  但是考虑空openid进来的话还是要避免操作数据库
                Map<String, Object> jsonObject = new HashMap<>();
                jsonObject.put("msg", "未保存的openid");
                jsonObject.put("status", "400");
                jsonObject.put("data", null);
                JsonUtil.json(resp, jsonObject);
            }
            //json返回
            Map<String, Object> jsonObject = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put("openid", ruser.getOpenid());
            data.put("nickname", ruser.getNickname());
            data.put("imgurl", ruser.getImgurl());
            data.put("rank", ruser.getRank());
            jsonObject.put("status", 200);
            jsonObject.put("msg", "ok");
            jsonObject.put("data", data);
            JsonUtil.json(resp, jsonObject);
//        } else {
//            //???
//        }
    }
}
