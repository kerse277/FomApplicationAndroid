package com.fom.msesoft.fomapplication.repository;

import com.fom.msesoft.fomapplication.model.FriendRelationship;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by oguz on 20.07.2016.
 */
@Rest(rootUrl = "http://192.168.2.120:8081/friendRelationShip",converters = { MappingJackson2HttpMessageConverter.class })
public interface FriendRepository {
    @Post("/saveFriend")
    FriendRelationship saveFriend (@Body FriendRelationship friendRelationship);
}
