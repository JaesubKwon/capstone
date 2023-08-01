package capstone.nerfserver.service;

import capstone.nerfserver.domain.MeshInfo;
import capstone.nerfserver.domain.Post;
import capstone.nerfserver.repository.MemoryPostRepository;
import capstone.nerfserver.repository.MeshInfoRepository;
import capstone.nerfserver.repository.PostRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

public class PostService {

    public PostService(PostRepository postRepository, MeshInfoRepository meshInfoRepository) {
        this.postRepository = postRepository;
        this.meshInfoRepository = meshInfoRepository;
    }

    private final PostRepository postRepository;
    private final MeshInfoRepository meshInfoRepository;
    private final String scriptPath = "/workspace/test.sh";//~~.sh
    private final String videoPath = "/workspace/video/";
    private final String imagePath = "/workspace/image/";
    private final String meshPath = "/workspace/result/mesh/";

    //글수정(구현x)


    /**
     * 글 등록
     * @param post
     * @return
     */
    public Long upload(Post post){
        return postRepository.save(post).getId();
    }

    /**
     * NeRF 실행
     * @param id
     */
    public void runNerf(Long id){
        try {
            Runtime.getRuntime().exec(scriptPath + " " + id);
            System.out.println("start running " + scriptPath + " " + id);
        } catch (IOException e) {
            System.out.println("runNerf error");
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * NeRF 완료
     * @param id
     * @return
     */
    public Optional<Post> finishNerf(Long id){
        return postRepository.updateState(id, "done");
    }

    /**
     * 글 목록 조회
     * @return
     */
    public List<Post> allPosts(){
        return postRepository.findAll();
    }

    /**
     * 글 내용 조회
     * @param postId
     * @return
     */
    public Optional<Post> findPost(Long postId){
        return postRepository.findById(postId);
    }

    /**
     * MeshInfo 조회
     * @param postId
     * @return
     */
    public Optional<MeshInfo> findMeshInfo(Long postId){
        return meshInfoRepository.findById(postId);
    }

    /**
     * mesh파일 주소 조회(폴더 주소)
     * @param postId
     * @return
     */
    public String findMesh(Long postId){
        return meshPath + postId + "/";
    }

    public String findImage(Long postId){
        return imagePath + postId + "/";
    }
    public void saveMeshInfo(Long id, MeshInfo meshInfo){
        meshInfoRepository.save(id, meshInfo);
    }

    public void saveVideo(Long id, MultipartFile video) {
        if(!video.isEmpty()){ //empty인 경우 처리 필요?
            try {
                video.transferTo(new File(videoPath + id + ".mp4"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveImages(Long id, MultipartFile[] images, Long numberOfImages) {
        File folder = new File(imagePath + id);
        if(!folder.exists()){
            try{
                folder.mkdir();
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }

        for(int index=0; index<numberOfImages; index++){
            if(!images[index].isEmpty()){ //불필요?
                try {
                    images[index].transferTo(new File(folder.getPath() + "/" + index + ".png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}
