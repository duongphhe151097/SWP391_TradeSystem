package Dtos;


import jakarta.servlet.http.HttpSession;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionDto {
    private boolean isValid;
    private String message;
    private HttpSession session;
    private UUID userId;
}
