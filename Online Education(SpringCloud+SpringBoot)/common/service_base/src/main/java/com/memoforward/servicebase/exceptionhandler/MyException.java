package com.memoforward.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // lombok生成有参构造
@NoArgsConstructor // lombok生成无参构造
public class MyException extends RuntimeException{

    private Integer code;

    private String msg;
}
