package com.zinc.librouter.matcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/10
 * @description
 */

public class MatcherRegister {

    private static final List<AbsMatcher> registry = new ArrayList<>();

    static {
        registry.add(new DirectMatcher(0x1000));
        registry.add(new SchemeMatcher(0x0100));
        registry.add(new ImplicitMatcher(0x0010));
        registry.add(new BrowserMatcher(0x0000));
        Collections.sort(registry);
    }

    public static void addMatcher(AbsMatcher matcher){
        registry.add(matcher);
        Collections.sort(registry);
    }

    public static List<AbsMatcher> getMatcher() {
        return registry;
    }

    public static void clear(){
        registry.clear();
    }

}
