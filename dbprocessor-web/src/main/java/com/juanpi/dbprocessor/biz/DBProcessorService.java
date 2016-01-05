package com.juanpi.dbprocessor.biz;

import java.util.List;

import com.juanpi.dbprocessor.entity.DBProcessorEntity;


public interface DBProcessorService {

    public List<DBProcessorEntity> findDBProcessorList();
    
}
