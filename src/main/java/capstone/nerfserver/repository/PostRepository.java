package capstone.nerfserver.repository;

import capstone.nerfserver.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);
    Optional<Post> updateTitle(Long id, String title);
    Optional<Post> updateContent(Long id, String content);
    Optional<Post> updatePrice(Long id, Long price);
    Optional<Post> updateState(Long id, String state);
    List<Post> findByUserId(Long userId);
    Optional<Post> findById(Long id);
    List<Post> findAll();
}
