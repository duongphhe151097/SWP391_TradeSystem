package DataAccess;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByTokenAndStatus(String token, int status);
}

