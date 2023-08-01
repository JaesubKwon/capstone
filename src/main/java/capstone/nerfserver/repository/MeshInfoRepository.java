package capstone.nerfserver.repository;

import capstone.nerfserver.domain.MeshInfo;

import java.util.Optional;

public interface MeshInfoRepository {

    void save(Long id, MeshInfo meshInfo);
    Boolean update(Long id, Double xSize, Double ySize, Double zSize);  //If id doesn't exist, return false. Or else return true
    Optional<MeshInfo> findById(Long id);
}
