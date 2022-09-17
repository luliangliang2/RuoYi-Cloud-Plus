package com.ruoyi.common.core.utils.file;

import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import com.ruoyi.common.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩包处理类
 *
 * @author Bleachtred
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZipUtils extends ZipUtil {

    private final static Integer TEMP_SIZE = 2048;

    /**
     * 压缩方法 返回前端下载（支持 本地文件/目录 + oss路径 混合）
     * @param downloadFilename 压缩包文件名 test.zip
     * @param files 文件列表
     * @author Bleachtred
     */
    public static void zipFilePip(String downloadFilename, List<String> files, HttpServletResponse response) {
        //转换中文否则可能会产生乱码
        try {
            FileUtils.setAttachmentResponseHeader(response, downloadFilename);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        zipFilePip(files, response);
    }

    /**
     * 压缩方法 返回前端下载（支持 本地文件/目录 + oss路径 混合）
     * @param files 文件列表
     * @author Bleachtred
     */
    private static void zipFilePip(List<String> files, HttpServletResponse response) {

        try(WritableByteChannel out = Channels.newChannel(response.getOutputStream())) {
            Pipe pipe = Pipe.open();
            //异步任务
            CompletableFuture.runAsync(() -> runTask(pipe, files));

            //获取读通道
            try (ReadableByteChannel readableByteChannel = pipe.source()) {
                ByteBuffer buffer = ByteBuffer.allocate(TEMP_SIZE);
                while (readableByteChannel.read(buffer) >= 0) {
                    buffer.flip();
                    out.write(buffer);
                    buffer.clear();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void runTask(Pipe pipe, List<String> files) {

        try(ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
            WritableByteChannel out = Channels.newChannel(zos)) {
            for (String file : files) {
                taskFunction(zos, file, out, "");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void taskFunction(ZipOutputStream zos, File file, WritableByteChannel out, String base) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            base = StringUtils.isEmpty(base) ? file.getName() + "/" : base + "/";

            if (files == null || files.length == 0){
                return;
            }
            for (File file1 : files) {
                taskFunction(zos, file1, out, base + file1.getName());
            }
        } else {
            base = StringUtils.isEmpty(base) ? file.getName() : base;

            zos.putNextEntry(new ZipEntry(base));

            try(FileInputStream fis = new FileInputStream(file.getAbsolutePath())){
                FileChannel fileChannel = fis.getChannel();

                fileChannel.transferTo(0, fileChannel.size(), out);

                fileChannel.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    private static void taskFunction(ZipOutputStream zos, String file, WritableByteChannel out, String base) throws IOException {
        // 网络文件
        if (file.contains("http")) {

            base = StringUtils.isEmpty(base) ? FileUtils.getName(file) : base;

            zos.putNextEntry(new ZipEntry(base));

            byte[] bytes = HttpUtil.downloadBytes(file);

            out.write(ByteBuffer.wrap(bytes));

        }else {
            taskFunction(zos, new File(file), out, "");
        }
    }
}
