import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 将静态资源发送给浏览器
 */


// HTTP Response = Status-Line
//              *(( general-header | response-header | entity-header ) CRLF)
//              CRLF
//              [message-body]
//              Status-Line=Http-Version SP Status-Code SP Reason-Phrase CRLF

class Response {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    /**
     * Response 对象在 {@link HttpServer#await()} 方法中通过传入套接字中获取的 {@code OutputStream} 来创建
     *
     * @param output {@code OutputStream output}
     */
    public Response(OutputStream output) {
        this.output = output;
    }

    /**
     * 接收一个 {@link #Respones(OutputStream)} 对象为参数
     *
     * @param request {@link #Respones(OutputStream)}
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * 用于发送一个静态资源到浏览器, 如 Html 文件
     *
     * @throws IOException
     */
    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(HttpServer.WEB_ROOT, request.getUrl());
            if (file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, BUFFER_SIZE);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            } else {
                // file not found
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type:text/html\r\n"
                        + "Content-Length:23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
                output.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
