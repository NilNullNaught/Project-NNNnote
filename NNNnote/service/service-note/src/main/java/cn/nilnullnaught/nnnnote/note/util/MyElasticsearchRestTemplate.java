package cn.nilnullnaught.nnnnote.note.util;

import cn.nilnullnaught.nnnnote.entity.note.NoteInfo;
import com.google.gson.*;
import org.apache.http.HttpHost;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;


import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MyElasticsearchRestTemplate {


    private RestHighLevelClient restHighLevelClient;
    private Gson gson;

    public MyElasticsearchRestTemplate() {
        this.restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://8.131.72.52:9200")
        ));

        GsonBuilder gsonBuilder = new GsonBuilder();

        // ElasticSearch 的字段中包含了下划线，需要进行转换
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        // ElasticSearch 的 TimeStamp 类型不能直接转换为 LocalDateTime，需要添加 Adapter
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
            }
        });
        this.gson = gsonBuilder.create();
    }

    /**
     * 查询全部
     *
     * @param index
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T> List<T> List(String index, Class<T> clazz) throws IOException {

        // region <- 发送请求 ->
        // 1.准备Request
        SearchRequest request = new SearchRequest(index);
        // 2.准备DSL
        request.source().query(QueryBuilders.matchAllQuery());
        // 3.获得响应
        var response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        // endregion

        // region <- 解析响应 ->
        // 1.获取响应结果
        var searchHits = response.getHits();
        // 2.查询结果为空，返回空的结果集
        if (searchHits.getTotalHits().value == 0) return null;
        // 3.获取文档数组
        var hits = searchHits.getHits();

        // 4.初始化结果集
        var result = new ArrayList<T>();
        // 5.遍历
        for (SearchHit hit : hits) {
            // 获取文档source
            String json = hit.getSourceAsString();
            // 使用 Gson 解析
            var t = this.gson.fromJson(json, clazz);
            result.add(t);
        }
        // endregion

        return result;

    }

    // 分页条件查询，高亮查询条件
    public <T> List<T> List(String index,
                            String fields,
                            String condition,
                            String sortField,
                            Integer page,
                            Integer limit,
                            Class<T> clazz) throws IOException {


        // region <- 发送请求 ->

        // 1.准备 Request
        var request = new SearchRequest(index);
        // 2.封装查询条件
        if (StringUtils.isEmpty(condition)) {
            request.source().query(QueryBuilders.matchAllQuery());
        } else {
            request.source().query(QueryBuilders.matchQuery(fields, condition));
        }
        // 3.排序
        request.source().sort(sortField, SortOrder.ASC);
        // 4.分页
        request.source().from(((page - 1) * limit)).size(limit);
        // 5.高亮
        request.source().highlighter(new HighlightBuilder().field(fields).requireFieldMatch(false));
        // 6.发送请求
        var response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        // endregion

        // region <- 解析响应 ->

        // 1.获取文档 source
        var searchHits = response.getHits();
        // 2.查询结果为 0
        if (searchHits.getTotalHits().value == 0) return null;
        // 3.获取文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.遍历
        return Arrays.stream(hits).map(hit -> {
            // 获取文档source
            String json = hit.getSourceAsString();

            // 使用 Gson 解析
            var t = this.gson.fromJson(json, clazz);

            // 获取高亮结果
            var highlightFields = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highlightFields)) {
                // 根据字段名获取高亮结果
                HighlightField highlightField = highlightFields.get(fields);
                if (highlightField != null) {
                    // 获取高亮值
                    String name = highlightField.getFragments()[0].string();
                    // 覆盖非高亮结果
                    System.out.println(name);
                }
            }
            System.out.println("hotelDoc = " + t);
            return t;
        }).collect(Collectors.toList());

        // endregion
    }


    /**
     * 分页条件查询笔记（笔记状态为公开，且未被逻辑删除）
     *
     * @param condition
     * @param sortField
     * @param page
     * @param limit
     * @return
     * @throws IOException
     */
    public List<NoteInfo> noteList(
            String condition,
            String sortField,
            Integer page,
            Integer limit) throws IOException {


        // region <- 发送请求 ->

        // 1.准备 Request
        var request = new SearchRequest("nnnnote_note_info");
        // 2.封装查询条件
        var boolQuery = QueryBuilders.boolQuery() ;
        if (!condition.isEmpty()) boolQuery.must(QueryBuilders.multiMatchQuery(condition,"title","preview"));

        boolQuery.must(QueryBuilders.termQuery("is_deleted",false));
        boolQuery.must(QueryBuilders.termQuery("status",2));

        request.source().query(boolQuery);
        // 3.排序
        request.source().sort(sortField, SortOrder.ASC);
        // 4.分页
        request.source().from(((page - 1) * limit)).size(limit);
        // 5.高亮
        request.source().highlighter(new HighlightBuilder().field("title").requireFieldMatch(false));
        request.source().highlighter(new HighlightBuilder().field("preview").requireFieldMatch(false));

        // 6.发送请求
        var response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        // endregion

        // region <- 解析响应 ->

        // 1.获取文档 source
        var searchHits = response.getHits();
        // 2.查询结果为 0
        if (searchHits.getTotalHits().value == 0) return null;
        // 3.获取文档数组
        SearchHit[] hits = searchHits.getHits();
        // 4.遍历
        return Arrays.stream(hits).map(hit -> {
            // 获取文档source
            String json = hit.getSourceAsString();

            // 使用 Gson 解析
            var noteInfo = this.gson.fromJson(json, NoteInfo.class);

            // 获取高亮结果
            var highlightFields = hit.getHighlightFields();
            if (!CollectionUtils.isEmpty(highlightFields)) {
                // 根据字段名获取高亮结果
                HighlightField highlightField_1 = highlightFields.get("title");
                HighlightField highlightField_2 = highlightFields.get("preview");

                if (highlightField_1 != null) {
                    // 获取高亮值
                    String title = highlightField_1.getFragments()[0].string();
                    // 覆盖非高亮结果
                    noteInfo.setTitle(title);
                }
                if (highlightField_2 != null) {
                    // 获取高亮值
                    String preview = highlightField_2.getFragments()[0].string();
                    // 覆盖非高亮结果
                    noteInfo.setPreview(preview);
                }
            }
            return noteInfo;
        }).collect(Collectors.toList());

        // endregion
    }
}
