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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "Rank" , value = "/servlet/getrank")
public class GetRankServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDaoImpl dao = new UserDaoImpl();
        ResultSet rs = dao.getRank();//获取rank表
        int all = 1;
        int rank = 0;
        HttpSession session = req.getSession();
        if(rs == null){//检查是否返回了rank表
            Map<String , Object> jsonObject = new HashMap<>();
            jsonObject.put("data" , null);
            jsonObject.put("msg" , "数据库发生错误,可能是没有数据");
            jsonObject.put("status" , 400);
            JsonUtil.json(resp,jsonObject);
        } else {
            Map<String , Object> jsonObject = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            try {
                while (rs.next()){//循环加入每个用户的rank信息  如果数量大于50就退出
//                    if(rs.getString("openid").equals(String.valueOf(session.getAttribute("openid")))){
                    if(rs.getString("openid").equals("ouRCyjpYbjwuHt2n7CjpOPnh0Spc")){//测试
                        rank = rs.getInt("rank");
                    }
                    Map<String , Object> person = new HashMap<>();
                    person.put("openid",rs.getString("openid"));
                    person.put("nickname" , rs.getString("nickname"));
                    person.put("imgurl" , rs.getString("imgurl"));
                    person.put("rank",rs.getInt("rank"));

                    data.put(String.valueOf(rs.getInt("rank")), person);
                    all += 1;
                    if(all >= 50) {
                        break;
                    }
                }
//                User user = dao.find(String.valueOf(session.getAttribute("openid")));
                User user = dao.find("ouRCyjpYbjwuHt2n7CjpOPnh0Spc");
                Map<String , Object> person = new HashMap<>();
                person.put("openid",user.getOpenid());
                person.put("nickname" , user.getNickname());
                person.put("imgurl" ,user.getImgurl());
                person.put("rank",rank);
                data.put("my" , person);
                jsonObject.put("data" , data);
                jsonObject.put("msg","ok");
                jsonObject.put("status" , 200);
                JsonUtil.json(resp,jsonObject);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



    }
}
