package com.abctech.blogtalking.repository.realm;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import java.io.IOException;

import io.realm.RealmList;

public class RealmStringArrayDeserializer extends JsonDeserializer<RealmList<RealmString>> {
    @Override
    public RealmList<RealmString> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (!p.isExpectedStartArrayToken()) {
            throw new RuntimeJsonMappingException("Token does not start array ");
        }
        RealmList<RealmString> intList = new RealmList<>();
        JsonToken token;
        do {
            token = p.nextToken();
            if (token == JsonToken.VALUE_STRING) {
                intList.add(new RealmString(p.getValueAsString()));
            }
        } while (token != JsonToken.END_ARRAY);

        return intList;
    }
}
