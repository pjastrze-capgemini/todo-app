package com.cap.api.dtos;

public class RegisterUserDto {
    public String login;
    public String password;
    public String confirmPassword;

    public static RegisterUserDto createNew(String login, String password, String confirmPassword) {
        var dto = new RegisterUserDto();
        dto.login = login;
        dto.password = password;
        dto.confirmPassword = confirmPassword;
        return dto;
    }

}
