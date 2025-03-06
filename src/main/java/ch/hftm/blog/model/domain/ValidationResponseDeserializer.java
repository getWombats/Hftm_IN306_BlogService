package ch.hftm.blog.model.domain;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class ValidationResponseDeserializer extends JsonbDeserializer<ValidationResponse> {
    public ValidationResponseDeserializer() {
        super(ValidationResponse.class);
    }
}