package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.common.Query;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserQueryCriteria {
    @Query(propName = "username", type = Query.Type.INNER_LIKE)
    private String username;
    @Query(propName = "firstName", type = Query.Type.INNER_LIKE)
    private String firstName;
    @Query(propName = "lastName", type = Query.Type.INNER_LIKE)
    private String lastName;
}
