package com.github.microservice.auth.server.core.dao.extend;

import com.github.microservice.auth.server.core.domain.UserLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserLogDaoExtend {

    Page<UserLog> list(Pageable pageable);

}
