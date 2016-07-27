package com.fom.msesoft.fomapplication.repository;

import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.model.Places;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by oguz on 17.07.2016.
 */
@Rest(rootUrl = "http://192.168.2.130:8081/places",converters = { MappingJackson2HttpMessageConverter.class })
public interface PlacesRepository {

    @Get("/personWorkSearch?uniqueId={uniqueId}")
    Places personWorkSearch (@Path String uniqueId);
}
