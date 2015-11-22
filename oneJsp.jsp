<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.io.File,java.io.IOException,java.util.Set,java.lang.management.GarbageCollectorMXBean,java.lang.management.ManagementFactory,java.lang.management.ThreadMXBean;" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>oneJsp</title>
</head>
<body>
disk space:

<% getDiskSpace(); %>
<BR>
Thread details:
<% getThreadDetails(); %>
<BR>
head Details:
<% getHeapDetails(); %>
<BR>

GC stats:
<% getGCStats(); %>
<BR>
</body>
</html>

<%! 

public String getDiskSpace(){
	File file = new File(".");
	long totalSpace = file.getTotalSpace(); //total disk space in bytes.
	long usableSpace = file.getUsableSpace(); ///unallocated / free disk space in bytes.
	long freeSpace = file.getFreeSpace(); //unallocated / free disk space in bytes.

	System.out.println(" === Partition Detail ==="+file.getAbsolutePath());
	StringBuilder sb = new StringBuilder();
	sb.append("{\"dirname\":\""+file.getAbsolutePath()+"\",");
	System.out.println(" === bytes ===");
	sb.append("\"total_space\":\""+totalSpace+"\",");
	sb.append("\"free_space\":\""+freeSpace+"\",");
	sb.append("\"unused_space\":\""+usableSpace+"\"}");
	System.out.println("Total size : " + totalSpace + " bytes");
	System.out.println("Space free : " + usableSpace + " bytes");
	System.out.println("Space free : " + freeSpace + " bytes");
	return sb.toString();
}

public String getThreadDetails(){
	ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
	long[] blockThreadIds = threadBean.findDeadlockedThreads();
	 long[] threadIds = threadBean.getAllThreadIds();
	 StringBuilder sb = new StringBuilder();
	 sb.append("{\"RunningThread\":[ ");
	for(int i=0;i<threadIds.length;i++){
		sb.append("{\"name\":\""+threadBean.getThreadInfo(threadIds[i]).getThreadName()+"\",");
		
		sb.append("\"cpu_time\":\""+threadBean.getThreadCpuTime(threadIds[i])+"\",");
		sb.append("\"state\":\""+threadBean.getThreadInfo(threadIds[i]).getThreadState()+"\"}");
		
		if(i>0){
			sb.append(",");
		}
	
	}
	sb.append("],\"blocked_thread\":[");
	
	for(int i=0;i<blockThreadIds.length;i++){
		sb.append("{\"name\":\""+threadBean.getThreadInfo(blockThreadIds[i]).getThreadName()+"\",");
		sb.append("\"state\":\""+threadBean.getThreadInfo(threadIds[i]).getThreadState()+"\"}");
		if(i>0){
			sb.append(",");
		}
	}
	sb.append("]}");
	
	/*Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
	Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
	StringBuilder sb = new StringBuilder();
	sb.append("[");
	for(int i=0;i<threadArray.length;i++){
		Thread t =threadArray[i];
		sb.append("{\"threadName\":\""+t.getName() +"\",");
		sb.append("\"threadId\":\""+t.getId() +"\",");
		sb.append("\"threadState\":\""+t.getState() +"\"}");
		if(i>0)
		sb.append(",");
	}
	sb.append("]");
*/		return sb.toString();
}

public String getHeapDetails(){
	int mb = 1024*1024;
    StringBuilder sb = new StringBuilder();
    //Getting the runtime reference from system
    Runtime runtime = Runtime.getRuntime();
     
    System.out.println("##### Heap utilization statistics [MB] #####");
    sb.append("{\"totalMemory\":\""+runtime.totalMemory() +"\","); 
    sb.append("\"freeMemory\":\""+runtime.freeMemory() +"\","); 
    sb.append("\"usedMemory\":\""+(runtime.totalMemory() - runtime.freeMemory()) +"\"}");
   return sb.toString();
}


public String getGCStats() {
    long totalGarbageCollections = 0;
    long garbageCollectionTime = 0;

    for(GarbageCollectorMXBean gc :
            ManagementFactory.getGarbageCollectorMXBeans()) {

        long count = gc.getCollectionCount();

        if(count >= 0) {
            totalGarbageCollections += count;
        }

        long time = gc.getCollectionTime();

        if(time >= 0) {
            garbageCollectionTime += time;
        }
    }
    StringBuilder sb = new StringBuilder();
    sb.append("{\"totalCollection\":\""+totalGarbageCollections+"\",");
    sb.append("{\"totalTime\":\""+garbageCollectionTime+"\"}");
    return sb.toString();
 }
%>
