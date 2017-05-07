package com.spring.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.topo.*;

@EnableMongoRepositories("com.spring.model")
@Controller
public class ReturnTopotoTopo {
	@Autowired
	private TopoRepository topoRepository;
	@RequestMapping(value="/Physical-Topo/topo.do",method=RequestMethod.POST)
    public @ResponseBody Topo handler(){
	       List<Topo> test = new ArrayList<Topo>();
	       Topo tp = new Topo();
	       try {
	    	   test = topoRepository.findAll();
				tp = test.get(0);
			} catch (Exception e) {
				System.out.println("failed to connect database ");
			}
	       return tp;
	}
}       
	       
	     //test  
		  /*FileInputStream fis = null;
	      InputStreamReader isr = null;
	      BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
	       String str = "";
	       String str1 = "";
	      try {

	       fis = new FileInputStream("/home/luyue/topo.json");// FileInputStream
	       // 从文件系统中的某个文件中获取字节
	        isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
	        br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
	       while ((str = br.readLine()) != null) {
	        str1 += str;
	       }
	       // 当读取的一行不为空时,把读到的str的值赋给str1
	       System.out.println(str1);// 打印出str1
	      } catch (FileNotFoundException e) {
	       System.out.println("找不到指定文件");
	      } catch (IOException e) {
	       System.out.println("读取文件失败");
	      } finally {
	       try {
	         br.close();
	         isr.close();
	         fis.close();
	        // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
	       } catch (IOException e) {
	        e.printStackTrace();
	       }
	      }	
		return str1;
	    */   

