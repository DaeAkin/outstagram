package com.project.outstagram;

import com.project.outstagram.global.event.source.SimpleSourceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventTestController {

    @Autowired
    SimpleSourceBean simpleSourceBean;

    @GetMapping("/event-test")
    public void test() {
        simpleSourceBean.publishLoginChange("getAction?","someIdzz");
    }






}
