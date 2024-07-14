package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.common.BaseMapper;
import com.piotrpabich.talktactics.user.dto.UserDto;
import com.piotrpabich.talktactics.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {

}
