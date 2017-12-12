package com.zinc.librouter.modle;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/12
 * @description
 */

public class Configuration {

    private boolean debuggable;
    private String[] modules;

    public boolean isDebuggable() {
        return debuggable;
    }

    public void setDebuggable(boolean debuggable) {
        this.debuggable = debuggable;
    }

    public String[] getModules() {
        return modules;
    }

    public void setModules(String[] modules) {
        this.modules = modules;
    }

    private Configuration() {
    }

    public static class Builder{
        private boolean debuggable;
        private String[] modules;

        public Builder setDebuggable(boolean debuggable) {
            this.debuggable = debuggable;
            return this;
        }

        public Builder registerModules(String ...modules){
            this.modules = modules;
            return this;
        }

        public Configuration build(){
            if(modules == null || modules.length == 0){
                throw new RuntimeException("You must call registerModules() to initalize JRouter");
            }

            Configuration configuration = new Configuration();
            configuration.debuggable = this.debuggable;
            configuration.modules = this.modules;

            return configuration;

        }

    }

}
