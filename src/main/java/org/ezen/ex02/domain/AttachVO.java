package org.ezen.ex02.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AttachVO {

	//pk
	private int id;
	private String fileName;
	private String filePath;
	private Date regDate;
	//fk
	private int articleNo;
}
