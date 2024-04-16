package hongsam.api.member.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import hongsam.api.member.domain.Member;
import hongsam.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j // log찍기 위함
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final MemberRepository memberRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public String uploadFiles(String userId, MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("Error: MultipartFile -> File로 전환이 실패했습니다."));
        return upload(userId, uploadFile, dirName);
    }

    @Transactional
    public String upload(String userId, File uploadFile, String filePath) {
        String fileName = filePath + "/" + userId + uploadFile.getName(); // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // S3로 업로드
        log.info("uploadImageUrl = " + uploadImageUrl);
        removeNewFile(uploadFile);

        // member DB에 변경된 이미지 url 반영
        Member member = memberRepository.findMemberByUUID(userId);
        member.setProfileUrl(uploadImageUrl); // dirtyChecking으로 변경사항 DB반영

        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile =  new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // S3에 저장된 파일 삭제
    public void deleteFile(String fileName) throws IOException {
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (SdkClientException e) {
            throw new IOException("Error deleting file from S3", e);
        }
    }

}
