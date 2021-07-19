package com.springboot.consumer;

import java.util.List;

public interface ProcessCallback {
    
    public void receiveMessage(List<Object> objectList);

}
