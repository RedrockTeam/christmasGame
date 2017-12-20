package org.redrock.christmas.domain;

import org.redrock.christmas.dao.impl.UserDaoImpl;
import org.redrock.christmas.util.CurlUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author momo
 * 用户实体类
 */

public class User {

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    String openid;
    String nickname;
    int score;
    String imgurl;
    int count;
    String date;
    int share;
    int rank;


    /**
     * 从rank表更新user的rank
     * @param rs
     * @return
     */
    public int Rankinfo(ResultSet rs){
        if (rs != null) {//判断是否成功查询
            try {
                while (rs.next()) {
                    if (rs.getString("openid").equals(this.getOpenid())) {
                        this.setRank(rs.getInt("rank"));//刷新用户排名
                        return this.rank;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return -1;
        }
        return -1;
    }
}
