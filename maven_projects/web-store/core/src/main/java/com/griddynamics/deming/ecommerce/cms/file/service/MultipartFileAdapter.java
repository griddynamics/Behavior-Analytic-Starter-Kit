package com.griddynamics.deming.ecommerce.cms.file.service;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class MultipartFileAdapter implements MultipartFile {

    private String name;
    private String originalFileName;
    private byte[] bytes;

    public MultipartFileAdapter(InputStream inputStream, String path) throws IOException {
        this.name = path;
        this.originalFileName = path;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, byteArrayOutputStream);

        this.bytes = byteArrayOutputStream.toByteArray();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFileName;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException();
    }
}
