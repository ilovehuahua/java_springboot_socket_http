package com.huahua.socket.webapp.controller.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huahua.socket.enums.WebSocketServerMsgTypeEnum;
import com.huahua.socket.model.vo.WebSocketServer.WebSocketServerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * socket处理
 *
 * @author huahua
 * @date 2019-08-22
 */
@ServerEndpoint(value = "/socket/{userId}")
@Component
public class WebSocketServerController {

  private static final Logger log = LoggerFactory.getLogger(WebSocketServerController.class);
  /** 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。 */
  private static int onlineCount = 0;
  /** 使用map对象，便于根据userId来获取对应的WebSocket */
  private static Map<String, Session> websocketMap = new ConcurrentHashMap<>();
  /** 接收sid */
  private String userId = "";

  /** 连接id和用户id对应关系 */
  private static Map<String, String> userIdConnectionIdMap = new ConcurrentHashMap<>();

  /**
   * 连接建立成功调用的方法
   *
   * @param session
   * @param userId
   */
  @OnOpen
  public void onOpen(Session session, @PathParam("userId") String userId) {
    userIdConnectionIdMap.put(userId, session.getId());
    websocketMap.put(session.getId(), session);
    // 在线数加一
    addOnlineCount();
    log.info("有新窗口开始监听:" + session.getId() + ",当前在线人数为" + getOnlineCount());
    try {
      sendMessage(
          session,
          JSON.toJSONString(
              WebSocketServerVO.builder()
                  .msgType(WebSocketServerMsgTypeEnum.SUCCESS_LOGIN.getValue())
                  .userId(session.getId())
                  .build()));
    } catch (IOException e) {
      log.error("websocket IO异常");
    }
  }

  /** 连接关闭调用的方法 */
  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    if (websocketMap.get(session.getId()) != null) {
      log.info(
          "有一连接关闭！连接id: "
              + session.getId()
              + "当前在线人数为"
              + getOnlineCount()
              + "关闭原因："
              + closeReason.getReasonPhrase());
      // 移除连接
      websocketMap.remove(session.getId());
      // 遍历连接id和用户id关系，清除关系
      for (Map.Entry<String, String> entry : userIdConnectionIdMap.entrySet()) {
        if (entry.getValue() == session.getId()) {
          userIdConnectionIdMap.remove(entry.getKey());
          break;
        }
      }
      // 在线数减1
      subOnlineCount();
    }
  }

  /**
   * 收到客户端消息后调用的方法
   *
   * @param message 客户端发送过来的消息
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("收到来自窗口" + session.getId() + "的信息");
    if (!StringUtils.isEmpty(message)) {
      JSONArray list = JSONArray.parseArray(message);
      for (int i = 0; i < list.size(); i++) {
        try {
          // 解析发送的报文
          JSONObject object = list.getJSONObject(i);
          String msgType = object.getString("msgType");
          switch (msgType) {
            case "PING":
              sendMessage(
                  session,
                  JSON.toJSONString(
                      WebSocketServerVO.builder()
                          .msgType(WebSocketServerMsgTypeEnum.PONG.getValue())
                          .userId(session.getId())
                          .build()));
              break;
            case "NEW_CARE_DATA":;
              break;
            default:;
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 错误
   *
   * @param session
   * @param error
   */
  @OnError
  public void onError(Session session, Throwable error) {
    log.error("发生错误");
    error.printStackTrace();
  }

  /**
   * 服务器推送消息
   *
   * @param session
   * @param message
   * @throws IOException
   */
  private static void sendMessage(Session session, String message) throws IOException {
    log.info("主动推送消息到窗口，userid：" + session.getId() + "，推送内容");
    session.getBasicRemote().sendText(message);
  }

  /**
   * 推送消息给用户
   *
   * @param message
   * @param userId
   * @return
   */
  public static Boolean sendMessage(String message, String userId) {
    try {
      if (null != getConnectIdByUserId(userId)
          && null != websocketMap.get(getConnectIdByUserId(userId))) {
        log.info("推送消息到窗口" + getConnectIdByUserId(userId) + "，推送内容");
        sendMessage(websocketMap.get(getConnectIdByUserId(userId)), message);
        return Boolean.TRUE;
      } else {
        log.info("该用户不在线" + userId);
        return Boolean.FALSE;
      }
    } catch (IOException e) {
      log.info("给该用户发送信息时，发生IO错误：" + userId);
      return Boolean.FALSE;
    }
  }

  public static synchronized int getOnlineCount() {
    return onlineCount;
  }

  public static synchronized void addOnlineCount() {
    WebSocketServerController.onlineCount++;
  }

  public static synchronized void subOnlineCount() {
    WebSocketServerController.onlineCount--;
  }

  /**
   * 根据用户id获取连接id
   *
   * @param userId
   */
  private static String getConnectIdByUserId(String userId) {
    return userIdConnectionIdMap.get(userId);
  }
}
