package com.wizarpos.atool.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {

	public static void convert(InputStream in, OutputStream out) {
		int len = -1;
		byte[] bs = new byte[1024 * 8];
		try {
			while ((len = in.read(bs)) != -1) {
				out.write(bs, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
