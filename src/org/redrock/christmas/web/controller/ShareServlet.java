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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "share" , value = "/servlet/share")
public class ShareServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //share success
        HttpSession session = req.getSession();
//        String openid = String.valueOf(session.getAttribute("openid"));//获取用户openid
        String openid = "ouRCyjpYbjwuHt2n7CjpOPnh0Spc";
//        String oursecret = req.getParameter("secret");
//        if(oursecret == "xxxxx") {
            UserDaoImpl dao = new UserDaoImpl();
            User ruser = dao.find(openid);
            if(ruser.getShare() == 0) {
                ruser.setShare(1);
                ruser.setCount(ruser.getCount() + 10);
                dao.update(ruser);
            } else {
                //...
            }
//        } else {
//            //???
//        }
    }
}
