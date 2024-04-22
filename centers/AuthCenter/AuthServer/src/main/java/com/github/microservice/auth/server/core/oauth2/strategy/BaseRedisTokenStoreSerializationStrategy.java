package com.github.microservice.auth.server.core.oauth2.strategy;

/**
 * Handles null/empty byte arrays on deserialize and null objects on serialize.
 *
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author efenderbosch
 */
@Deprecated
public abstract class BaseRedisTokenStoreSerializationStrategy implements RedisTokenStoreSerializationStrategy {

	private static final byte[] EMPTY_ARRAY = new byte[0];

	private static boolean isEmpty(byte[] bytes) {
		return bytes == null || bytes.length == 0;
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) {
		if (isEmpty(bytes)) {
			return null;
		}
		return deserializeInternal(bytes, clazz);
	}

	protected abstract <T> T deserializeInternal(byte[] bytes, Class<T> clazz);

	@Override
	public String deserializeString(byte[] bytes) {
		if (isEmpty(bytes)) {
			return null;
		}
		return deserializeStringInternal(bytes);
	}

	protected abstract String deserializeStringInternal(byte[] bytes);

	@Override
	public byte[] serialize(Object object) {
		if (object == null) {
			return EMPTY_ARRAY;
		}
		return serializeInternal(object);
	}

	protected abstract byte[] serializeInternal(Object object);

	@Override
	public byte[] serialize(String data) {
		if (data == null) {
			return EMPTY_ARRAY;
		}
		return serializeInternal(data);
	}

	protected abstract byte[] serializeInternal(String data);

}
