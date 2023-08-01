package capstone.nerfserver.repository;

import capstone.nerfserver.domain.MeshInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryMeshInfoRepository implements MeshInfoRepository{
    private static Map<Long, MeshInfo> store = new HashMap<>();
    @Override
    public void save(Long id, MeshInfo meshInfo) {
        store.put(id, meshInfo); //HashMap.put은 id가 이미 존재하는 key일 경우 value가 수정됨
    }

    @Override
    public Boolean update(Long id, Double xSize, Double ySize, Double zSize) {
        if(!store.containsKey(id)){
            return false;
        }
        store.put(id, new MeshInfo(xSize, ySize, zSize));
        return true;
    }

    @Override
    public Optional<MeshInfo> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public void clearStore() {
        store.clear();
    }
}
