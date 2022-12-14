package ru.job4j.concurrent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private final int size;

    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public synchronized Integer getSize() {
        return queue.size();
    }

    public void offer(T value) throws InterruptedException {
        synchronized (this) {
            while (queue.size() >= size) {
                this.wait();
            }
            queue.offer(value);
            this.notifyAll();
        }
    }

    /**
     * Метод poll() должен вернуть объект из внутренней коллекции.
     * Если в коллекции объектов нет, то нужно перевести
     * текущую нить в состояние ожидания.
     *
     * @return
     */
    public T poll() throws InterruptedException {
        T element;
        synchronized (this) {
            while (queue.size() == 0) {
                this.wait();
            }
            element = queue.poll();
            this.notifyAll();
        }
        return element;
    }
}