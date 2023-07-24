package capstone.nerfserver.repository;

import capstone.nerfserver.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryPostRepositoryTest {

    MemoryPostRepository repository = new MemoryPostRepository();

    @AfterEach
    void afterEach(){
        repository.clearStore();
    }

    @Test
    void save() {
        //given
        Post post = new Post();

        //when
        repository.save(post);

        //then
        Post result = repository.findById(post.getId()).get();
        Assertions.assertThat(post).isEqualTo(result);
    }

    @Test
    void updateTitle() {
        //given
        Post post = new Post();
        Long id = repository.save(post).getId();

        //when
        repository.updateTitle(id, "update title");

        //then
        String result = repository.findById(post.getId()).get().getTitle();
        Assertions.assertThat("update title").isEqualTo(result);
    }

    @Test
    void updateContent() {
        //given
        Post post = new Post();
        Long id = repository.save(post).getId();

        //when
        repository.updateContent(id, "update content");

        //then
        String result = repository.findById(post.getId()).get().getContent();
        Assertions.assertThat("update content").isEqualTo(result);
    }

    @Test
    void updatePrice() {
        //given
        Post post = new Post();
        Long id = repository.save(post).getId();

        //when
        repository.updatePrice(id, 237L);

        //then
        Long result = repository.findById(post.getId()).get().getPrice();
        Assertions.assertThat(237L).isEqualTo(result);
    }

    @Test
    void updateState() {
        //given
        Post post = new Post();
        Long id = repository.save(post).getId();

        //when
        repository.updateState(id, "update state");

        //then
        String result = repository.findById(post.getId()).get().getState();
        Assertions.assertThat("update state").isEqualTo(result);
    }

    @Test
    void findByUserId() {
        //given
        Post post1 = new Post();
        post1.setUserId(26L);
        repository.save(post1);
        Post post2 = new Post();
        post2.setUserId(26L);
        repository.save(post2);

        //when
        List<Post> list = repository.findByUserId(post1.getUserId());

        //then
        Post result = list.get(0);
        Assertions.assertThat(post1).isEqualTo(result);
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        //given
        Post post = new Post();
        repository.save(post);

        //when
        Post result = repository.findById(post.getId()).get();

        //then
        Assertions.assertThat(post).isEqualTo(result);
    }

    @Test
    void findAll() {
        //given
        Post post1 = new Post();
        repository.save(post1);
        Post post2 = new Post();
        repository.save(post2);

        //when
        List<Post> list = repository.findAll();

        //then
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}