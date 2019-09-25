package com.huahua.socket.model.param.IdCard;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushDataParam {
  /** 前段发送过来的json字符串 */
  private String jsonStr;

  /** 需要推送给的那个人的用户id */
  private String userId;
}
