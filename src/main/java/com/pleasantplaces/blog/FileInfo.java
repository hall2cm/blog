package com.pleasantplaces.blog;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by cohall on 3/27/2017.
 */
public class FileInfo {

    private String location;
    //private long fileSize;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //public long getFileSize() {
    //    return fileSize;
   // }

    //public void setFileSize(long fileSize) {
    //    this.fileSize = fileSize;
    //}
}
