package com.shurman.homevisits.database;

import com.shurman.homevisits.data.DMonth;

public interface DataLoad {
    interface Month extends DataLoad {
        void monthParsed(DMonth month);
    }
}
