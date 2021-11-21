package com.example.trading.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileUtil {
     public void append(String text) throws IOException
    {
        text = text + System.lineSeparator();
        FileOutputStream outputStream = new FileOutputStream("./transactions.txt", true);
        byte[] strToBytes = text.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }
}
