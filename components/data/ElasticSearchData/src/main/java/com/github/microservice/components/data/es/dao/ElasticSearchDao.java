package com.github.microservice.components.data.es.dao;

import com.github.microservice.components.data.es.domain.SuperEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * ES的dao
 *
 * @param <T>
 */
public interface ElasticSearchDao<T extends SuperEntity> extends ElasticsearchRepository<T, String> {

}
