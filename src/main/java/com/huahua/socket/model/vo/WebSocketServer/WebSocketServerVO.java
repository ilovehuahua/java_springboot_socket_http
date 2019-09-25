package com.huahua.socket.model.vo.WebSocketServer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketServerVO<T> {
  private String msgType;

  private T data;

  private String userId;
}
