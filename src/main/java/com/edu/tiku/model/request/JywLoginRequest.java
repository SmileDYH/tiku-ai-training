package com.edu.tiku.model.request;

import lombok.Data;

/**
 * description:
 * author: dyh
 * date: 2022/12/29 17:14
 */
@Data
public class JywLoginRequest {

    public String userID;

    public String apiID;

    public String userPwd;

    public String apiKey;
}
