package org.dromara.common.encrypt.filter;

import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.dromara.common.encrypt.utils.EncryptUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 加密响应参数包装类
 *
 * @author Michelle.Chung
 */
public class EncryptResponseBodyWrapper extends HttpServletResponseWrapper {

<<<<<<< HEAD
    private final ByteArrayOutputStream byteArrayOutputStream;
    private final ServletOutputStream servletOutputStream;
    private final PrintWriter printWriter;
=======
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final Charset RESPONSE_CHARSET = StandardCharsets.UTF_8;

    private final ByteArrayOutputStream byteArrayOutputStream;
    private final ServletOutputStream servletOutputStream;
    private PrintWriter printWriter;
>>>>>>> future/3.X

    public EncryptResponseBodyWrapper(HttpServletResponse response) throws IOException {
        super(response);
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.servletOutputStream = this.getOutputStream();
<<<<<<< HEAD
        this.printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));
=======
>>>>>>> future/3.X
    }

    @Override
    public PrintWriter getWriter() {
<<<<<<< HEAD
=======
        if (printWriter == null) {
            printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream, RESPONSE_CHARSET));
        }
>>>>>>> future/3.X
        return printWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (servletOutputStream != null) {
            servletOutputStream.flush();
        }
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    @Override
    public void reset() {
        byteArrayOutputStream.reset();
    }

    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return byteArrayOutputStream.toByteArray();
    }

    public String getContent() throws IOException {
        flushBuffer();
<<<<<<< HEAD
        return byteArrayOutputStream.toString();
=======
        return byteArrayOutputStream.toString(RESPONSE_CHARSET);
>>>>>>> future/3.X
    }

    /**
     * 获取加密内容
     *
     * @param servletResponse response
     * @param publicKey       RSA公钥 (用于加密 AES 秘钥)
     * @param headerFlag      请求头标志
     * @return 加密内容
     * @throws IOException
     */
    public String getEncryptContent(HttpServletResponse servletResponse, String publicKey, String headerFlag) throws IOException {
        // 生成秘钥
        String aesPassword = RandomUtil.randomString(32);
        // 秘钥使用 Base64 编码
        String encryptAes = EncryptUtils.encryptByBase64(aesPassword);
        // Rsa 公钥加密 Base64 编码
        String encryptPassword = EncryptUtils.encryptByRsa(encryptAes, publicKey);

        // 设置响应头
        servletResponse.setHeader(headerFlag, encryptPassword);
<<<<<<< HEAD
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
=======
        servletResponse.setCharacterEncoding(RESPONSE_CHARSET.name());
>>>>>>> future/3.X

        // 获取原始内容
        String originalBody = this.getContent();
        // 对内容进行加密
<<<<<<< HEAD
        return EncryptUtils.encryptByAes(originalBody, aesPassword);
=======
        String encryptContent = EncryptUtils.encryptByAes(originalBody, aesPassword);
        servletResponse.setContentLengthLong(encryptContent.getBytes(RESPONSE_CHARSET).length);
        return encryptContent;
>>>>>>> future/3.X
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {

            }

            @Override
            public void write(int b) throws IOException {
                byteArrayOutputStream.write(b);
            }

            @Override
            public void write(byte[] b) throws IOException {
                byteArrayOutputStream.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                byteArrayOutputStream.write(b, off, len);
            }
        };
    }

<<<<<<< HEAD
=======
    private String generateAesPassword() {
        byte[] bytes = new byte[24];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

>>>>>>> future/3.X
}
