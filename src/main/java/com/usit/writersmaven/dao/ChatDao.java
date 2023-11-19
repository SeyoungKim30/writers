package com.usit.writersmaven.dao;

import java.util.Map;

public interface ChatDao {
    public int insertMessage(Map<String,String> message);
}
