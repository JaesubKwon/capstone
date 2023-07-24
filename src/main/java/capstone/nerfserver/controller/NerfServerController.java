package capstone.nerfserver.controller;

import capstone.nerfserver.domain.Post;
import capstone.nerfserver.repository.MemoryPostRepository;
import capstone.nerfserver.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class NerfServerController {

    PostService service = new PostService(new MemoryPostRepository());

    @PostMapping("upload")
    @ResponseBody
    public Post uploadPost(@RequestParam Long userId, @RequestParam String title, @RequestParam String content, @RequestParam Long price, @RequestParam MultipartFile video) {
        Post post = new Post(userId, title, content, price);
        service.upload(post);
        service.saveVideo(post.getId(), video);
        service.runNerf(post.getId());

        return post; //void로?
    }

    @GetMapping("finishNerf")
    @ResponseBody
    public void finishNerf(@RequestParam("id") Long id) {
        Optional<Post> post = service.finishNerf(id);
        post.ifPresentOrElse(p -> System.out.println("id: " + id + " NeRF " + p.getState()), () -> System.out.println("FinishNerfError Wrong id: " + id));
    }

    @GetMapping("postList")
    @ResponseBody
    public List<Post> getPostList() {
        return service.allPosts();
    }

    @GetMapping("post")
    @ResponseBody
    public Post getPost(@RequestParam("id") Long id) {
        return service.findPost(id).orElseGet(() -> {
            Post p = new Post();
            p.setTitle("Wrong ID");
            return p;
        }); //존재하지 않는 글이면 title이 "Wrong ID"인 Post객체 전송
    }

    @GetMapping("mesh")
    @ResponseBody
    public void getMesh(@RequestParam("id") Long id, HttpServletResponse response) {
        if(service.findPost(id).isEmpty()){
            System.out.println("[MeshError] Wrong id(id: " + id + ")");
            return;
        }
        if(service.findPost(id).get().getState() == "waiting"){  //waiting상태인 글의 mesh를 요청하면 body에 아무것도 없이 전송(즉 Content-Length가 0)
            System.out.println("[MeshError] Status is \"waiting\"(id: " + id + ")");
            return;
        }
        String path = service.findMesh(id);

        File obj = new File(path + "mesh.obj");
        File mtl = new File(path + "material_0.mtl");
        File png = new File(path + "material_0.png");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/zip");
        response.addHeader("Content-Disposition", "attachment; filename=\"mesh.zip\"");

        FileOutputStream fos = null;

        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

            // File 객체를 List에 담는다.
            List<File> fileList = Arrays.asList(obj, mtl, png);

            // 루프를 돌며 ZipOutputStream에 파일들을 계속 주입해준다.
            for(File file : fileList) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fileInputStream = new FileInputStream(file);

                StreamUtils.copy(fileInputStream, zipOutputStream);

                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    @GetMapping("mesh")
    @ResponseBody
    public void getMesh(@RequestParam("id") Long id, HttpServletResponse response) {
        if(service.findPost(id).isEmpty()){
            System.out.println("[MeshError] Wrong id(id: " + id + ")");
            return;
        }
        if(service.findPost(id).get().getState() == "waiting"){  //waiting상태인 글의 mesh를 요청하면 body에 아무것도 없이 전송(즉 Content-Length가 0)
            System.out.println("[MeshError] Status is \"waiting\"(id: " + id + ")");
            return;
        }
        String path = service.findMesh(id);

        File obj = new File(path + "mesh.obj");
        File mtl = new File(path + "material_0.mtl");
        File png = new File(path + "material_0.png");

        response.setHeader("Content-Disposition", "attachment;fileName=" + obj.getName());

        try { //반복하면 여러파일 전송 되는 게 아니라, 하나의 파일로 합쳐져셔 보내짐, 여러개 전송은 어쩌지?(getMapping을 3개 따로 구현해서 client에서 mesh요청할때 저 3개모두에 요청을 보내개 하면 되긴할텐데, 그것보단 서버단에서 하는방법을 찾는게 좋을듯)
            FileInputStream fileInputStream = new FileInputStream(obj);
            OutputStream outputStream = response.getOutputStream();

            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
                outputStream.flush();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
     */
}

