import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 这个类表示一个 Web 服务器<br>
 * 这个 Web 服务器可以处理对指定目录的静态资源的请求, 该目录包括由公有静态变量 {@code final String WEB_ROOT}
 * 指明的目录及其所有子目录
 */

class HttpServer {
	public static final String WEB_ROOT=System.getProperty("user.dir")+File.separator+"webroot";

	private static final String SHUTDOWN_COMMAND="/SHUTDOWN";

	private boolean shutdown=false;

    /**
     * 创建一个 {@code HttpServer} 实例, 然后调用其 {@link HttpServer#await()} 方法
     *
     * @param args null
     */
	public static void main(String[] args) {
		HttpServer server=new HttpServer();
		server.await();
	}

    /**
     * 在指定端口上等待 HTTP 请求, 对其进行处理, 然后发送响应信息回客户端<br>
     * 在接收到关闭命令前, 它会保持等待状态
     */
	public void await(){
		ServerSocket serverSocket=null;
		int port=8080;
		try {
			serverSocket=new ServerSocket(port,1,InetAddress.getByName("127.0.0.1"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		while(!shutdown){
			Socket socket=null;
			InputStream input=null;
			OutputStream output=null;
			try {
				socket=serverSocket.accept();
				input=socket.getInputStream();
				output=socket.getOutputStream();
				//create Request object and parse
				Request request=new Request(input);
				request.parse();

				//create Response object
				Response response=new Response(output);
				response.setRequest(request);
				response.sendStaticResource();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}