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
import javax.xml.ws.RequestWrapper;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 判断是否绑定了小帮手
 * 并且初始化user数据库数据,然后进行跳转
 */
@WebServlet(name = "Isstu" , value = "/servlet")
public class IsStuServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String openid = req.getParameter("openid");
//        String nickname = req.getParameter("nickname");
//        String imgurl = req.getParameter("headimgurl");
//        if (openid != null && nickname != null && imgurl != null) {
            User user = new User();
//        user.setOpenid(openid);
//        user.setNickname(URLDecoder.decode(nickname));
//        user.setImgurl(URLDecoder.decode(imgurl)); //获取url参数
            user.setOpenid("ouRCyjpYbjwuHt2n7CjpOPnh0Spc");
            user.setNickname("水舞丶流花");
            user.setImgurl("https://avatars3.githubusercontent.com/u/23241933?s=460&v=4"); //获取url参数     ??TODO：检查下前端发过来的还有没有urlencode
            String res = user.stuInfo();//调用端口检查是否绑定小帮手
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.getInteger("status") == 200) {
                UserDaoImpl dao = new UserDaoImpl();
                User ruser = dao.find(user.getOpenid());//查找是否已经加入数据库
                String t_date = DateUtil.getdate();//获得格式化日期
                if (ruser != null) { //如果加入过了
                    ruser.setImgurl(user.getImgurl());//更新头像url
                    ruser.setNickname(user.getNickname());//更新昵称
                    if (DateUtil.Same_month(ruser.getDate(), t_date)) {//检查是否过了一天需要更新游戏次数
                        if (!DateUtil.Same_day(ruser.getDate(), t_date)) {//如果过了一天
                            ruser.setDate(t_date);//更新游戏次数和分享的相关数据
                            ruser.setCount(20);
                            ruser.setShare(0);
                        }
                    }
                    dao.update(ruser);//上传数据
                    user = ruser;//更新返回给前端的数据
                } else {//如果没加入过
                    user.setRank(-1);//初始化数据
                    user.setScore(0);
                    user.setCount(20);
                    user.setShare(0);
                    user.setDate(t_date);
                    dao.add(user);//加入数据库
                }
                //格式化返回给前端的json
//            Map<String , Object> data = new HashMap<>();
//            data.put("openid",user.getOpenid());
//            data.put("nickname" , user.getNickname());
//            data.put("imgurl" , user.getImgurl());
//            data.put("rank",user.getRank());
//            data.put("count" , user.getCount());
//            data.put("share",user.getShare());
//            jsonObject.put("data" , data);
//            jsonObject.put("msg","ok");
//            jsonObject.put("status" , 200);
//            JsonUtil.json(resp,jsonObject);
                req.getSession().setAttribute("openid", user.getOpenid());
                req.getRequestDispatcher("/views/start.html").forward(req, resp);
            } else {//未绑定
//            jsonObject.put("data",null);
//            jsonObject.put("msg","用户未绑定小帮手");
//            jsonObject.put("status",400);
//            JsonUtil.json(resp,jsonObject);

            }

//        }else {
//            String url = "";
//            resp.sendRedirect(url);
//        }
    }
}
