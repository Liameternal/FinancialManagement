package com.mirliam.financialmanagement;

public class FinancialDbSchema {
    public static final class UserTable {
        public static final String NAME = "user";

        public static final class Cols {
            public static final String ACCOUNT = "account";
            public static final String PASSWD = "passwd";
        }
    }

    public static final class FinancialDetailsTable {
        public static final String NAME = "financialDetails";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String TYPE = "type";
            public static final String DATE = "date";
            public static final String MONEY = "money";
            public static final String REMARK = "remark";
            public static final String PICTURE = "picture";
        }
    }
}
