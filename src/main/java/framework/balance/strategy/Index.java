package framework.balance.strategy;
//用于记录当前轮询位置
public class Index {
    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Index(int value) {
        this.value = value;
    }
}
