package com.piotrpabich.talktactics.service.user;

import com.piotrpabich.talktactics.common.BaseMapper;
import com.piotrpabich.talktactics.dto.user.UserDto;
import com.piotrpabich.talktactics.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {

}
