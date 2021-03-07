package core;


import dnl.utils.text.table.TextTable;
import lombok.val;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ErrorTable {

    private final List<Error> errorList = new ArrayList<Error>();

    public void add(Error error) {
        this.errorList.add(error);
    }



    public void printAndFlushAllErrors() {
        if ( errorList.isEmpty() ) {
            return;
        }
        val table = new TextTable(new String[]{"பிழை", "வரி எண்", "எழுத்து எண்"}, new Object[][]{});
        table.printTable();
       // for now just flush it.
        this.errorList.clear();
    }

}
