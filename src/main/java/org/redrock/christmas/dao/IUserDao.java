package org.redrock.christmas.dao;

import org.redrock.christmas.domain.User;

import java.sql.ResultSet;

/**
 * @author momo
 * 数据库接口类
 */
public interface IUserDao {
    /**
     * 添加用户
     * @param user
     */
    boolean add(User user);

    /**
     * 根据openid查找用户
     * @param openid
     */
    User find(String openid);

    /**
     * 更新用户信息
     * @param user
     */

    boolean update(User user);

    /**
     * @return re 数据库取回的带着rank信息的ResultSet
     */

    ResultSet getRank();
}
