package lk.poools.top.springbot.database.repository;

import lk.poools.top.springbot.database.entity.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Optional;

@Repository
public interface PoolRepository extends JpaRepository<Pool, Long> {

    boolean existsPoolByCalendar(Calendar calendar);

    Optional<Pool> findPoolByCalendar(Calendar calendar);
}
