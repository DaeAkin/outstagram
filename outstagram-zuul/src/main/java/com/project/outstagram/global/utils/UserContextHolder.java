package com.project.outstagram.global.utils;

import org.springframework.util.Assert;

/**
 * ThreadLocal 변수에 UserContext를 저장한다. ThreadLocal 변수는
 * 요청을 처리하는 해당 스레드에서 호출되는 모든 메소드에서
 * 액세스 가능한 변수임.
 */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();

    public static final UserContext getContext(){
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);

        }
        return userContext.get();
    }

    public static final void setContext(UserContext context) {
        Assert.notNull(context, "Only non-null UserContext instances are permitted");
        userContext.set(context);
    }

    public static final UserContext createEmptyContext(){
        return new UserContext();
    }
}
