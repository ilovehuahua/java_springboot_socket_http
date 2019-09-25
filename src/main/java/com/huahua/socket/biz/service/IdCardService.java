package com.huahua.socket.biz.service;

import com.huahua.socket.model.param.IdCard.PushDataParam;
import com.huahua.socket.model.vo.IdCard.PushDataVO;

public interface IdCardService {
    /**
     * http推送数据到socket
     * @param pushDataParam
     * @return
     */
    PushDataVO sendPushData(PushDataParam pushDataParam);
}
