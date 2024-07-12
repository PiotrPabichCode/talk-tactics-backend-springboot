package com.example.talktactics.util;

import cn.hutool.core.util.ObjectUtil;
import com.example.talktactics.exception.BadRequestException;

public class ValidationUtil {
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " does not exist: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }
}
