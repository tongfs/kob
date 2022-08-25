package com.kob.backend.mainserver.service.user;

import java.util.Map;

/**
 * @author tfs
 * @date 2022-08-20
 * @description
 */
public interface AccountService {
    Map<String, String> getInfo();

    Map<String, String> getToken(String username, String password);

    Map<String, String> register(String username, String password, String confirmedPassword);
}
