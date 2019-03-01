/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author obinna.asuzu
 */
public class UserDetailDeserializer  implements JsonDeserializer{
    private Class<?> implClass;

    public UserDetailDeserializer(Class<?> c) {
        implClass = c;
    }
    
    @Override
    public UserDetails deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
         return context.deserialize(json, implClass);
    }
    
}
