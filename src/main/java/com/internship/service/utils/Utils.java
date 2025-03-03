package com.internship.service.utils;

import com.internship.persistence.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    public static Long getCurrentUserId() {
        return ((User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getId();
    }
}
