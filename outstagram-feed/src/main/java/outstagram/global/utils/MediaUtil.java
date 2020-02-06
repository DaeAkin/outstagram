package outstagram.global.utils;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;


public class MediaUtil {
    private static List<String> permittedFileExtension;

    static {
        permittedFileExtension = new ArrayList<>();

        permittedFileExtension.add("JPG");
        permittedFileExtension.add(MediaType.IMAGE_JPEG_VALUE);
        permittedFileExtension.add(MediaType.IMAGE_PNG_VALUE);

        permittedFileExtension.add("MOV");
        permittedFileExtension.add("AVI");
    }




    public static boolean containsImageExtension(String extension){
        return permittedFileExtension.contains(extension.toUpperCase());
    }

    private static String saveFile(String uploadPath, MultipartFile file, String imageIndex) {

        File uploadPathDir = new File(uploadPath);

        if ( !uploadPathDir.exists() ){
            uploadPathDir.mkdirs();
        }


        String originalFileName = file.getOriginalFilename();
        String fileExtension = getExtension(originalFileName);

        if(!MediaUtil.containsImageExtension(fileExtension)){
            throw new RuntimeException("Invalid_File_Extension!:"+fileExtension);
        }

        String genId = UUID.randomUUID().toString();
        genId = genId.replace("-", "");

        String saveFileName = genId + "-" + imageIndex + "." + fileExtension;

        String savePath = calcPathByDate(uploadPath);

        File target = new File(uploadPath + savePath, saveFileName);
        try {
            FileCopyUtils.copy(file.getBytes(), target);
        } catch(IOException e){
            throw new RuntimeException("Failed_To_Copy_File!");
        }
        return makeFilePath(uploadPath, savePath, saveFileName);
    }


    /**
     *
     * @param file 저장할 파일
     * @param path 파일 경로
     * @param sequence 파일명에 포함될 sequence 번호
     * @return 최종 저장 파일 경로
     */
    public static String saveImageFile(MultipartFile file, String path, String sequence){
        if(file == null || file.isEmpty()){
            throw new RuntimeException("emptyFileRequest!!");
        }

        String savedFilePath = MediaUtil.saveFile(path, file, sequence);

        if (savedFilePath.toCharArray()[0] == '/') {
            savedFilePath = savedFilePath.substring(1);
        }
        return savedFilePath;
    }

    /**
     * 파일이름으로부터 확장자를 반환
     *
     * @param fileName
     *            확장자를 포함한 파일 명
     * @return 파일 이름에서 뒤의 확장자 이름만을 반환
     */
    public static String getExtension(String fileName) {
        int dotPosition = fileName.lastIndexOf('.');

        if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
            return fileName.substring(dotPosition + 1);
        } else {
            return "";
        }
    }

    public static Resource getResourceFromFilename(String filename, String path) throws Exception{
        Path filePath = Paths.get(path).toAbsolutePath().normalize();

        Path file = filePath.resolve(filename);

        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new Exception("Could not read file: " + filename);
        }
    }

    private static String calcPathByDate(String uploadPath) {

        Calendar cal = Calendar.getInstance();

        String yearPath = File.separator + cal.get(Calendar.YEAR);
        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

        makeDir(uploadPath, yearPath, monthPath, datePath);


        return datePath;
    }

    private static void makeDir(String uploadPath, String... paths) {

        if (new File(paths[paths.length - 1]).exists()) {
            return;
        }

        for (String path : paths) {
            File dirPath = new File(uploadPath + path);

            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
        }
    }

    private static String makeFilePath(String uploadPath, String path, String fileName){
        String filePath = uploadPath + path + File.separator + fileName;
        return filePath.substring(uploadPath.length()).replace(File.separatorChar, '/');
    }

}
