package test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class person implements Serializable {
    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    List<Integer>list=new ArrayList<>();

}
