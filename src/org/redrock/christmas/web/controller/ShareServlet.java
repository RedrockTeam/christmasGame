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
        UserDaoImpl dao = new UserDaoImpl();
        HttpSession session = req.getSession();
        User user = dao.find((String) session.getAttribute("openid"));
//        String oursecret = req.getParameter("secret");
//        if(oursecret == "xxxxx") {

            if(user.getShare() == 0) {
                user.setShare(1);
                user.setCount(user.getCount() + 10);
                dao.update(user);
            }
            session.setAttribute("user",user);
//        } else {
//            //???
//        }
    }
}
