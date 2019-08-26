// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import java.util.concurrent.Semaphore;
import java.util.concurrent.LinkedBlockingQueue;

public class AVMediaProcessQueue
{
    private Thread a;
    private LinkedBlockingQueue<Runnable> b;
    private Object c;
    private Runnable d;
    
    public AVMediaProcessQueue() {
        this.c = new Object();
        this.d = new Runnable() {
            @Override
            public void run() {
                while (!AVMediaProcessQueue.this.a.isInterrupted()) {
                    AVMediaProcessQueue.this.a();
                }
            }
        };
        this.b = new LinkedBlockingQueue<Runnable>(10);
        (this.a = new Thread(this.d)).start();
    }
    
    public void runAsynchronouslyOnProcessingQueue(final Runnable e) {
        try {
            this.b.put(e);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void runSynchronouslyOnProcessingQueue(final Runnable runnable) {
        final Semaphore semaphore = new Semaphore(0);
        this.runAsynchronouslyOnProcessingQueue(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                semaphore.release();
            }
        });
        try {
            semaphore.acquire();
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void quit() {
        synchronized (this.c) {
            this.c.notifyAll();
        }
        this.a.interrupt();
    }
    
    public void clearAll() {
        this.b.clear();
    }
    
    private void a() {
        Runnable runnable;
        while ((runnable = this.b.poll()) != null) {
            runnable.run();
        }
    }
}
