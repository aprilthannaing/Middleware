package com.middleware.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AbstractController {
    public static final String secretKey = "67f878091a3d0b3d871bdc53f47b15aa74ad9e25";// wipouser,123//SHA1
    private static Logger logger = Logger.getLogger(AbstractController.class);

    public String getMyanmarElement(String content, String element, String remover) {
	int begin = content.indexOf(element);
	content = content.substring(begin, content.length());
	int end = content.indexOf("</dynamic-content>");
	String remainString = content.substring(end, content.length());

	int mStart = remainString.indexOf(remover);
	if (mStart > 0) {
	    int mEnd = remainString.indexOf("]]");
	    if (!remainString.isEmpty() && mEnd > 0)
		return Jsoup.parse(remainString.substring(mStart, mEnd)).text();
	}
	return "";
    }

    public String getEngElement(String content, String element, String remover) {
	int begin = content.indexOf(element);
	String remainString = content.substring(begin, content.length());
	int mStart = remainString.indexOf(remover);
	if (mStart > 0) {
	    int mEnd = remainString.indexOf("]]");
	    if (remainString.isEmpty() || mEnd < 0 || mStart < 0 || mEnd < mStart)
		return "";

	    return Jsoup.parse(remainString.substring(mStart, mEnd)).text();
	}
	return "";
    }

    public String ImageSourceChange(String htmlinput) {

	Document docimage = Jsoup.parse(htmlinput, "", Parser.htmlParser());
	Elements images = docimage.getElementsByTag("img");
	if (images.size() > 0) {
	    for (Element img : images) {
		String imgsrc = img.attr("src");

		// System.out.println("source image...." + imgsrc);
		String imgreplace = "https://myanmar.gov.mm" + imgsrc;
		img.attr("src", imgreplace);
	    }
	}
	return docimage.html();
    }

    public String ImageSourceChangeforanouncement(String htmlinput) {

	Document docimage = Jsoup.parse(htmlinput, "", Parser.htmlParser());
	Elements images = docimage.getElementsByTag("img");
	String imgreplace;
	if (images.size() > 0) {
	    for (Element img : images) {
		String imgsrc = img.attr("src");
		if (img.attr("src").contains("data:image/jpeg;")) {
		    imgreplace = imgsrc;
		} else if (img.attr("src").contains("https://")) {
		    imgreplace = imgsrc;
		} else {
		    imgreplace = "https://myanmar.gov.mm" + imgsrc;
		}
		// System.out.println("source image...." + imgsrc);
		img.attr("src", imgreplace);
	    }
	}
	return docimage.html();
    }

    public void replaceTag(Elements els) {
	ListIterator<Element> iter = els.listIterator();
	while (iter.hasNext()) {
	    Element el = iter.next();
	    replaceTag(el.children());
	    if (el.parentNode() != null)
		el.replaceWith(new TextNode("/" + el.text().trim() + "/"));
	}
    }

    public List<String> removeInvalidString(String[] titleArr) {
	List<String> titleList = new ArrayList<String>();
	for (String title : titleArr) {
	    if (title != null && !title.isEmpty() && title.length() > 1)
		titleList.add(title);
	}
	return titleList;
    }

    public List<String> removeDelimeterFrom(String str) {
	Document doc = Jsoup.parse(str);
	replaceTag(doc.children());
	String[] infos = Jsoup.parse(doc.toString()).text().split("/");
	return removeInvalidString(infos);
    }

    public String getImage(String content) {
	int start = content.indexOf("/image/");
	if (start < 0)
	    return "";
	String remainString = content.substring(start, content.length());
	int end = remainString.indexOf("<");
	return remainString.substring(0, end).startsWith("/")
		? "https://myanmar.gov.mm" + remainString.substring(0, end)
		: remainString.substring(0, end);
    }

    public String getDocumentImage(String content) {
	int start = content.indexOf("/document");
	if (start < 0)
	    return "";
	String remainString = content.substring(start, content.length());
	int end = remainString.indexOf("]]");
	return remainString.substring(0, end).startsWith("/")
		? "https://myanmar.gov.mm" + remainString.substring(0, end)
		: remainString.substring(0, end);
    }

    public String getHttpImage(String content) {
	int start = content.indexOf("http");
	if (start < 0)
	    return "";
	String remainString = content.substring(start, content.length());
	int end = remainString.indexOf("]]");
	return remainString.substring(0, end).startsWith("/")
		? "https://myanmar.gov.mm" + remainString.substring(0, end)
		: remainString.substring(0, end);
    }

    public String getHttpImage2(String content) {
	int start = content.indexOf("http");
	if (start < 0)
	    return "";
	String remainString = content.substring(start, content.length());
	int end = remainString.indexOf("\"");
	return remainString.substring(0, end).startsWith("/")
		? "https://myanmar.gov.mm" + remainString.substring(0, end)
		: remainString.substring(0, end);
    }

    public String convertEntryListToString(List<String> entryList, String input) {
	int index = Integer.parseInt(input);
	int lastIndex = (entryList.size() - 1) - (index * 10 - 10);
	int substract = lastIndex < 9 ? lastIndex : 9;
	int startIndex = lastIndex - substract;

	String info = "";
	for (int i = startIndex; i <= lastIndex; i++)
	    info += entryList.get(i) + ",";
	return info;
    }

    public String getMyanamrContent(String content) {
	int begin = content.indexOf("\"text_area\"");
	content = content.substring(begin, content.length());
	int end = content.indexOf("</dynamic-content>");
	String remainString = content.substring(end, content.length());

	int mStart = remainString.indexOf("<dynamic-content language-id=\"my_MM\">");
	if (mStart > 0) {
	    int mEnd = remainString.lastIndexOf("</dynamic-content>");
	    return Jsoup.parse(remainString.substring(mStart, mEnd)).text().replaceAll("value 1", "");
	}
	return "";
    }

    public String getEngContent(String content) {
	int begin = content.indexOf("\"text_area\"");
	if (begin < 0)
	    return "";
	content = content.substring(begin, content.length());

	int start = content.indexOf("<dynamic-content language-id=\"en_US\">");
	int end = content.indexOf("</dynamic-content>");

	if (start < 0 || end < 0)
	    return "";
	return Jsoup.parse(content.substring(start, end)).text().replaceAll("value 1", "");
    }

    public List<Object> bySize(List<Object> entryList, String input) {
	int index = Integer.parseInt(input);
	int lastIndex = (entryList.size() - 1) - (index * 10 - 10);
	int substract = lastIndex < 9 ? lastIndex : 9;
	int startIndex = lastIndex - substract;

	List<Object> objectList = new ArrayList<Object>();
	for (int i = startIndex; i <= lastIndex; i++)
	    objectList.add(entryList.get(i));
	return objectList;
    }

    public JSONObject isValidComment(@RequestBody JSONObject json) {
	JSONObject resultJson = new JSONObject();
	Object userName = json.get("userName");
	if (userName == null || userName.toString().isEmpty()) {
	    resultJson.put("message", "User Name is Empty!");
	    resultJson.put("status", "0");
	}

	Object userId = json.get("userId");
	if (userId == null || userId.toString().isEmpty() || Long.parseLong(userId.toString()) == 0) {
	    resultJson.put("message", "User Id is Empty!");
	    resultJson.put("status", "0");
	}

	Object body = json.get("body");
	if (body == null || body.toString().isEmpty()) {
	    resultJson.put("message", "Reply is Empty!");
	    resultJson.put("status", "0");
	}

	Object classPK = json.get("classPK");
	if (classPK == null || classPK.toString().isEmpty()) {
	    resultJson.put("message", "classPK is Empty!");
	    resultJson.put("status", "0");
	}

	return resultJson;
    }

    public JSONObject isValidReply(@RequestBody JSONObject json) {
	JSONObject resultJson = new JSONObject();
	Object userName = json.get("userName");
	if (userName == null || userName.toString().isEmpty()) {
	    resultJson.put("message", "User Name is Empty!");
	    resultJson.put("status", "0");
	}

	Object userId = json.get("userId");
	if (userId == null || userId.toString().isEmpty() || Long.parseLong(userId.toString()) == 0) {
	    resultJson.put("message", "User Id is Empty!");
	    resultJson.put("status", "0");
	}

	Object body = json.get("body");
	if (body == null || body.toString().isEmpty()) {
	    resultJson.put("message", "Reply is Empty!");
	    resultJson.put("status", "0");
	}

	Object messageId = json.get("messageId");
	if (messageId == null || messageId.toString().isEmpty()) {
	    resultJson.put("message", "Message Id is Empty!");
	    resultJson.put("status", "0");
	}

	return resultJson;
    }

    public JSONObject isValidFeedback(@RequestBody JSONObject json) {
	JSONObject resultJson = new JSONObject();

	Object name = json.get("name");
	if (name == null || name.toString().isEmpty()) {
	    resultJson.put("message", "User Name is Empty!");
	    resultJson.put("status", "0");
	}

	Object email = json.get("email");
	if (email == null || email.toString().isEmpty()) {
	    resultJson.put("message", "Email is Empty!");
	    resultJson.put("status", "0");
	}

	Object contactNo = json.get("contactNo");
	if (contactNo == null || contactNo.toString().isEmpty()) {
	    resultJson.put("message", "Contact No is Empty!");
	    resultJson.put("status", "0");
	}

	Object feedback = json.get("feedback");
	if (feedback == null || feedback.toString().isEmpty()) {
	    resultJson.put("message", "Feedback is Empty!");
	    resultJson.put("status", "0");
	}

	return resultJson;
    }

    public JSONObject isValidProfile(@RequestBody JSONObject json) {
	JSONObject resultJson = new JSONObject();

	Object name = json.get("name");
	if (name == null || name.toString().isEmpty()) {
	    resultJson.put("message", "User Name is Empty!");
	    resultJson.put("status", "0");
	}

	Object email = json.get("email");
	if (email == null || email.toString().isEmpty()) {
	    resultJson.put("message", "Email is Empty!");
	    resultJson.put("status", "0");
	}

	Object contactNo = json.get("contactNo");
	if (contactNo == null || contactNo.toString().isEmpty()) {
	    resultJson.put("message", "Contact No is Empty!");
	    resultJson.put("status", "0");
	}

	Object feedback = json.get("feedback");
	if (feedback == null || feedback.toString().isEmpty()) {
	    resultJson.put("message", "Feedback is Empty!");
	    resultJson.put("status", "0");
	}

	return resultJson;
    }

    public static String getMessageDescription(String messageCode) {

	String retMsgDesc = "";
	ArrayList<String> arl = new ArrayList<>();
	Resource resource = new ClassPathResource("cbPay.txt");
	// InputStream inputStream = (InputStream) resource.getInputStream();

	// arl = readList(, false);

	try {
	    for (int i = 0; i < arl.size(); i++) {
		if (!arl.get(i).equals("")) {

		    if (!arl.get(i).equals("")) {

			if (arl.get(i).split(":")[0].equalsIgnoreCase(messageCode)) {
			    System.out.println("msgdesc" + arl.get(i).split(":")[1]);
			    retMsgDesc = arl.get(i).split(":")[1];
			}
		    }

		}
	    }

	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	}
	return retMsgDesc;
    }

    public String getEndDateOfMonth(String date) {

	try {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    Date convertedDate = dateFormat.parse(date);
	    Calendar c = Calendar.getInstance();
	    c.setTime(convertedDate);
	    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
	    return c.getTime().toString().split(" ")[2];
	} catch (ParseException e) {
	    logger.error("Error: " + e.getMessage());
	}
	return null;
    }
    
    
    public String getEndDateOfYear(String year) {

  	try {
  	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
  	    Date convertedDate = dateFormat.parse(year + "/12/01");
  	    Calendar c = Calendar.getInstance();
  	    c.setTime(convertedDate);
  	    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
  	    return year + "/12/" + c.getTime().toString().split(" ")[2];
  	} catch (ParseException e) {
  	    logger.error("Error: " + e.getMessage());
  	}
  	return null;
      }

    public int parseMonthToInt(String month) {
	switch (month) {
	case "Jan":
	    return 1;
	case "Feb":
	    return 2;
	case "Mar":
	    return 3;
	case "Apr":
	    return 4;
	case "May":
	    return 5;
	case "June":
	    return 6;
	case "July":
	    return 7;
	case "Aug":
	    return 8;
	case "Sep":
	    return 9;
	case "Oct":
	    return 10;
	case "Nov":
	    return 11;
	case "Dec":
	    return 12;
	}
	return -1;
    }
    
    public String dateTimeFormat() {
    	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	LocalDateTime now = LocalDateTime.now();
    	return dateFormat.format(now);
    }
    
}
