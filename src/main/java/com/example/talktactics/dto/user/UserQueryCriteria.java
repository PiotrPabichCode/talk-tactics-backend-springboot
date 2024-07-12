package com.example.talktactics.dto.user;

import com.example.talktactics.common.Query;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserQueryCriteria {
    @Query(propName = "username" ,type = Query.Type.INNER_LIKE)
    private String username;
    @Query(propName = "first_name", type = Query.Type.INNER_LIKE)
    private String firstName;
    @Query(propName = "last_name", type = Query.Type.INNER_LIKE)
    private String lastName;
}
