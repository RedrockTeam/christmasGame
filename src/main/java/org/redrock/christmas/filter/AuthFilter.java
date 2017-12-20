package org.redrock.christmas.filter;



import org.redrock.christmas.domain.User;
import org.redrock.christmas.service.UserInfo;
import org.redrock.christmas.util.PropertyUtil;
import org.redrock.christmas.util.StringUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@WebFilter(filterName = "AuthFilter", value = "/*")
public class AuthFilter implements Filter {
    private String apiUrl = "https://wx.idsbllp.cn/MagicLoop/index.php?s=/addon/Api/Api/oauth&redirect=";
    private String bindUrl = "https://wx.idsbllp.cn/MagicLoop/index.php?s=/addon/Bind/Bind/bind/openid/";

    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        this.check(request, response);

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {}

    private void check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String openid = (String) session.getAttribute("openid");
        String nickname = "";
        String headimgurl = "";
        if (StringUtil.isBlank(openid)) {
            String isDubug = PropertyUtil.getProperty("weixin.debug");
            if (isDubug.equalsIgnoreCase("true")) {
                openid = PropertyUtil.getProperty("weixin.openid");
                nickname = PropertyUtil.getProperty("weixin.nickname");
                headimgurl = PropertyUtil.getProperty("weixin.imgurl");
            } else {
                openid = request.getParameter("openid");
                if (StringUtil.isBlank(openid)) {
                    String indexUrl = PropertyUtil.getProperty("url.index");
                    String redirectUrl = apiUrl + URLEncoder.encode(indexUrl, "UTF-8");
                    response.sendRedirect(redirectUrl);
                    return;
                }
                nickname = URLDecoder.decode(request.getParameter("nickname"),"UTF-8");
                headimgurl = URLDecoder.decode(request.getParameter("headimgurl"),"UTF-8");
            }
            session.setAttribute("openid", openid);
        } else {
            User user = (User) session.getAttribute("user");
            nickname = user.getNickname();
            headimgurl = user.getImgurl();
        }
        if (session.getAttribute("student") == null) {
            User user = UserInfo.stuInfo(openid , nickname , headimgurl);
            if (user == null) {
                response.sendRedirect(bindUrl + openid);
                return;
            }
            session.setAttribute("user", user);
        }
    }
}
