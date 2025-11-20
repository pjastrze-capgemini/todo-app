package com.cap.api.dtos;

public class UserCredentialsDto {
    public String login;
    public String password;

    public static UserCredentialsDto createNew(String login, String password) {
        var dto = new UserCredentialsDto();
        dto.login = login;
        dto.password = password;
        return dto;
    }
}
