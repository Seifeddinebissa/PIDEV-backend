package tn.esprit.Gestion_entreprise.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.Gestion_entreprise.entities.Favorite;

import java.util.Collection;
import java.util.List;

public interface FavoriteRepo extends JpaRepository<Favorite, Long> {
    void deleteByOffreId(Long offreId);
    boolean existsByStudentIdAndOffreId(Long studentId, Long offreId);
    @Query("SELECT f FROM Favorite f JOIN FETCH f.offre WHERE f.studentId = :studentId")
    List<Favorite> findByStudentId(Long studentId);
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.studentId = :studentId AND f.offre.id = :offerId")
    void deleteByStudentIdAndOffreId(@Param("studentId") Long studentId, @Param("offerId") Long offerId);

    @Query("SELECT f.offre, COUNT(f) as count FROM Favorite f GROUP BY f.offre ORDER BY count DESC")
    List<Object[]> findTopFavoritedOffers();


}