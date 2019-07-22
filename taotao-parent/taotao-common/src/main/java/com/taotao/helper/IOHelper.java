package com.taotao.helper;

import java.io.BufferedReader;

/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * IO帮助类
 * <p>Title: IOHelper</p>
 * <p>@Description:TODO</p>
 * <p>Company: www.chenhaitao.com</p>	
 * @author chenhaitao
 * @date:2019年6月3日下午8:37:37
 * @version:1.0
 */
public interface IOHelper {

    /**
     * 将数据写入文件
     * @param fileName
     * @param bytes
     * @throws IOException
     */
    static void writeFile(String fileName, byte[] bytes) throws IOException {
        if (fileName != null) {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

    /**
     * 读取输入流中的字节
     * @param inputStream
     * @return
     * @throws IOException
     */
    static byte[] readStreamBytes(InputStream inputStream) throws IOException {
        byte[] cache = new byte[2048];
        int len;
        byte[] bytes = new byte[0];
        while ((len = inputStream.read(cache)) > 0) {
            byte[] temp = bytes;
            bytes = new byte[bytes.length + len];
            System.arraycopy(temp, 0, bytes, 0, temp.length);
            System.arraycopy(cache, 0, bytes, temp.length, len);
        }
        if (bytes.length == 0) {
            return null;
        }
        return bytes;
    }

    static byte[] readStreamBytesAndClose(InputStream inputStream) throws IOException {
        byte[] bytes = readStreamBytes(inputStream);
        inputStream.close();
        return bytes;
    }
    /**
     * 根据url读取指定文件内容
     * @param url
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    static Map<String, Object> readUrlStream(URL url) throws UnsupportedEncodingException, IOException{
    	Map<String, Object> map  = new HashMap<>(); 
    	BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        while (true) {
            String line = r.readLine();
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }

            int i = line.indexOf('=');
            if (i > 0) {
                String name = line.substring(0, i).trim();
                String clazz = line.substring(i + 1).trim();
                map.put(name, clazz);
            }
        }
        return map;
    }
    
}
