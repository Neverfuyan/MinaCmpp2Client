package com.cmpp.httpsqs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * 类型描述：消息队列（9218）
 * @author 蔡新鹏
 * @date 日期：2017-01-09  20:22
 * @version 1.0
 */
public class HttpSmsClient {

	private String server = "139.196.160.207"; // 服务器IP地址
	private int port = 9217; // 服务器端口号
	private String charset = "UTF-8"; // HTTP请求字符集
	private int connectTimeout = 0; // 连接超时
	private int readTimeout = 0; // 读超时

	public static final String HTTPSQS_ERROR_PREFIX = "Sms（9218）HTTPSQS_ERROR";

	/**
	 * 建立HTTP Sqs Client
	 * 
	 * @param server
	 *            服务器IP地址
	 * @param port
	 *            服务器端口号
	 * @param charset
	 *            HTTP请求字符集
	 * @param connectTimeout
	 *            连接超时
	 * @param readTimeout
	 *            读超时
	 */
	private static HttpSmsClient instance = null;

	private HttpSmsClient() {
	}

	public static HttpSmsClient getInstance() {
		if (instance == null) {
			instance = new HttpSmsClient();
		}
		return instance;
	}

	/**
	 * 处理HTTP
	 * 
	 * @param urlstr
	 *            请求的URL
	 * @return 服务器的返回信息
	 */
	private String doProcess(String urlstr) {
		URL url = null;
		try {
			url = new URL(urlstr);
		} catch (MalformedURLException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		}

		BufferedReader reader = null;
		try {
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(false);
			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "text/plain;charset=" + charset);

			conn.connect();

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			StringBuilder result = new StringBuilder();

			int i = 0;
			while ((line = reader.readLine()) != null) {
				i++;
				if (i != 1) {
					result.append("\n");
				}
				result.append(line);
			}
			return result.toString();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	/**
	 * 更改指定队列的最大队列数量
	 * 
	 * @param queue_name
	 *            队列名
	 * @param num
	 *            最大队列数
	 * @return 成功: 返回"HTTPSQS_MAXQUEUE_OK" <br>
	 *         错误: "HTTPSQS_MAXQUEUE_CANCEL"-设置没有成功 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String maxqueue(String queue_name, String num) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?name=" + URLEncoder.encode(queue_name, charset) + "&opt=maxqueue&num=" + num;
			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}

	// synctime

	/**
	 * 修改定时刷新内存缓冲区内容到磁盘的间隔时间
	 * 
	 * @param queue_name
	 *            队列名
	 * @param num
	 *            间隔时间(秒)
	 * @return 成功: 返回"HTTPSQS_SYNCTIME_OK" <br>
	 *         错误: "HTTPSQS_SYNCTIME_CANCEL"-设置没有成功 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String synctime(String queue_name, String num) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?name=" + URLEncoder.encode(queue_name, charset) + "&opt=synctime&num=" + num;
			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}

	/**
	 * 重置指定队列
	 * 
	 * @param queue_name
	 *            队列名
	 * @return 成功: 返回"HTTPSQS_RESET_OK" <br>
	 *         错误: "HTTPSQS_RESET_ERROR"-设置没有成功 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String reset(String queue_name) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?name=" + URLEncoder.encode(queue_name, charset) + "&opt=reset";

			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}

	/**
	 * 查看指定队列位置点的内容
	 * 
	 * @param queue_name
	 *            队列名
	 * @param pos
	 *            位置
	 * @return 成功: 返回指定位置的队列内容,错误返回已"HTTPSQS_ERROR"开头的字符串 <br>
	 *         错误: "HTTPSQS_ERROR_NOFOUND"-指定的消息不存在 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String view(String queue_name, String pos) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset + "&name=" + URLEncoder.encode(queue_name, charset) + "&opt=view&pos=" + pos;

			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}

	/**
	 * 查看队列状态
	 * 
	 * @param queue_name
	 *            队列名
	 * @return 成功: 返回队列信息 <br>
	 *         错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String status(String queue_name) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?name=" + URLEncoder.encode(queue_name, charset) + "&opt=status";

			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}

	/**
	 * 已JSO格式,查看队列状态
	 * 
	 * @param queue_name
	 *            队列名
	 * @return 成功:
	 *         {"name":"队列名","maxqueue":最大队列数,"putpos":队列写入点值,"putlap":队列写入点值圈数
	 *         ,"getpos":队列获取点值,"getlap":队列获取点值圈数,"unread":未读消息数} <br>
	 *         错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String statusJson(String queue_name) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?name=" + URLEncoder.encode(queue_name, charset) + "&opt=status_json";

			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}

	/**
	 * 出队列
	 * 
	 * @param queue_name
	 *            队列名
	 * @return 成功: 出队列的消息内容 <br>
	 *         错误: "HTTPSQS_GET_END"-队列为空 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String get(String queue_name) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset + "&name=" + URLEncoder.encode(queue_name, charset) + "&opt=get";

			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}
	
	public String getT(String queue_name,String tServer,String tPort) {
		String result = null;
		try {
			String urlstr = "http://" + tServer + ":" + tPort + "/?charset=" + this.charset + "&name=" + URLEncoder.encode(queue_name, charset) + "&opt=get";
			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}

	/**
	 * 未出队列大小
	 * 
	 * @param queue_name
	 *            队列名
	 * @param data
	 *            入队列的消息内容
	 * @return 成功: 返回字符串"HTTPSQS_PUT_OK" <br>
	 *         错误: "HTTPSQS_PUT_ERROR"-入队列错误; "HTTPSQS_PUT_END"-队列已满 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String total(String queue_name) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset + "&name=" + URLEncoder.encode(queue_name, charset) + "&opt=total";
			result = this.doProcess(urlstr);
			return result;
		} catch (Throwable ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}

	/**
	 * 入队列
	 * 
	 * @param queue_name
	 *            队列名
	 * @param data
	 *            入队列的消息内容
	 * @return 成功: 返回字符串"HTTPSQS_PUT_OK" <br>
	 *         错误: "HTTPSQS_PUT_ERROR"-入队列错误; "HTTPSQS_PUT_END"-队列已满 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String put(String queue_name, String data) {
		String urlstr;
		URL url;
		try {
			urlstr = "http://" + this.server + ":" + this.port + "/?name=" + URLEncoder.encode(queue_name, charset) + "&opt=put";
			url = new URL(urlstr);
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		} catch (MalformedURLException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		}
		URLConnection conn;

		OutputStreamWriter writer = null;
		try {
			conn = url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "text/plain;charset=" + charset);

			conn.connect();

			writer = new OutputStreamWriter(conn.getOutputStream(), charset);
			writer.write(URLEncoder.encode(data, charset));
			writer.flush();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
				}
			}
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			return reader.readLine();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	
	public String putT(String queue_name, String data,String tServer,String tPort) {
		String urlstr;
		URL url;
		try {
			urlstr = "http://" + tServer + ":" + tPort + "/?name=" + URLEncoder.encode(queue_name, charset) + "&opt=put";
			url = new URL(urlstr);
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		} catch (MalformedURLException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		}
		URLConnection conn;

		OutputStreamWriter writer = null;
		try {
			conn = url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "text/plain;charset=" + charset);

			conn.connect();

			writer = new OutputStreamWriter(conn.getOutputStream(), charset);
			writer.write(URLEncoder.encode(data, charset));
			writer.flush();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
				}
			}
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			return reader.readLine();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	/**
	 * 放入key值
	 * 
	 * @param key key值
	 *            
	 * @param data 消息内容
	 * @return 成功: 返回字符串"HTTPSQS_PUT_OK" <br>
	 *         错误: "HTTPSQS_PUT_ERROR"-入队列错误; "HTTPSQS_PUT_END"-队列已满 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String setnx(String key, String data) {
		String urlstr;
		URL url;
		try {
			urlstr = "http://" + this.server + ":" + this.port + "/?name=" + URLEncoder.encode(key, charset) + "&opt=setnx";
			url = new URL(urlstr);
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		} catch (MalformedURLException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		}
		URLConnection conn;

		OutputStreamWriter writer = null;
		try {
			conn = url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "text/plain;charset=" + charset);

			conn.connect();

			writer = new OutputStreamWriter(conn.getOutputStream(), charset);
			writer.write(URLEncoder.encode(data, charset));
			writer.flush();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
				}
			}
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			return reader.readLine();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	/**
	 * 取数据
	 * 
	 * @param key key名
	 * @return 成功: 消息内容 <br>
	 *         错误: "HTTPSQS_GET_END"-队列为空 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String getnx(String key) {
		String result = null;
		try {
			String urlstr = "http://" + this.server + ":" + this.port + "/?charset=" + this.charset + "&name=" + URLEncoder.encode(key, charset) + "&opt=getnx";

			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}
	/**
	 * 计数据
	 * 
	 * @param key key名
	 * @param data 增减值
	 * @return 成功: 消息内容 <br>
	 *         错误: "HTTPSQS_GET_END"-队列为空 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String calcby(String key,String data) {
		String urlstr;
		URL url;
		try {
			urlstr = "http://" + this.server + ":" + this.port + "/?name=" + URLEncoder.encode(key, charset) + "&opt=calcby";
			url = new URL(urlstr);
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		} catch (MalformedURLException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		}
		URLConnection conn;

		OutputStreamWriter writer = null;
		try {
			conn = url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "text/plain;charset=" + charset);

			conn.connect();

			writer = new OutputStreamWriter(conn.getOutputStream(), charset);
			writer.write(URLEncoder.encode(data, charset));
			writer.flush();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
				}
			}
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			return reader.readLine();
		} catch (IOException e) {
			return HTTPSQS_ERROR_PREFIX + ":" + e.getMessage();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	
	/**
	 * 已JSO格式,查看队列状态
	 * 
	 * @param queue_name
	 *            队列名
	 * @return 成功:
	 *         {"name":"队列名","maxqueue":最大队列数,"putpos":队列写入点值,"putlap":队列写入点值圈数
	 *         ,"getpos":队列获取点值,"getlap":队列获取点值圈数,"unread":未读消息数} <br>
	 *         错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String statusJson(String ip,String port,String queue_name) {
		String result = null;
		try {
			String urlstr = "http://" + ip + ":" + port + "/?name=" + URLEncoder.encode(queue_name, charset) + "&opt=status_json";

			result = this.doProcess(urlstr);
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}
	
	/**
	 * 取数据从内存(取了数据不清除)
	 * 
	 * @param key key名
	 * @return 成功: 消息内容 <br>
	 *         错误: "HTTPSQS_GET_END"-队列为空 <br>
	 *         其他错误: 返回已"HTTPSQS_ERROR"开头的字符串
	 */
	public String getpr(String ip,String port,String key) {
		String result = null;
		try {
			String urlstr = "http://" + ip + ":" + port + "/?charset=" + this.charset + "&name=" + URLEncoder.encode(key, charset) + "&opt=getpr";
			result = this.doProcess(urlstr);
			if("".equals(result)){
				return "HTTPSQS_END";
			}
			return result;
		} catch (UnsupportedEncodingException ex) {
			return HTTPSQS_ERROR_PREFIX + ":" + ex.getMessage();
		}
	}
}