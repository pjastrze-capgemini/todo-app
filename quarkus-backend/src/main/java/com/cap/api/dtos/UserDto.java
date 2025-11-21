package com.cap.api.dtos;

import com.cap.domain.user.User;

public class UserDto {
    public Long id;
    public String name;

    public static UserDto from(User user) {
        var dto = new UserDto();
        dto.id = user.id;
        dto.name = user.name;
        return dto;
    }
}
