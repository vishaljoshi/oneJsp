/**
 *
 * 
 * History:
 * ----------------------------------------------------------------------------------------------------
 * Author                   | Date                |        Description                                |
 * ----------------------------------------------------------------------------------------------------
 *  Vishal Joshi            |Feb 03, 2014         | Creation                                          |
 * ----------------------------------------------------------------------------------------------------
 */

package com.health;

import java.io.File;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Health {
	
	

	private static final String OS_NAME = "OS.NAME";
	private static final String OS_VERSION = "OS.VERSION";
	private static final String OS_ARCH = "OS.ARCH";
	private static final String OS_PROCESSOR = "OS.PROCESSOR";

	private static final String CPU_VM_LOAD = "CPU.VM.LOAD";
	private static final String CPU_SYSTEM_LOAD = "CPU.SYSTEM.LOAD";
	private static final String CPU_PHYSICAL_FREE_MEMORY = "PHYSICAL.MEMORY.FREE";
	private static final String CPU_PHYSICAL_TOTAL_MEMORY = "PHYSICAL.MEMORY.TOTAL";
	private static final String CPU_SWAP_FREE = "SWAP.FREE";
	private static final String CPU_SWAP_TOTAL = "SWAP.TOTAL";

	private static final String VM_NAME = "VM.NAME";
	private static final String VM_VERSION = "VM.VERSION";
	private static final String VM_VENDOR = "VM.VENDOR";
	private static final String VM_START_TIME = "VM.START_TIME";
	private static final String VM_UP_TIME = "VM.UP_TIME";
	private static final String VM_INPUTS = "VM.INPUTS";

	private static final String RUNTIME_PROCESSORS = "RUNTIME.PROCESSORS";
	private static final String RUNTIME_MEMORY_FREE = "HEAP.MEMORY.FREE";
	private static final String RUNTIME_MEMORY_MAX = "HEAP.MEMORY.MAX";
	private static final String RUNTIME_MEMORY_TOTAL = "HEAP.MEMORY.TOTAL";
	private static final String RUNTIME_TIME_IN_MS = "HEAP.TIME.IN.MS";

	private static final String DISK_DIR_NAME = "DISK.DIR.NAME";
	private static final String DISK_TOTAL_SPACE = "DISK.TOTAL.SPACE";
	private static final String DISK_USABLE_SPACE = "DISK.USABLE.SPACE";
	private static final String DISK_FREE_SPACE = "DISK.FREE.SPACE";
	private static final String DISK_FILEDESC_MAX_COUNT = "DISK.FILEDESC.MAX.COUNT";
	private static final String DISK_FILEDESC_OPEN_COUNT = "DISK.FILEDESC.OPEN.COUNT";

	private static final String THREAD_NAME = "THREAD_NAME";
	private static final String THREAD_CPU = "THREAD_CPU";
	private static final String THREAD_STATE = "THREAD_STATE";
	private static final String ALL_RUNNING_THREAD = "ALL_RUNNING_THREAD";
	private static final String ALL_BLOCKED_THREAD = "ALL_BLOCKED_THREAD";
	private static final String THREAD_PEAK_COUNT = "THREAD_PEAK_COUNT";
	private static final String THREAD_STARTED_COUNT = "THREAD_STARTED_COUNT";
	private static final String THREAD_DAEMON_COUNT = "THREAD_DAEMON_COUNT";
	private static final String THREAD_COUNT = "THREAD_COUNT";

	private static final String GC_TOTAL_COLLECTION = "GC_TOTAL_COLLECTION";
	private static final String GC_TOTAL_TIME = "GC_TOTAL_TIME";

	private static final long BYTES_PER_MB = 1048576;

	private static final long MILISEC_PER_SEC = 1000;
	private static final long NANO_PER_MILISEC = 1000000;
	private static final Runtime runtime = Runtime.getRuntime();
	private static final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
	private static final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
	
	public static SystemStats getRealTimeData(){
		
		SystemStats stats = new SystemStats();
		Map<String, Object> map = new HashMap<String, Object>();
		stats.setProcessors( runtime.availableProcessors());
		stats.setHeapFree(runtime.freeMemory() / BYTES_PER_MB);
		stats.setHeapMax(runtime.maxMemory() / BYTES_PER_MB);
		stats.setHeapTotal(runtime.totalMemory() / BYTES_PER_MB);
		stats.setThreadCount(threadBean.getThreadCount());
		
		if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
			final com.sun.management.OperatingSystemMXBean sunOsbean = (com.sun.management.OperatingSystemMXBean) osBean;
			stats.setSystemLoad(sunOsbean.getSystemCpuLoad());
			stats.setVmLoad(sunOsbean.getProcessCpuLoad());
		}
		return stats;
		
	}
	

	public static DataHolder getOsDetails() {
		DataHolder dataHolder = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		map.put(OS_NAME, osBean.getName());
		map.put(OS_VERSION, osBean.getVersion());
		map.put(OS_ARCH, osBean.getArch());
		map.put(OS_PROCESSOR, osBean.getAvailableProcessors());

		dataHolder = new DataHolder(map);
		return dataHolder;
	}

	public static DataHolder getCPUDetails() {
		DataHolder dataHolder = null;
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		Map<String, Object> map = new HashMap<String, Object>();
		if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
			final com.sun.management.OperatingSystemMXBean sunOsbean = (com.sun.management.OperatingSystemMXBean) osBean;

			map.put(CPU_VM_LOAD, sunOsbean.getProcessCpuLoad()*100+" %");
			map.put(CPU_SYSTEM_LOAD, sunOsbean.getSystemCpuLoad()*100+" %");

			map.put(CPU_PHYSICAL_FREE_MEMORY, sunOsbean.getFreePhysicalMemorySize() / BYTES_PER_MB);
			map.put(CPU_PHYSICAL_TOTAL_MEMORY, sunOsbean.getTotalPhysicalMemorySize() / BYTES_PER_MB);
			map.put(CPU_SWAP_FREE, sunOsbean.getFreeSwapSpaceSize() / BYTES_PER_MB);
			map.put(CPU_SWAP_TOTAL, sunOsbean.getTotalSwapSpaceSize() / BYTES_PER_MB);

		}
		/*if (osBean instanceof com.sun.management.UnixOperatingSystemMXBean) {
			final com.sun.management.UnixOperatingSystemMXBean unixOsbean = (com.sun.management.UnixOperatingSystemMXBean) osBean;

			map.put(CPU_VM_LOAD, unixOsbean.getProcessCpuLoad());
			map.put(CPU_SYSTEM_LOAD, unixOsbean.getSystemCpuLoad());

			map.put(CPU_PHYSICAL_FREE_MEMORY, unixOsbean.getFreePhysicalMemorySize() / BYTES_PER_MB);
			map.put(CPU_PHYSICAL_TOTAL_MEMORY, unixOsbean.getTotalPhysicalMemorySize() / BYTES_PER_MB);
			map.put(CPU_SWAP_FREE, unixOsbean.getFreeSwapSpaceSize() / BYTES_PER_MB);
			map.put(CPU_SWAP_TOTAL, unixOsbean.getTotalSwapSpaceSize() / BYTES_PER_MB);

		}*/

		dataHolder = new DataHolder(map);
		return dataHolder;
	}

	public static DataHolder getVmDetails() {
		DataHolder dataHolder = null;
		Map<String, Object> map = new HashMap<String, Object>();
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();

		map.put(VM_NAME, bean.getVmName());
		map.put(VM_VENDOR, bean.getVmVendor());
		map.put(VM_VERSION, bean.getSpecVersion());
		map.put(VM_START_TIME, new Date(bean.getStartTime() / MILISEC_PER_SEC));
		map.put(VM_UP_TIME, bean.getUptime() / MILISEC_PER_SEC);
		map.put(VM_INPUTS, bean.getInputArguments());
		dataHolder = new DataHolder(map);
		return dataHolder;
	}

	public static DataHolder getRunTimeDetails() {
		DataHolder dataHolder = null;
		Map<String, Object> map = new HashMap<String, Object>();
		final Runtime runtime = Runtime.getRuntime();

		map.put(RUNTIME_PROCESSORS, runtime.availableProcessors());
		map.put(RUNTIME_MEMORY_FREE, runtime.freeMemory() / BYTES_PER_MB);
		map.put(RUNTIME_MEMORY_MAX, runtime.maxMemory() / BYTES_PER_MB);
		map.put(RUNTIME_MEMORY_TOTAL, runtime.totalMemory() / BYTES_PER_MB);
		map.put(RUNTIME_TIME_IN_MS, System.currentTimeMillis());
		dataHolder = new DataHolder(map);
		return dataHolder;
	}

	public static DataHolder getDiskSpace() {
		File file = new File(".");
		// total disk space in bytes.
		long totalSpace = file.getTotalSpace();
		// unallocated free disk space in bytes.
		long usableSpace = file.getUsableSpace();
		// unallocated free disk space in bytes.
		long freeSpace = file.getFreeSpace();
		DataHolder dataHolder = null;
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(DISK_DIR_NAME, file.getAbsolutePath());
		map.put(DISK_TOTAL_SPACE, totalSpace / BYTES_PER_MB);
		map.put(DISK_FREE_SPACE, freeSpace / BYTES_PER_MB);
		map.put(DISK_USABLE_SPACE, usableSpace / BYTES_PER_MB);
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

		if (osBean instanceof com.sun.management.UnixOperatingSystemMXBean) {
			final com.sun.management.UnixOperatingSystemMXBean unixOsbean = (com.sun.management.UnixOperatingSystemMXBean) osBean;

			map.put(DISK_FILEDESC_MAX_COUNT, unixOsbean.getMaxFileDescriptorCount());
			map.put(DISK_FILEDESC_OPEN_COUNT, unixOsbean.getOpenFileDescriptorCount());

		}

		dataHolder = new DataHolder(map);
		return dataHolder;
	}

	public static DataHolder getThreadDetails(String threadName) {
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		long[] blockThreadIds = threadBean.findDeadlockedThreads();
		long[] threadIds = threadBean.getAllThreadIds();
		DataHolder dataHolder = null;
		Map<String, Object> map = null;
		Map<String, Object> finalMap = new HashMap<String, Object>();
		List<Map<String, Object>> threadArray = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < threadIds.length; i++) {
			if (threadName == null || threadBean.getThreadInfo(threadIds[i]).getThreadName().indexOf(threadName) != -1) {
				map = new HashMap<String, Object>();
				map.put(THREAD_STATE, "" + threadBean.getThreadInfo(threadIds[i]).getThreadId());
				map.put(THREAD_NAME, "" + threadBean.getThreadInfo(threadIds[i]).getThreadName());
				map.put(THREAD_CPU, "" + threadBean.getThreadCpuTime(threadIds[i]) / NANO_PER_MILISEC);
				threadArray.add(map);
			}

		}

		List<Map<String, Object>> blockThreadArray = new ArrayList<Map<String, Object>>();
		if (blockThreadIds != null) {
			for (int i = 0; i < blockThreadIds.length; i++) {
				if (threadName == null || threadBean.getThreadInfo(blockThreadIds[i]).getThreadName().indexOf(threadName) != -1) {
					map = new HashMap<String, Object>();
					map.put(THREAD_STATE, "" + threadBean.getThreadInfo(blockThreadIds[i]).getThreadId());
					map.put(THREAD_NAME, "" + threadBean.getThreadInfo(blockThreadIds[i]).getThreadName());
					map.put(THREAD_CPU, "" + threadBean.getThreadCpuTime(blockThreadIds[i]) / NANO_PER_MILISEC);
					blockThreadArray.add(map);

				}

			}
		}
		finalMap.put(ALL_RUNNING_THREAD, threadArray);
		finalMap.put(ALL_BLOCKED_THREAD, blockThreadArray);

		finalMap.put(THREAD_PEAK_COUNT, threadBean.getPeakThreadCount());
		finalMap.put(THREAD_STARTED_COUNT, threadBean.getTotalStartedThreadCount());
		finalMap.put(THREAD_DAEMON_COUNT, threadBean.getDaemonThreadCount());
		finalMap.put(THREAD_COUNT, threadBean.getThreadCount());

		dataHolder = new DataHolder(finalMap);
		return dataHolder;
	}

	public static DataHolder getGCStats() {
		long totalGarbageCollections = 0;
		long garbageCollectionTime = 0;
		DataHolder dataHolder = null;
		
		for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
			
			long count = gc.getCollectionCount();

			if (count >= 0) {
				totalGarbageCollections += count;
			}

			long time = gc.getCollectionTime();

			if (time >= 0) {
				garbageCollectionTime += time;
			}

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(GC_TOTAL_COLLECTION, totalGarbageCollections);
		map.put(GC_TOTAL_TIME, garbageCollectionTime);
		dataHolder = new DataHolder(map);

		return dataHolder;
	}

	public static class DataHolder {

		public Map<String, Object> map = null;
		public List<Map<String, Object>> array = null;

		public DataHolder(Map<String, Object> map) {
			this.map = map;
		}

		public DataHolder(List<Map<String, Object>> array) {
			this.array = array;
		}

		public String getJsonObject(Map<String, Object> map) {
			StringBuilder sb = new StringBuilder();
			if (map != null) {

				sb.append("{");

				Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				for (int i = 0; it.hasNext(); i++) {
					Entry<String, Object> entry = it.next();
					if (i > 0) {
						sb.append(",");
					}
					if (entry.getValue() instanceof String) {
						sb.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"");
					} else if (entry.getValue() instanceof HashMap) {
						sb.append("\"" + entry.getKey() + "\":" + getJsonObject((Map) entry.getValue()));
					} else if (entry.getValue() instanceof ArrayList) {
						sb.append("\"" + entry.getKey() + "\":" + getJsonArray((List) entry.getValue()));
					} else {
						sb.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"");
					}

				}

				sb.append("}");

			}
			return sb.toString();
		}

		public String getJsonArray(List<Map<String, Object>> array) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			if (array != null) {
				for (int i = 0; i < array.size(); i++) {
					if (i > 0) {
						sb.append(",");
					}
					sb.append(getJsonObject(array.get(i)));
				}
			}
			sb.append("]");
			return sb.toString();
		}

		@Override
		public String toString() {

			if (array != null) {
				return getJsonArray(array);
			} else if (map != null) {
				return getJsonObject(map);
			}
			return "";
		}

	}
	public static class  SystemStats{
		int processors;
		long heapMax;
		long heapFree;
		long heapTotal;
		int threadCount;
		double systemLoad;
		double vmLoad;
		public int getProcessors() {
			return processors;
		}
		public void setProcessors(int processors) {
			this.processors = processors;
		}
		public long getHeapMax() {
			return heapMax;
		}
		public void setHeapMax(long heapMax) {
			this.heapMax = heapMax;
		}
		public long getHeapFree() {
			return heapFree;
		}
		public void setHeapFree(long heapFree) {
			this.heapFree = heapFree;
		}
		public long getHeapTotal() {
			return heapTotal;
		}
		public void setHeapTotal(long heapTotal) {
			this.heapTotal = heapTotal;
		}
		public int getThreadCount() {
			return threadCount;
		}
		public void setThreadCount(int threadCount) {
			this.threadCount = threadCount;
		}
		public double getSystemLoad() {
			return systemLoad;
		}
		public void setSystemLoad(double systemLoad) {
			this.systemLoad = systemLoad;
		}
		public double getVmLoad() {
			return vmLoad;
		}
		public void setVmLoad(double vmLoad) {
			this.vmLoad = vmLoad;
		}
		@Override
		public String toString() {
			return "SystemStats [processors=" + processors + ", heapMax="
					+ heapMax + ", heapFree=" + heapFree + ", heapTotal="
					+ heapTotal + ", threadCount=" + threadCount
					+ ", systemLoad=" + systemLoad + ", vmLoad=" + vmLoad + "]";
		}
	}

}
