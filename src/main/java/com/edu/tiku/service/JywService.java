package com.edu.tiku.service;

import com.edu.tiku.model.request.JywLoginRequest;
import com.edu.tiku.model.request.JywRegisterRequest;

/**
 * description:
 * author: dyh
 * date: 2023/2/1 15:58
 */
public interface JywService {

    String register(JywRegisterRequest request);

    String login(JywLoginRequest request);
}
