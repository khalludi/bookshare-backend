package com.gmu.bookshare.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public final class JsonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {

    }

    /**
     * Utility method to write json object as string.
     *
     * @param object - json obect
     * @return - json string representing the object
     */
    public static String writeValueAsString(@NotNull Object object) {

        String jsonStr = "";
        try {
            jsonStr = getObjectMapperInstance().writeValueAsString(object);
        } catch (JsonProcessingException pe) {
            LOG.error("failed to create JSON String: {}", pe.getMessage());
        }

        return jsonStr;

    }

    /**
     * Utility method to parse any json string and convert to java class.
     *
     * @param str       - json string
     * @param classType - target class type
     * @param <T>       - template
     * @return - returns java object based on jason string
     */
    public static <T> T parseString(@NotNull String str, Class<T> classType) {

        ObjectMapper mapper = getObjectMapperInstance();
        try {
            return mapper.readValue(str, classType);
        } catch (IOException exp) {
            LOG.error("failed to parse JSON String: {}", exp.getMessage());
        }
        return null;
    }

    /**
     * Get Object Mapper Instance.
     *
     * @return - ObjectMapper
     */
    private static ObjectMapper getObjectMapperInstance() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JodaModule());
        return mapper;
    }


    /**
     * returns the mapper jackson xml mapper.
     */
    public static XmlMapper getXmlMapperInstance() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return xmlMapper;
    }
}
