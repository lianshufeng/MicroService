package com.github.microservice.auth.server.core.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.server.core.dao.ApplicationClientDao;
import com.github.microservice.auth.server.core.domain.ApplicationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ApplicationClientService {

    @Autowired
    private ApplicationClientDao applicationClientDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResultContent<String> add(String clientId, String passWord, Set<String> authorizedGrantTypes) {
        if (applicationClientDao.existsByClientId(clientId)) {
            return ResultContent.build(ResultState.ClientExists);
        }
        ApplicationClient client = new ApplicationClient();
        client.setClientId(clientId);
        client.setSecret(passwordEncoder.encode(passWord));
        client.setAuthorizedGrantTypes(authorizedGrantTypes);
        this.applicationClientDao.save(client);
        return ResultContent.build(ResultState.Success, client.getId());
    }


}
