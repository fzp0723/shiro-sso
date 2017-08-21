package com.bigsys.shiro.session;

import com.bigsys.redis.service.RedisDb;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import java.io.*;

/**
 * Created by fangzhipeng on 2017/8/15.
 */
public class SessionManager extends EnterpriseCacheSessionDAO {

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        RedisDb.setObject(sessionId.toString().getBytes(), sessionToByte(session));
        return sessionId;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        // 先从缓存中获取session，如果没有再去数据库中获取
//        Session session = super.doReadSession(sessionId);
        Session session = null;
        if(session == null){
            byte[] bytes = RedisDb.getObject(sessionId.toString().getBytes());
            if(bytes != null && bytes.length > 0){
                session = byteToSession(bytes);
            }
        }
        return session;
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        RedisDb.setObject(session.getId().toString().getBytes(), sessionToByte(session));
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
        RedisDb.delString(session.getId() + "");
    }

    // 把session对象转化为byte保存到redis中
    public byte[] sessionToByte(Session session){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(session);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    // 把byte还原为session
    public Session byteToSession(byte[] bytes){
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream in;
        SimpleSession session = null;
        try {
            in = new ObjectInputStream(bi);
            session = (SimpleSession) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return session;
    }

}
