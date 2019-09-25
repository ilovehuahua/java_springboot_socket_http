package com.huahua.socket.webapp.app;

import com.huahua.socket.biz.service.IdCardService;
import com.huahua.socket.model.param.IdCard.PushDataParam;
import com.huahua.socket.model.vo.IdCard.PushDataVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 身份证信息推送控制器
 *
 * @author huahua
 * @date 2019-08-23
 */
@RestController
@RequestMapping("/idCard")
public class IdCardController {

  /** log打印统一使用 slf4j */
  private static final Logger log = LoggerFactory.getLogger(IdCardController.class);

  @Resource private IdCardService idCardService;

  @RequestMapping(value = "/pushData", method = RequestMethod.POST)
  public PushDataVO pushData(@RequestBody PushDataParam
                                                     pushDataParam) {
    return idCardService.sendPushData(pushDataParam);
  }
}
