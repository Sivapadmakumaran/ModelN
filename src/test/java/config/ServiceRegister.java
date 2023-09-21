package config;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


import com.cognizant.framework.selenium.SeleniumTestParameters;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public final class ServiceRegister {

	/**
	 * Map to hold any run time object while test execution
	 */
	private Map<String, Object> register = new ConcurrentHashMap<String, Object>();

	// private String name;

	private Map<String, List<SeleniumTestParameters>> failedCases = new HashMap<>();

	private static ServiceRegister REGISTER = new ServiceRegister();

	private static AtomicInteger count = new AtomicInteger(1);

	private final static String[] workers = new String[] { "Jackson", "John", "Gabriel", "Jacob", "James", "Michael",
			"Jayden", "Tyler", "Joseph", "Mathew", "Ethan", "Adam", "Angel", "Daniel", "Ryan", "Alexandar", "Wood",
			"David", "Hunter", "Jose", "Emma", "Madison", "Emily", "Sophia", "Grace", "Elizabeth", "Ashley", "Addision",
			"Hailey", "Olivia", "Nicole", "Lily", "Genesis", "Beaker", "Mark", "Graham", "Hannah", "Rhode", "Natalie",
			"Charles" };

	private static Set<String> workerSet = new HashSet<String>();

	
	
	private static List<String> sessions = new ArrayList<String>();
	
	private ServiceRegister() {
	}

	public static ServiceRegister getInstance() {
		if (null == REGISTER) {
			REGISTER = new ServiceRegister();
			return REGISTER;
		} else {
			return REGISTER;
		}
	}

//	public synchronized void setName(String name) {
//		this.name = name;
//	}

	/**
	 * function to store run time object
	 * 
	 * @param serviceName
	 * @param serviceClass
	 */
	public synchronized void putService(String serviceName, Object serviceClass) {
		register.put(serviceName, serviceClass);
	}

//	public synchronized String getName() {
//		return name;
//	}

	public synchronized Object getService(String id, String serviceName) {

		if (register.containsKey(id + serviceName)) {
			return register.get(id + serviceName);
		} else {
			return null;
		}
	}

	public synchronized void removeService(String id, String serviceName) {
		if (register.containsKey(id + serviceName)) {
			Object removedItem = register.remove(id + serviceName);
			
		}
	}

	public synchronized void addFailedCases(String suiteName, SeleniumTestParameters parameters) {
		List<SeleniumTestParameters> param = failedCases.get(suiteName);

		if (param == null) {
			List<SeleniumTestParameters> prm = new ArrayList<SeleniumTestParameters>();
			prm.add(parameters);
			failedCases.put(suiteName, prm);

		} else {
			if (!(param.contains(parameters))) {
				param.add(parameters);
			}
		}
	}

	public Map<String, List<SeleniumTestParameters>> getFailedCases() {
		return this.failedCases;
	}

	public synchronized void removeAll(String id) {
		register.entrySet().removeIf((e) -> e.getKey().contains(id));
	}

	public synchronized void remove() {
		register.clear();
	}

	public synchronized String getWorkerName() {

		int currentCount = count.getAndIncrement();

		return "Thread" + currentCount;
	}

	public synchronized void removeService(String workerName) {
		String sessionId = getService(workerName, "Session").toString();
		removeService(workerName, "StopExecutionFlag");
		removeService(workerName, "CurrentSuite");
		removeService(workerName, "CurrentTestCase");
		removeService(workerName, "Failure");
		removeService(sessionId, "WD");
		removeService(workerName, "Session");
	}
	
	public synchronized static void putSession(String sessionId) {
		sessions.add(sessionId);
	}
	
	public static void clearSessions() {
		
	}
}
