package com.github.microservice.app.feign;

import feign.Param;
import feign.codec.EncodeException;
import feign.querymap.FieldQueryMapEncoder;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FeignPageableDefaultFieldQueryMapEncoder extends FieldQueryMapEncoder {

    private final Map<Class<?>, ObjectParamMetadata> classToMetadata = new ConcurrentHashMap<>();


    @Override
    public Map<String, Object> encode(Object object) throws EncodeException {

        final LinkedMultiValueMap<String, Object> ret = new LinkedMultiValueMap();

        final ObjectParamMetadata metadata = classToMetadata.computeIfAbsent(object.getClass(), ObjectParamMetadata::parseObjectType);
        metadata.objectFields.stream()
                .map(field -> this.FieldValuePair(object, field))
                .filter(fieldObjectPair -> fieldObjectPair.right.isPresent())
                .collect(Collectors.toMap(
                        this::fieldName,
                        fieldObjectPair -> fieldObjectPair.right.get(),
                        (address1, address2) -> {
                            return address1;
                        }
                ))
                .entrySet()
                .stream()
                .filter(it -> !it.getKey().equalsIgnoreCase("sort"))
                .forEach(it -> ret.add(it.getKey(), it.getValue()));

        //特殊处理pageable
        if (object instanceof Pageable) {
            process(ret, (Pageable) object);
        }

        return (Map) ret;
    }

    /**
     * 重写sort
     *
     * @param ret
     * @param pageable
     */
    private void process(LinkedMultiValueMap<String, Object> ret, Pageable pageable) {
        pageable.getSort().forEach((sort) -> {
            ret.add("sort", sort.getProperty() + "," + sort.getDirection().name());
        });
    }


    private String fieldName(Pair<Field, Optional<Object>> pair) {
        Param alias = pair.left.getAnnotation(Param.class);
        return alias != null ? alias.value() : pair.left.getName();
    }

    private Pair<Field, Optional<Object>> FieldValuePair(Object object, Field field) {
        try {
            return Pair.pair(field, Optional.ofNullable(field.get(object)));
        } catch (IllegalAccessException e) {
            throw new EncodeException("Failure encoding object into query map", e);
        }
    }

    private static class ObjectParamMetadata {

        private final List<Field> objectFields;

        private ObjectParamMetadata(List<Field> objectFields) {
            this.objectFields = Collections.unmodifiableList(objectFields);
        }

        private static ObjectParamMetadata parseObjectType(Class<?> type) {
            List<Field> allFields = new ArrayList();

            for (Class currentClass = type; currentClass != null; currentClass =
                    currentClass.getSuperclass()) {
                Collections.addAll(allFields, currentClass.getDeclaredFields());
            }

            return new ObjectParamMetadata(allFields.stream()
                    .filter(field -> !field.isSynthetic())
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toList()));
        }
    }

    private static class Pair<T, U> {
        private Pair(T left, U right) {
            this.right = right;
            this.left = left;
        }

        public final T left;
        public final U right;

        public static <T, U> Pair<T, U> pair(T left, U right) {
            return new Pair<>(left, right);
        }

    }

}
