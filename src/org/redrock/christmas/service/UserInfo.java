package org.redrock.christmas.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.redrock.christmas.dao.impl.UserDaoImpl;
import org.redrock.christmas.domain.User;
import org.redrock.christmas.util.CurlUtil;
import org.redrock.christmas.util.DateUtil;

public class UserInfo {
    public static User stuInfo(String openid ,String nickname , String imgurl) {

        String url = "https://wx.idsbllp.cn/MagicLoop/index.php?s=/addon/UserCenter/UserCenter/getStuInfoByOpenId&openId=" + openid;
        String res = CurlUtil.getContent(url, null, "GET");
        JSONObject jsonObject = JSON.parseObject(res);
        if (jsonObject.getInteger("status") == 200) {
            User user = new User();
            user.setOpenid(openid);
            user.setNickname(nickname);
            user.setImgurl(imgurl);
            UserDaoImpl dao = new UserDaoImpl();
            User ruser = dao.find(openid);//查找是否已经加入数据库
            String t_date = DateUtil.getdate();//获得格式化日期
                if (ruser != null) { //如果加入过了
                ruser.setImgurl(imgurl);//更新头像url
                ruser.setNickname(nickname);//更新昵称
                if (DateUtil.Same_month(ruser.getDate(), t_date)) {//检查是否过了一天需要更新游戏次数
                    if (!DateUtil.Same_day(ruser.getDate(), t_date)) {//如果过了一天
                        ruser.setDate(t_date);//更新游戏次数和分享的相关数据
                        ruser.setCount(20);
                        ruser.setShare(0);
                    }
                }
                dao.update(ruser);//上传数据
                user = ruser;//更新返回给上层的数据
            } else {//如果没加入过
                user.setRank(-1);//初始化数据
                user.setScore(0);
                user.setCount(20);
                user.setShare(0);
                user.setDate(t_date);
                dao.add(user);//加入数据库
            }
            return user;
        } else {
            return null;
        }
    }
}
