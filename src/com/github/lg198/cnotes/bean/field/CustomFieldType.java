package com.github.lg198.cnotes.bean.field;

public enum CustomFieldType {

    TEXT {
        @Override
        public String getPresentableDefault(String dv) {
            return dv;
        }
    },

    NUMBER {
        @Override
        public String getPresentableDefault(String dv) {
            return dv;
        }
    },

    OPTIONS {
        @Override
        public String getPresentableDefault(String dv) {
            //DefaultValue;value1,value2,value3
            return dv.split(";")[0];
        }
    };

    public abstract String getPresentableDefault(String dv);

}
