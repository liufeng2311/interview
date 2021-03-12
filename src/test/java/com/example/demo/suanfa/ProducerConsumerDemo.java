package com.example.demo.suanfa;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: liufeng
 * @Date: 2021/2/3
 * @desc 生产者消费者
 */
public class ProducerConsumerDemo {

  static ArrayBlockingQueue queue = new ArrayBlockingQueue(20);


  public static void main(String[] args) {

    Thread thread = new Thread("生产者") {
      @Override
      public void run() {
        while (true) {
          System.out.println(Thread.currentThread().getName() + "生产了");
          queue.add("添加元素");
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    };

    thread.start();

    Thread thread1 = new Thread("消费者") {

      @Override
      public void run() {
        try {
          while (true) {
            queue.take();
            System.out.println(Thread.currentThread().getName() + "消费了: ");
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };

    thread1.start();
  }
}
