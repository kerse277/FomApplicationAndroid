package com.fom.msesoft.fomapplication.repository;


import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.model.Token;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


/**
 * Created by oguz on 30.06.2016.
 */
@Rest(rootUrl = "http://192.168.2.120:8081/person",converters = { MappingJackson2HttpMessageConverter.class })
public interface PersonRepository {

    @Get("/regGCM?token={token}&regId={regId}")
    void registerGCM(@Path String token,@Path String regId);

    @Get("/signIn?email={email}&password={password}")
    Token signIn (@Path String email, @Path String password);

    @Get("/findByFirstDegreeFriend?token={token}")
    CustomPerson[] findByFirstDegreeFriend(@Path String token);

    @Get("/workNotFriend?uniqueId={uniqueId}")
    CustomPerson[] workNotFriend(@Path String uniqueId);

    @Post("/signUp")
    Person insert(@Body Person person);

    @Get("/friendDegree?token={token}&degree={degree}&skip={skip}")
    CustomPerson[] findDegreeFriend(@Path String token, @Path int degree , @Path int skip);

    @Get("/findByToken?token={token}")
    CustomPerson findByToken(@Path String token);

    @Get("/findByUniqueId?uniqueId={uniqueId}")
    CustomPerson findPersonByUniqueId(@Path String uniqueId);

    @Post("/uploadPhoto")
    void uploadPhoto(@Body byte[] base64photo);
}