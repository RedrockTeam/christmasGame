package org.redrock.christmas.dao.impl;

import org.redrock.christmas.dao.IUserDao;
import org.redrock.christmas.domain.User;
import org.redrock.christmas.util.DateUtil;

import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

public class UserDaoImpl implements IUserDao{
    private static Connection conn;
//可以在输入的配置加过滤%的过滤器 不过影响不大  只是被注入了like功能
    public UserDaoImpl(){
        try {
            //加载配置文件
            Properties properties = new Properties();
            properties.load(UserDaoImpl.class.getResourceAsStream("/dao.properties"));
            String url = properties.getProperty("mysql.url");
            String user = properties.getProperty("mysql.user");
            String password = properties.getProperty("mysql.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建用户信息
     * @param user
     * @return
     */
    @Override
    public boolean add(User user) {
        String reg="insert into users(openid , nickname , score , imgurl , counts , share , dates)  values(?,?,?,?,20,0,?)";

        try
        {
            PreparedStatement pstmt=conn.prepareStatement(reg);
            pstmt.setString(1,user.getOpenid());
            pstmt.setString(2,user.getNickname());
            pstmt.setInt(3,user.getScore());
            pstmt.setString(4,user.getImgurl());
            pstmt.setString(5,user.getDate());
            pstmt.executeUpdate();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 寻找对应用户
     * @param openid
     * @return
     */
    @Override
    public User find(String openid) {
        try {
            String sql="select * from  users where openid= ?;";
            PreparedStatement pstmt=conn.prepareStatement(sql);
            pstmt.setString(1,openid);
            ResultSet rs=pstmt.executeQuery();
            if(rs.next())
            {
                    User user = new User();
                    user.setOpenid(rs.getString("openid"));
                    user.setNickname(rs.getString("nickname"));
                    user.setScore(rs.getInt("score"));
                    user.setImgurl(rs.getString("imgurl"));
                    user.setCount(rs.getInt("counts"));
                    user.setDate(rs.getString("dates"));
                    user.setShare(rs.getInt("share"));
                    return user;
                }else{
                    return null;
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public boolean update(User user) {
        String reg="UPDATE users SET score = ? , imgurl = ? , nickname = ? , counts = ? , dates = ? , share = ?  WHERE openid = ?;";

        User ruser = find(user.getOpenid());
        if(ruser.getOpenid().equals(user.getOpenid())) {
            try {
                PreparedStatement pstmt = conn.prepareStatement(reg);
                pstmt.setInt(1, user.getScore());
                pstmt.setString(2, user.getImgurl());
                pstmt.setString(3,user.getNickname());
                pstmt.setInt(4,user.getCount());
                pstmt.setString(5,user.getDate());
                pstmt.setInt(6,user.getShare());
                pstmt.setString(7,user.getOpenid());
                pstmt.executeUpdate();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            return add(user);
        }
        return false;
    }

    /**
     * 返回一个带rank的表
     * @return
     */

    @Override
    public ResultSet getRank() {
        String sql = "select * from (select *, (@rank := @rank + 1)rank from (select openid , nickname, imgurl from users order by score DESC)t, (select @rank := 0)a)b ;";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs =stmt.executeQuery(sql);
            return rs;
//                if(rs.getString("openid").equals(user.getOpenid())){
//                    user.setrank = rs.getInt("rank");
//                    return this.rank;
//                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 测试   返回一个数据库链接  上线请删除
     * @return
     */
    public static Connection getConn() {
        return conn;
    }


//    public static void main(String[] args) throws SQLException {
//        String openid = "ouRCyjpsCjN1luOFckV6tZKpPRM4";
//        UserDaoImpl dao = new UserDaoImpl();
//        User ruser = new User();
//        Connection conn = dao.getConn();
//        String sql = "select * from users where openid='"+openid+"';";
//        Statement stmt = conn.createStatement();
//        ResultSet rs =stmt.executeQuery(sql);
//        if (rs.next()) {
//            String date = rs.getString(7);
//            int c_month = Integer.parseInt(date.split("-")[1]);
//            int c_day = Integer.parseInt(date.split("-")[2]);
//            System.out.println(c_day+",,,,"+c_month);
//            String t_date = DateUtil.getdate();
//            System.out.println(t_date);
//        }
//    }
}
