package com.example.mongo.core.controller;

import com.github.microservice.encryption.client.model.data.RecordModel;
import com.github.microservice.encryption.client.model.table.CollectionModel;
import com.github.microservice.encryption.client.model.table.FieldsModel;
import com.github.microservice.encryption.client.ret.content.ResultContent;
import com.github.microservice.encryption.client.service.EncryptionDataRecordService;
import com.github.microservice.encryption.client.service.EncryptionDataTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("data")
public class DataController {

    @Autowired
    private EncryptionDataRecordService encryptionDataRecordService;

    @Autowired
    private EncryptionDataTableService encryptionDataTableService;


    @RequestMapping("create")
    public Object create() {
        CollectionModel collectionModel =   CollectionModel.builder().collectionName("test_1").build();
        collectionModel.setFields(new FieldsModel[]{
                FieldsModel.builder().path("test").bsonType("string").queries(Map.of("queryType","equality")).build()
        });
        Object ret = encryptionDataTableService.createCollection(collectionModel);
        return ret;
    }

    @RequestMapping("save")
    public ResultContent<Page<Map<String, Object>>> save() {
        RecordModel recordModel = new RecordModel();
        recordModel.setCollectionName("tb_user");
        recordModel.setData(Map.of("uid", UUID.randomUUID().toString(), "name", "xiaofeng"));
        return ResultContent.buildContent(encryptionDataRecordService.insertOne(recordModel));
    }

    @RequestMapping("list")
    public ResultContent<Page<Map<String, Object>>> list() {
        ResultContent<Page<Map<String, Object>>> ret = encryptionDataRecordService.list("tb_user", "{'name':'ao'}", null, PageRequest.of(0,20));
        return ResultContent.buildContent(ret);
    }

}
