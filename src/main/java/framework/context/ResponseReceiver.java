package framework.context;

import framework.message.ResponseMessage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ResponseReceiver {
    /**
     * 存储异步返回结果的阻塞队列
     */
    private BlockingQueue<ResponseMessage> responseQueue = new ArrayBlockingQueue<>(1);
    /**
     * 记录异步返回结果的存储时刻
     */
    private long responseTime;

    /**
     * 计算该返回结果是否已经过期,按正常过程put结果后就会立即get,这个验证过期主要是用于异常情况(如put后,发生异常未get)
     */
    public boolean isExpire() {
        ResponseMessage response = responseQueue.peek();
        if (null == response) {
            // 可能是异步结果还未存入阻塞队列中,也有可能是异步结果已经取走
            // 但是异步结果取走后,该对象在ResponseReceiverHolder的Map中也会remove掉,此对象的isExpire方法就不会被调用
            return false;
        }
        long timeout = response.getTimeout();
        if ((System.currentTimeMillis() - responseTime) > timeout) {
            return true;
        }
        return false;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public BlockingQueue<ResponseMessage> getResponseQueue() {
        return responseQueue;
    }
}
