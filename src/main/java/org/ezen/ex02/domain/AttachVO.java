package org.ezen.ex02.domain;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachVO {

	//pk
	private String fileName;
	private String filePath;
	//fk
	private int articleNo;
}
