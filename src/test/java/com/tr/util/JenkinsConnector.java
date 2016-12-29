package com.tr.util;

public class JenkinsConnector {
	public static String getJobName() {
		return System.getenv("JOB_NAME");
	}

	public static String getBuildNo() {
		return System.getenv("BUILD_NUMBER");
	}

	public static String getBuildId() {
		return System.getenv("BUILD_ID");
	}

	public static String getBuildUrl() {
		return System.getenv("BUILD_URL");
	}

	public static String getBuildTag() {
		return System.getenv("BUILD_TAG");
	}

	public static String getJenkinsUrl() {
		return System.getenv("JENKINS_URL");
	}

}
