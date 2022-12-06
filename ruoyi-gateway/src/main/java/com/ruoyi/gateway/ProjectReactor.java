package com.ruoyi.gateway;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static java.io.File.separator;

/**
 * 项目修改器，一键替换 Maven 的 groupId、artifactId，项目的 package 等
 * <p>
 * 通过修改 groupIdNew、artifactIdNew、projectBaseDirNew 三个变量
 *
 */
@Slf4j
public class ProjectReactor {

    private static final String PACKAGE_NAME = "com.ruoyi";
	private static final String PACKAGE_NAME_NEW = "com.cloudsaas.framework";
    private static final String GROUP_ID = "com.ruoyi";
	private static final String GROUP_ID_NEW = "com.cloudsaas";
    private static final String ARTIFACT_ID = "ruoyi-cloud-plus";
	private static final String ARTIFACT_ID_NEW = "teas";
    private static final String PROJECT_MODULE = "ruoyi";
	private static final String PROJECT_MODULE_NEW = "teas";
    private static final String TITLE = "RuoYi";
	private static final String TITLE_NEW = "Teas";
    private static final String PROJECT_DIR = "RuoYi-Cloud-Plus";
	private static final String PROJECT_DIR_NEW = "teas-cloud"; // 一键改名后，“新”项目所在的目录

    /**
     * 白名单文件，不进行重写，避免出问题
     */
    private static final List<String> WHITE_FILE_TYPES = Arrays.asList( "gif", "jpg", "svg", "png", // 图片
        "eot", "woff2", "ttf", "woff" ); // 字体

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String projectBaseDir = getProjectBaseDir();
        log.info( "[main][原项目路劲改地址 ({})]", projectBaseDir );

        String projectBaseDirNew = projectBaseDir.replaceAll(PROJECT_DIR, PROJECT_DIR_NEW);
        log.info( "[main][新项目路径地址 ({})]", projectBaseDirNew );

        // 获得需要复制的文件
        log.info( "[main][开始获得需要重写的文件，预计需要 10-20 秒]" );
        Collection<File> files = listFiles( projectBaseDir );
        log.info( "[main][需要重写的文件数量：{}，预计需要 15-30 秒]", files.size() );
        // 写入文件
        files.forEach( file -> {
            // 如果是白名单的文件类型，不进行重写，直接拷贝
            String fileType = FileTypeUtil.getType( file );
            if (WHITE_FILE_TYPES.contains( fileType )) {
                copyFile( file, projectBaseDir, projectBaseDirNew, PACKAGE_NAME_NEW, ARTIFACT_ID_NEW, PROJECT_MODULE_NEW);
                return;
            }
            // 如果非白名单的文件类型，重写内容，在生成文件
            String content = replaceFileContent( file, GROUP_ID_NEW, ARTIFACT_ID_NEW, PACKAGE_NAME_NEW, TITLE_NEW, PROJECT_MODULE_NEW);
            writeFile( file, content, projectBaseDir, projectBaseDirNew, PACKAGE_NAME_NEW, ARTIFACT_ID_NEW, PROJECT_MODULE_NEW);
        } );
        log.info( "[main][重写完成]共耗时：{} 秒", (System.currentTimeMillis() - start) / 1000 );
    }

    private static String getProjectBaseDir() {
        String baseDir = System.getProperty( "user.dir" );
        if (StrUtil.isEmpty( baseDir )) {
            throw new NullPointerException( "项目基础路径不存在" );
        }
        return baseDir;
    }

    private static Collection<File> listFiles(String projectBaseDir) {
        Collection<File> files = FileUtils.listFiles( new File( projectBaseDir ), null, true );
        // 移除 IDEA、Git 自身的文件、Node 编译出来的文件
        files = files.stream()
            .filter( file -> !file.getPath().contains( separator + "target" + separator )
                && !file.getPath().contains( separator + "node_modules" + separator )
                && !file.getPath().contains( separator + ".idea" + separator )
                && !file.getPath().contains( separator + ".git" + separator )
                && !file.getPath().contains( separator + "dist" + separator )
                    && !file.getPath().contains( separator + ".svn" + separator )
                && !file.getPath().contains( ".iml" )
                && !file.getPath().contains( ".html.gz" ) )
            .collect( Collectors.toList() );
        return files;
    }

    private static String replaceFileContent(File file, String groupIdNew, String artifactIdNew, String packageNameNew, String titleNew, String projectModuleNew) {
        String content = FileUtil.readString( file, StandardCharsets.UTF_8 );
        // 如果是白名单的文件类型，不进行重写
        String fileType = FileTypeUtil.getType( file );
        if (WHITE_FILE_TYPES.contains( fileType )) {
            return content;
        }
        // 执行文件内容都重写
        return content.replaceAll( PACKAGE_NAME, packageNameNew )
            .replaceAll( GROUP_ID, groupIdNew )
            .replaceAll( ARTIFACT_ID, artifactIdNew )// 必须放在最后替换，因为 ARTIFACT_ID 太短！
//            .replaceAll( StrUtil.upperFirst( ARTIFACT_ID ), StrUtil.upperFirst( artifactIdNew ) )
            .replaceAll( TITLE, titleNew )
            .replaceAll(PROJECT_MODULE, projectModuleNew );
    }

    private static void writeFile(File file, String fileContent, String projectBaseDir, String projectBaseDirNew, String packageNameNew, String artifactIdNew, String projectModuleNew) {
        String newPath = buildNewFilePath( file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew, projectModuleNew );
        FileUtil.writeUtf8String( fileContent, newPath );
    }

    private static void copyFile(File file, String projectBaseDir, String projectBaseDirNew, String packageNameNew, String artifactIdNew, String projectModuleNew) {
        String newPath = buildNewFilePath( file, projectBaseDir, projectBaseDirNew, packageNameNew, artifactIdNew, projectModuleNew );
        FileUtil.copyFile( file, new File( newPath ) );
    }

    private static String buildNewFilePath(File file, String projectBaseDir, String projectBaseDirNew, String packageNameNew, String artifactIdNew, String projectModuleNew) {
        return file.getPath().replace( projectBaseDir, projectBaseDirNew ) // 新目录
            .replace( PACKAGE_NAME.replaceAll( "\\.", Matcher.quoteReplacement( separator ) ),
                packageNameNew.replaceAll( "\\.", Matcher.quoteReplacement( separator ) ) )
            .replace( ARTIFACT_ID, artifactIdNew ) //
            .replaceAll( StrUtil.upperFirst( ARTIFACT_ID ), StrUtil.upperFirst( artifactIdNew ) )
            .replaceAll(PROJECT_MODULE, projectModuleNew );
    }

}
