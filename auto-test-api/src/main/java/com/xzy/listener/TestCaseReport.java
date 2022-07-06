package com.xzy.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;

public class TestCaseReport implements IReporter {
	
	/*private long currentTime = System.currentTimeMillis();
    private SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年-MM月-dd日-HH时mm分ss秒");
    private Date date = new Date(currentTime);
    private String reportdate = formatter.format(date);*/

	private String path = System.getProperty("user.dir") + File.separator + "report" + File.separator + "report.html";

	private String templatePath = System.getProperty("user.dir") + File.separator + "template.html";

	private int testsPass = 0;

	private int testsFail = 0;

	private int testsSkip = 0;

	private String beginTime;

	private long totalTime;

	private String name = "auto-test-api 测试报告";


	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		List<ITestResult> list = new ArrayList<ITestResult>();
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (ISuiteResult suiteResult : suiteResults.values()) {
				ITestContext testContext = suiteResult.getTestContext();
				IResultMap passedTests = testContext.getPassedTests();
				testsPass = testsPass + passedTests.size();
				IResultMap failedTests = testContext.getFailedTests();
				testsFail = testsFail + failedTests.size();
				IResultMap skippedTests = testContext.getSkippedTests();
				testsSkip = testsSkip + skippedTests.size();
				IResultMap failedConfig = testContext.getFailedConfigurations();
				list.addAll(this.listTestResult(passedTests));
				list.addAll(this.listTestResult(failedTests));
				list.addAll(this.listTestResult(skippedTests));
				list.addAll(this.listTestResult(failedConfig));

			}
		}
		this.sort(list);
		this.outputResult(list);
	}

	private ArrayList<ITestResult> listTestResult(IResultMap resultMap) {
		Set<ITestResult> results = resultMap.getAllResults();
		return new ArrayList<ITestResult>(results);
	}

	private void sort(List<ITestResult> list) {
		Collections.sort(list, new Comparator<ITestResult>() {

			public int compare(ITestResult r1, ITestResult r2) {
				if (r1.getInstanceName().hashCode() > r2.getInstanceName().hashCode()) {
					return 1;
				} else {
					return -1;
				}
			}
		});

		Collections.sort(list, new Comparator<ITestResult>() {

			public int compare(ITestResult r1, ITestResult r2) {
				if (r1.getInstanceName().equals(r2.getInstanceName())) {
					if (r1.getStartMillis() > r2.getStartMillis()) {
						return 1;
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			}
		});
	}

	private void outputResult(List<ITestResult> list) {
		try {
			List<ReportInfo> listInfo = new ArrayList<ReportInfo>();
			int caseIndex = -1;
			ITestNGMethod method = null;
			long startTime = 0;
			long endTime = 0;
			for (int i = 0; i < list.size(); i++) {
				ITestResult result = list.get(i);
				if (result.getMethod().equals(method)) {
					caseIndex++;
				} else {
					caseIndex = 1;
				}
				method = result.getMethod();
				if (caseIndex == 1) {
					if (i == list.size() - 1) {
						caseIndex = -1;
					} else {
						ITestResult next = list.get(i + 1);
						if (!next.getMethod().equals(method)) {
							caseIndex = -1;
						}
					}
				}
				String tn = result.getTestContext().getCurrentXmlTest().getParameter("testCase");
				if (i == 0) {
					startTime = result.getStartMillis();
					endTime = result.getEndMillis();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					beginTime = formatter.format(new Date(result.getStartMillis()));
				}
				if (result.getStartMillis() < startTime) {
					startTime = result.getStartMillis();
				}
				if (result.getEndMillis() > endTime) {
					endTime = result.getEndMillis();
				}
				long spendTime = result.getEndMillis() - result.getStartMillis();
				//totalTime += spendTime;
				String status = this.getStatus(result.getStatus());
				List<String> log = Reporter.getOutput(result);
				for (int j = 0; j < log.size(); j++) {
					log.set(j, log.get(j).replaceAll("\"", "\\\\\""));
				}
				Throwable throwable = result.getThrowable();
				if (throwable != null) {
					log.add(throwable.toString().replaceAll("\"", "\\\\\""));
					StackTraceElement[] st = throwable.getStackTrace();
					for (StackTraceElement stackTraceElement : st) {
						log.add(("    " + stackTraceElement).replaceAll("\"", "\\\\\""));
					}
				}
				ReportInfo info = new ReportInfo();
				info.setName(tn);
				info.setSpendTime(spendTime + "ms");
				info.setStatus(status);
				info.setClassName(result.getInstanceName());
				info.setLog(log);
				if (caseIndex > 0) {
					info.setMethodName(result.getName() + (caseIndex <= 9 ? "0" + caseIndex : caseIndex));
				} else {
					info.setMethodName(result.getName());
				}

				info.setDescription(result.getMethod().getDescription()==null?"":result.getMethod().getDescription());
				listInfo.add(info);
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("testName", name);
			result.put("testPass", testsPass);
			result.put("testFail", testsFail);
			result.put("testSkip", testsSkip);
			result.put("testAll", testsPass + testsFail + testsSkip);
			result.put("beginTime", beginTime);
			result.put("totalTime", (endTime - startTime) + "ms");
			result.put("testResult", listInfo);
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			String template = this.read(templatePath);
			//BufferedWriter output = new BufferedWriter();
			//output.write(new String(s.getBytes("gbk"),"utf-8"));
			//BufferedWriter output = new BufferedWriter(new FileWriter(path));
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path)), "utf-8"));
			template = template.replaceFirst("\\$\\{resultData\\}", Matcher.quoteReplacement(gson.toJson(result)));
			output.write(template);
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getStatus(int status) {
		String statusString = null;
		switch (status) {
			case 1:
				statusString = "成功";
				break;
			case 2:
				statusString = "失败";
				break;
			case 3:
				statusString = "跳过";
				break;
			default:
				break;
		}
		return statusString;
	}

	public static class ReportInfo {

		private String name;

		private String className;

		private String methodName;

		private String description;

		private String spendTime;

		private String status;

		private List<String> log;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getSpendTime() {
			return spendTime;
		}

		public void setSpendTime(String spendTime) {
			this.spendTime = spendTime;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public List<String> getLog() {
			return log;
		}

		public void setLog(List<String> log) {
			this.log = log;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	private String read(String path) {
		File file = new File(path);
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		try {
			is = new FileInputStream(file);
			int index = 0;
			byte[] b = new byte[1024];
			while ((index = is.read(b)) != -1) {
				sb.append(new String(b, 0, index));
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
