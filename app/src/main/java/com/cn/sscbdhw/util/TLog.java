package com.cn.sscbdhw.util;


import com.stonesun.newssdk.NewsAgent;

public class TLog {

	public static void log(String msg) {
		if (NewsAgent.isDebugMode()) {
			System.out.println("ttt newssdklog:"+msg);
		}
	}

	public static void log(Exception e) {
		if (NewsAgent.isDebugMode()) {
			e.printStackTrace();
		}
	}

	public static void log(String msg, Throwable e) {
		if (NewsAgent.isDebugMode()) {
			System.out.println("错误：" + msg + ",详情:");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}
}
