package com.security.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author: fuhongxing
 * @Date: 2021/3/16
 **/
public class SockerClient {
    public static void main(String[] args) throws IOException {
        Socket client = null;
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            client = new Socket();
            client.connect(new InetSocketAddress("localhost",9000));
            printWriter = new PrintWriter(client.getOutputStream(),true);
            printWriter.println("hello");
            printWriter.flush();
            //读取服务器返回的信息并进行输出
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("来自服务器的信息是：" + bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            printWriter.close();
            bufferedReader.close();
            client.close();
        }
    }
}
