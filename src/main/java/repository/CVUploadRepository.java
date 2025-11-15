package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.CvUpload;

@Repository
public interface CVUploadRepository extends JpaRepository<CvUpload, Long> {
}