package com.example.demo.Mapper;

import com.example.demo.DTOs.UserDTO;
import com.example.demo.Entities.User;

public class UserMapper {

    public static UserDTO mapFromEntityToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                null,
                user.getImage(),
                user.getEnable(),
                user.getRoleId(),
                user.getCreateAt(),
                user.getUpdateAt(),
                user.getRole()
        );
    }

    public static User mapFromDTOToEntity(UserDTO userDTO) {
        return new User(
                userDTO.getUserId(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getFullName(),
                userDTO.getImage(),
                userDTO.getEnable(),
                userDTO.getRoleId(),
                userDTO.getCreateAt(),
                userDTO.getUpdateAt(),
                userDTO.getRole()
        );
    }
}
