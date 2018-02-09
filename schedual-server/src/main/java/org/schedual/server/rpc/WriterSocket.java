package org.schedual.server.rpc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 写数据的socket
 * @author Administrator
 *
 */
public class WriterSocket {
	
	Socket socket;
	
	PrintWriter writer;
	
	private long lastReciveTime=System.currentTimeMillis();

	public WriterSocket(Socket socket) {
		super();
		this.socket = socket;
		try {
			writer=getWriter(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public long getLastReciveTime() {
		return lastReciveTime;
	}


	public void setLastReciveTime(long lastReciveTime) {
		this.lastReciveTime = lastReciveTime;
	}


	/**
	 * socket写数据,涉及多线程使用该outputStream
	 * @param s
	 * @throws Exception 
	 */
	public void writeData(String s) throws Exception{
		if(socket==null || socket.isClosed() || writer==null ){
			throw new Exception("socket is closed");
		}
		writer.println(s);
	}
	
	private static PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }
	
	public void close(){
		
		if(writer!=null){
			try {
				writer.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(socket!=null){
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
