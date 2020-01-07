package com.project.outstagram.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//
//@Component
/**
 * 모든 주울 필터는 ZuulFilter 클래스를 확장하고 filterType()과 filterOrder(), shouldFilter(), run()등
 * 4개의 메서드를 재정의 해야한다.
 */

public class TrackingFilter extends ZuulFilter {
    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    @Autowired
    FilterUtils filterUtils; // 모든 필터에서 공통으로 사용되는 기능을 FilterUtils 클래스에 담음.


    @Override
    // 주울에 사전,경로,사후 필터를 지정하는데 사용된다.
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    // 주울이 다른 필터 유형으로 요청을 보내야 하는 순서를 나타내는 정수 값을 반환한다.
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    // 필터의 활성화 여부
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private boolean isCorrelationIdPresent() {
        if(filterUtils.getCorrelationId() != null) {
            return true;
        }
        return false;
    }

    private String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * run 메소드는 서비스가 필터를 통과할 때마다 실행되는 코드다.
     * run() 메소드는 tmx-correlation-id 존재의 여부를 확인하고
     * 없다면 생성하고 tmx-correlation-id HTTP 헤더를 설정한다.
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        if(isCorrelationIdPresent()) {
            logger.debug("tmx-correlation-id found in tracking filter: {}." ,
                    filterUtils.getCorrelationId());
        }
        else {
            filterUtils.setCorrelationId(generateCorrelationId());
            logger.debug("tmx-correlation-id generated in tracking filter: {}." ,
                    filterUtils.getCorrelationId());
        }

        RequestContext ctx = RequestContext.getCurrentContext();
        logger.debug("Processing incoming request for {}." , ctx.getRequest().getRequestURI());
        return null;
    }

}
