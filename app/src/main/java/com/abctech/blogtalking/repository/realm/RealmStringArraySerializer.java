package com.abctech.blogtalking.repository.realm;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import io.realm.RealmList;

public class RealmStringArraySerializer extends JsonSerializer<RealmList<RealmString>> {
    @Override
    public void serialize(RealmList<RealmString> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartArray();
        for (RealmString s : value) {
            gen.writeString(s.getValue());
        }
        gen.writeEndArray();
    }
}
