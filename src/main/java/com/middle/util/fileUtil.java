package com.middle.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.middleware.controller.AbstractController;

public class fileUtil extends AbstractController{
	public static ArrayList<String> readList(String strPath, boolean stem) {
		ArrayList<String> arlTemp = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(strPath));
			String str;
			while ((str = in.readLine()) != null) {
				String strOK = str;
				if (stem) {
					strOK = str;
				}
				if (!str.equals(""))
					arlTemp.add(strOK);
			}
			in.close();
		} catch (Exception e) {
			System.out.println("Error @ readList" + e.getMessage());
		}
		return arlTemp;
	}
}
