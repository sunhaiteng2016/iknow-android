package com.beyond.library.network.task;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Task任务管理器
 * @author linjinfa@126.com
 * @date 2013-4-16 上午10:07:33
 */
public class TaskManager {
	
	private static TaskManager mManager = null;
	/**
	 * 任务队列
	 */
	private Queue<ITask> mTaskQueue;
	/**
	 * 管理视图控制器
	 */
	private ConcurrentLinkedQueue<IUIController> mControllerList;

	private ExecutorService pool = null;
	
	private TaskManager() {
		mTaskQueue = new LinkedList<ITask>();
		mControllerList = new ConcurrentLinkedQueue<IUIController>();
	}

	public synchronized static TaskManager getInstance() {
		if (mManager == null)
			mManager = new TaskManager();
		return mManager;
	}

	/**
	 * 单例  线程池
	 *
	 * @return
	 */
	private ExecutorService getPoolInstance() {
		if (pool == null || (pool != null && pool.isShutdown()))
			pool = Executors.newCachedThreadPool();
		return pool;
	}

	/**
	 * 清理
	 */
	private void shutdownNow() {
		if (pool != null)
			pool.shutdownNow();
		pool = null;
	}

	/**
	 * 有新任务进来，唤醒线程
	 * @param task
	 */
	public synchronized void addTask(ITask task) {
		if (!mTaskQueue.contains(task)) {
			mTaskQueue.offer(task);
		}
		task.setFuture(getPoolInstance().submit(TaskRunnable.getInstance()));
	}

	/**
	 * 取出队列的第一个
	 * @return
	 */
	public synchronized ITask getTask() {
        try {
            return mTaskQueue.poll();
        } catch (Exception e) {
            return null;
        }
    }

	/**
	 * 注册视图控制器
	 * @param con
	 */
	public void registerUIController(IUIController con) {
		if (!mControllerList.contains(con))
			mControllerList.add(con);
	}

	/**
	 * 移除视图控制器
	 * @param con
	 */
	public void unRegisterUIController(IUIController con) {
		if (mControllerList.contains(con))
			mControllerList.remove(con);
	}

	/**
	 * 根据task的标识获取对应的视图控制器
	 * @param identification
	 * @return
	 */
	public IUIController getUIController(String identification) {
		for (IUIController controller : mControllerList) {
			if (controller.getIdentification().equals(identification)) {
				return controller;
			}
		}
		return null;
	}
	
	/**
	 * 清空任务队列及视图控制器集合
	 */
	public void destroy(){
		mTaskQueue.clear();
		mControllerList.clear();
		shutdownNow();
	}
	
	/**
	 * 清空任务队列
	 */
	public void taskClear(){
		mTaskQueue.clear();
	}

}
