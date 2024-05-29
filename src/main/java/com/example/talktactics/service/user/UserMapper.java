package com.example.talktactics.service.user;

import com.example.talktactics.common.BaseMapper;
import com.example.talktactics.dto.user.UserDto;
import com.example.talktactics.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {

}
