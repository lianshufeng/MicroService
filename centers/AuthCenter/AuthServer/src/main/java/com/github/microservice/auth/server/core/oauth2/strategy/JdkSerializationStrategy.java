/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.microservice.auth.server.core.oauth2.strategy;

import com.github.microservice.auth.server.core.oauth2.util.SerializationUtils;
import org.springframework.core.serializer.support.SerializationFailedException;

import java.io.Serializable;


/**
 * Serializes and deserializes allowed objects using {@link SerializationUtils}.
 *
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author efenderbosch
 * @author Artem Smotrakov
 */
@Deprecated
public class JdkSerializationStrategy extends StandardStringSerializationStrategy {

    private static final byte[] EMPTY_ARRAY = new byte[0];

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return (T) SerializationUtils.deserialize(bytes);
        } catch (Exception e) {
            throw new SerializationFailedException("Failed to deserialize payload", e);
        }
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        if (object == null) {
            return EMPTY_ARRAY;
        }
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException(this.getClass().getSimpleName()
                + " requires a Serializable payload but received an object of type ["
                + object.getClass().getName() + "]");
        }

        try {
            return SerializationUtils.serialize(object);
        } catch (Exception e) {
            throw new SerializationFailedException("Failed to serialize object", e);
        }
    }

}
