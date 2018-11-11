// package mogeko.httpserver;

import java.io.InputStream;

/**
 * Request 类表示一个 HTTP 请求
 *
 * @author Mogeko
 * @version v0.0.1
 */
class Request {
	private InputStream input;

	private String url;

	/**
	 * 通过传递 {@code InputStream} 对象来创建 {@link #Requset(InputStream)} 对象<br>
	 *
	 * @param input {@code InputStream} 对象
	 */
	public Request(InputStream input){
		this.input = input;
	}

	/**
	 * 解析 HTTP 请求的原始数据, parse() 方法会调用私有方法 {@link #parseUrl(String)} 来解析 HTTP 请求的 Url<br>
	 *
	 * 通过调用 InputStream 对象中的 {@link InputStream#read()} 方法来读取 HTTP 请求的原始数据。<br>
	 * 将处理好的 Url 写回到成员变量 {@code String url} 中
	 */
	public void parse(){
		//Read a set of characters from the socket
		StringBuffer request = new StringBuffer(2048);
		int i;
		byte[] buffer = new byte[2048];
		try {
			i = input.read(buffer);
		} catch (Exception e) {
			e.printStackTrace();
			i = -1;
		}
		for(int j = 0;j<i;j++){
			request.append((char)buffer[j]);
		}
		System.out.print(request.toString());
		url = parseUrl(request.toString());
	}

	/**
	 * 处理 Url
	 *
	 * @param requestString 经过 {@link #parse()} 处理的 {@code String requestString}
	 * @return {@code String url}
	 */
	private String parseUrl(String requestString){
		int index1, index2;
		index1 = requestString.indexOf(" ");
		if (index1 != -1) {
			index2 = requestString.indexOf(" ", index1 + 1);
			if (index2 > index1) {
				return requestString.substring(index1 + 1, index2);
			}
		}
		return null;
	}

	/**
	 * 返回请求的 Url
	 * @return {@code String url}
	 */
	public String getUrl(){
		return this.url;
	}
}