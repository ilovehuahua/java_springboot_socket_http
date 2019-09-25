package com.huahua.socket.biz.service.impl;

import com.huahua.socket.biz.service.IdCardService;
import com.huahua.socket.model.param.IdCard.PushDataParam;
import com.huahua.socket.model.vo.IdCard.PushDataVO;
import com.huahua.socket.webapp.controller.socket.WebSocketServerController;
import org.springframework.stereotype.Service;

@Service(value="idCardService")
public class IdCardServiceImpl implements IdCardService {

    @Override
    public PushDataVO sendPushData(PushDataParam pushDataParam){
        if (WebSocketServerController.sendMessage(pushDataParam.getJsonStr(),
                                                  pushDataParam.getUserId())) {
            return PushDataVO.builder().isSuccess(Boolean.TRUE).build();
        }
        return PushDataVO.builder().isSuccess(Boolean.FALSE).build();
    }
}
