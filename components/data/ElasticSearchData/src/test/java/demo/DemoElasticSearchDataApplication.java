package demo;

import com.github.microservice.core.boot.ApplicationBootSuper;
import demo.simple.dao.SearchTableDao;
import demo.simple.domain.SearchTable;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;


@ComponentScan("demo.simple")
public class DemoElasticSearchDataApplication extends ApplicationBootSuper {


    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(DemoElasticSearchDataApplication.class, args);


//        ElasticsearchOperations elasticsearchTemplate = applicationContext.getBean(ElasticsearchOperations.class);
//        String documentId = "123456";
//        SearchTable searchTable = new SearchTable();
//        searchTable.setId(documentId);
//        searchTable.setContent("content");
//        searchTable.setTitle("title");
//        IndexQuery indexQuery = new IndexQueryBuilder().withId(searchTable.getId()).withObject(searchTable).build();
//        elasticsearchTemplate.index()
//
//        System.out.println("---");

        // jpa 插入数据
        SearchTable searchTable = new SearchTable();
        searchTable.setTitle("_title" + System.currentTimeMillis());
        searchTable.setContent("_conten中文测试");

        SearchTableDao searchTableDao = applicationContext.getBean(SearchTableDao.class);
        searchTableDao.save(searchTable);

        final ElasticsearchOperations elasticsearchTemplate = applicationContext.getBean(ElasticsearchOperations.class);
//
//
        CriteriaQuery criteriaQuery = new CriteriaQuery(Criteria.where("title").is("_title"));
        System.out.println(elasticsearchTemplate.search(criteriaQuery, SearchTable.class));
        System.out.println(elasticsearchTemplate.count(criteriaQuery, SearchTable.class));
//
        System.out.println(searchTableDao.countByTitleIs("_title"));

        // Highlight
        Pageable pageable = PageRequest.of(0, 10);
        QueryBuilder query = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("title", "_title"));
        final NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"))
                .build();

        SearchHits<SearchTable> searchHits = elasticsearchTemplate.search(nativeSearchQuery,
                SearchTable.class);
        searchHits.get().forEach((it) -> {
            it.getHighlightFields().forEach((k, v) -> {
                System.out.println(k);
                System.out.println(v);
            });
        });
        System.out.println(searchHits);


    }


}
