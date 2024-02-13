package com.sanket.wealthify.security.utility;

import java.util.List;

import org.springframework.security.core.userdetails.User;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtils {

	public User convert(com.sanket.wealthify.entity.User user) {
		return new User(user.getEmailId(), user.getPassword(), List.of());
	}

}