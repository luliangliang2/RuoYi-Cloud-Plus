package com.ruoyi.common.core.utils.file;

import cn.hutool.http.HttpUtil;
import com.ruoyi.common.core.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩包处理类
 *
 * @author Bleachtred
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZipUtils {

    private final static Integer TEMP_SIZE = 2048;

    /**
     * 压缩方法
     * @param zipFile 压缩到本地的zip文件
     * @param files 需要压缩的文件列表
     * @author Bleachtred
     */
    public static void zipFilePip(File zipFile, File... files){
        zipFilePip(zipFile, Arrays.asList(files));
    }

    /**
     * 压缩方法
     * @param zipFile 压缩到本地的zip文件名称带后缀
     * @param files 需要压缩的文件列表
     * @author Bleachtred
     */
    public static void zipFilePip(String zipFile, String ... files){
        List<File> files1 = new ArrayList<>();
        for(String fileStr : files){
            files1.add(new File(fileStr));
        }
        zipFilePip(new File(zipFile),files1);
    }

    /**
     * 压缩方法
     * @param zipFile 压缩到本地的zip文件名称带后缀
     * @param files 需要压缩的文件列表
     * @author Bleachtred
     */
    public static void zipFilePip(String zipFile, List<String> files){
        List<File> files1 = new ArrayList<>();
        for(String fileStr : files){
            files1.add(new File(fileStr));
        }
        zipFilePip(new File(zipFile),files1);
    }

    /**
     * 压缩方法 返回前端下载（支持 本地文件/目录 + oss路径 混合）
     * @param files 文件列表
     * @author Bleachtred
     */
    public static void zipFilePip(String downloadFilename, List<String> files, HttpServletResponse response) {
        try {
            //转换中文否则可能会产生乱码
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
        zipFilePip(files, response);
    }

    /**
     * 压缩方法 返回前端下载（支持 本地文件/目录 + oss路径 混合）
     * @param files 文件列表
     * @author Bleachtred
     */
    public static void zipFilePip(List<String> files, HttpServletResponse response) {

        try(WritableByteChannel out = Channels.newChannel(response.getOutputStream())) {
            Pipe pipe = Pipe.open();
            //异步任务
            CompletableFuture.runAsync(() -> runTask(pipe, files));

            //获取读通道
            ReadableByteChannel readableByteChannel = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(TEMP_SIZE);
            while (readableByteChannel.read(buffer)>= 0) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 压缩方法
     * @param zipFile 压缩到本地的zip文件
     * @param files 需要压缩的文件列表
     * @author Bleachtred
     */
    public static void zipFilePip(File zipFile, List<File> files) {

        try(WritableByteChannel out = Channels.newChannel(Files.newOutputStream(zipFile.toPath()))) {
            Pipe pipe = Pipe.open();
            //异步任务
            CompletableFuture.runAsync(() -> runLocalTask(pipe,files));

            //获取读通道
            ReadableByteChannel readableByteChannel = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(TEMP_SIZE);
            while (readableByteChannel.read(buffer)>= 0) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void runLocalTask(Pipe pipe, List<File> files) {

        try(ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
            WritableByteChannel out = Channels.newChannel(zos)) {
            for (File file : files) {
                taskFunction(zos, file, out, "");
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
            InputStream fis = new ByteArrayInputStream(Objects.requireNonNull(bytes));

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                out.write(ByteBuffer.wrap(buffer, 0, len));
            }

            fis.close();
        }else {
            taskFunction(zos, new File(file), out, "");
        }
    }
}
