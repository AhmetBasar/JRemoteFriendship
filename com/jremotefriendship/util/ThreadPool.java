package com.jremotefriendship.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

	private static volatile ThreadPool instance;
//	public static final int POOL_SIZE = (Runtime.getRuntime().availableProcessors() / 2);
//	public static final int POOL_SIZE = (Runtime.getRuntime().availableProcessors() / 4);
	public static final int POOL_SIZE = 1;
	private ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);

	public static ThreadPool getInstance() {
		if (instance == null) {
			synchronized (ThreadPool.class) {
				if (instance == null) {
					instance = new ThreadPool();
				}
			}
		}
		return instance;
	}

	private ThreadPool() {
	}

	public void execute(Runnable runnable, boolean sync) {
		if (sync) {
			try {
				executorService.submit(runnable).get();
			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();
			}
		} else {
			executorService.submit(runnable);
		}
	}

	public void execute(List<Runnable> runnables, boolean sync) {
		if (sync) {
			List<Callable<Object>> callables = convertToCallable(runnables);
			try {
				executorService.invokeAll(callables);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		} else {
			for (Runnable runnable : runnables) {
				executorService.submit(runnable);
			}
		}
	}

	private List<Callable<Object>> convertToCallable(List<Runnable> runnables) {
		List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
		for (Runnable r : runnables) {
			callables.add(Executors.callable(r));
		}
		return callables;
	}
	
	public void shutDown() {
		executorService.shutdown();
	}

}
