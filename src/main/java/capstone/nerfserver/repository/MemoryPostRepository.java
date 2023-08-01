package capstone.nerfserver.repository;

import capstone.nerfserver.domain.Post;

import java.util.*;
import java.util.stream.Collectors;

public class MemoryPostRepository implements PostRepository{

    private static Map<Long, Post> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Post save(Post post) {
        post.setId(++sequence);
        store.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> updateTitle(Long id, String title) {
        Post post = store.get(id);
        if(post == null){
            return Optional.empty();
        }
        post.setTitle(title);
        return Optional.ofNullable(post);

    }

    @Override
    public Optional<Post> updateContent(Long id, String content) {
        Post post = store.get(id);
        if(post == null){
            return Optional.empty();
        }
        post.setContent(content);
        return Optional.ofNullable(post);
    }

    @Override
    public Optional<Post> updatePrice(Long id, Long price) {
        Post post = store.get(id);
        if(post == null){
            return Optional.empty();
        }
        post.setPrice(price);
        return Optional.ofNullable(post);
    }

    @Override
    public Optional<Post> updateNumberOfImages(Long id, Long numberOfImages) {
        Post post = store.get(id);
        if(post == null){
            return Optional.empty();
        }
        post.setNumberOfImages(numberOfImages);
        return Optional.ofNullable(post);
    }


    @Override
    public Optional<Post> updateState(Long id, String state) {
        Post post = store.get(id);
        if(post == null){
            return Optional.empty();
        }
        post.setState(state);
        return Optional.ofNullable(post);
    }

    @Override
    public List<Post> findByUserId(Long userId) {
        return store.values().stream().filter(post -> post.getUserId() == userId).collect(Collectors.toList());
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
